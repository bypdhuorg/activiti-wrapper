package com.wf.modules.flowable.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.enums.EnumBpmDefinitionStatus;
import com.wf.common.enums.EnumInstanceResult;
import com.wf.common.enums.EnumInstanceStatus;
import com.wf.common.exception.UserClientException;
import com.wf.modules.flowable.dao.BpmDefinitionDao;
import com.wf.modules.flowable.dao.BpmInstanceDao;
import com.wf.modules.flowable.entity.BpmDefinition;
import com.wf.modules.flowable.entity.BpmInstance;
import com.wf.modules.flowable.entity.BusData;
import com.wf.modules.flowable.entity.BusTable;
import com.wf.modules.flowable.service.BpmInstanceService;
import com.wf.modules.flowable.service.BusDataService;
import com.wf.modules.flowable.service.BusTableService;
import com.wf.modules.flowable.service.flowable.WfInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流程实例接口实现
 * @author meibo
 */
@Slf4j
@Service
@Transactional
public class BpmInstanceServiceImpl extends BpmInstanceBaseService implements BpmInstanceService {

    @Autowired
    private BpmInstanceDao bpmInstanceDao;

    @Autowired
    private BpmDefinitionDao bpmDefinitionDao;

    @Autowired
    private BusTableService busTableService;

    @Autowired
    private BusDataService busDataService;

    @Autowired
    private WfInstanceService wfInstanceService;

    @Override
    public BpmInstanceDao getRepository() {
        return bpmInstanceDao;
    }

    @Override
    public void saveCheck(BpmInstance bpmInstance){
        //流程定义 check
        BpmDefinition bpmDefinition =bpmDefinitionDao.findById(bpmInstance.getDefId())
                .orElseThrow(()->new UserClientException("流程定义不存在，请联系管理员,definitionID:"+bpmInstance.getDefId()));
        if(EnumBpmDefinitionStatus.FORBIDDEN.equals(bpmDefinition.getStatus())){
            throw new UserClientException("流程定义已经被禁用，请联系管理员,definitionID:"+bpmInstance.getDefId());
        }

        bpmInstance.setActDefId(bpmDefinition.getActDefId());
        bpmInstance.setDefKey(bpmDefinition.getKey());
        bpmInstance.setDefName(bpmDefinition.getName());
        bpmInstance.setBpmDefinition(bpmDefinition);
    };
    @Override
    public void updateCheck(BpmInstance bpmInstance){
        this.saveCheck(bpmInstance);

        //状态检查
        BpmInstance orgBpmInstance =getRepository().findById(bpmInstance.getId())
                .orElseThrow(()->new UserClientException("流程实例不存在，请联系管理员,ID:"+bpmInstance.getId()));

        if(!EnumInstanceResult.DRAFT.equals(orgBpmInstance.getStatus())){
            throw new UserClientException("流程定义已经启动,ID:"+bpmInstance.getId());
        }
    };

    public BpmInstance getAndExistCheck(String id){
        return getRepository().findById(id)
                .orElseThrow(()->new UserClientException("流程实例DB中不存在"));
    };

    @Override
    public BpmInstance save(BpmInstance bpmInstance){
        saveCheck(bpmInstance);

        //取得对应的业务表
        saveDbBisData(bpmInstance);

        bpmInstance.setResult(EnumInstanceResult.DRAFT);
        bpmInstance.setStatus(EnumInstanceStatus.DRAFT);

        //保存表 bizKey
        getRepository().save(bpmInstance);

        return bpmInstance;
    }

    @Override
    public BpmInstance update(BpmInstance bpmInstance){
        //check
        updateCheck(bpmInstance);

        saveDbBisData(bpmInstance);

        //保存表 bizKey
        bpmInstance.setResult(EnumInstanceResult.DRAFT);
        bpmInstance.setStatus(EnumInstanceStatus.DRAFT);
        getRepository().save(bpmInstance);

        return bpmInstance;
    }

    @Override
    public BpmInstance saveAndStart(BpmInstance bpmInstance){
        //check,ID 会自动产生，用这个判断
        if(StringUtils.isEmpty(bpmInstance.getBizKey())){
            saveCheck(bpmInstance);
        }else{
            updateCheck(bpmInstance);
        }


        saveDbBisData(bpmInstance);

        String actIntaceId=wfInstanceService.startProcessInstance(bpmInstance.getCreateBy()
                ,bpmInstance.getActDefId(),bpmInstance.getDefKey()
                ,bpmInstance.getBizDBData());

        bpmInstance.setResult(EnumInstanceResult.RUNNING);
        bpmInstance.setStatus(EnumInstanceStatus.RUNNING);
        bpmInstance.setActInstId(actIntaceId);
        //保存表 bizKey
        getRepository().save(bpmInstance);

        return bpmInstance;
    }

    @Override
    public BpmInstance suspend(String id) {
        BpmInstance bpmDefinition =this.getAndExistCheck(id);

        //判断数据库状态,如果不是运行中，则直接返回
        if(!bpmDefinition.getStatus().equals(EnumInstanceStatus.RUNNING)){
            throw new UserClientException("流程状态不是运行中，不能挂起,ID:"+id);
        }
        wfInstanceService.suspendProcessInstanceById(bpmDefinition.getActInstId());
        bpmDefinition.setStatus(EnumInstanceStatus.SUSPEND);
        getRepository().save(bpmDefinition);

        return bpmDefinition;
    }

    @Override
    public BpmInstance active(String id) {
        BpmInstance bpmDefinition =this.getAndExistCheck(id);

        //判断数据库状态,如果已经是激活，则直接返回
        if(!bpmDefinition.getStatus().equals(EnumInstanceStatus.SUSPEND)){
            throw new UserClientException("流程状态不是挂起，不能激活,ID:"+id);
        }

        wfInstanceService.activateProcessInstanceById(bpmDefinition.getActInstId());
        bpmDefinition.setStatus(EnumInstanceStatus.RUNNING);
        getRepository().save(bpmDefinition);

        return bpmDefinition;
    }
    /**
     * 保存 业务信息到数据库
     * @param bpmInstance
     */
    private void saveDbBisData(BpmInstance bpmInstance){

        JSONObject data = JSONUtil.parseObj(bpmInstance.getBusData());

        //取得对应的业务表
        BpmDefinition bpmDefinition=bpmInstance.getBpmDefinition();
        String modelCode = bpmDefinition.getBunissnessTable();
        // 提交的数据中包换，做映射
        if (data.containsKey(modelCode)) {
            JSONObject bizData =  data.getJSONObject(modelCode);

            BusTable busTable=busTableService.getByTableName(modelCode);
            BusData busData =busDataService.parseJsonDataToBusData(busTable,bizData);
            busData =busDataService.saveData(busData);

            bpmInstance.getBizDBData().put(modelCode,bizData);
            bpmInstance.setBizKey(busData.getPkValue());
        }

        bpmInstance.setStatus(EnumInstanceStatus.DRAFT);

    }
    @Override
    /**
     * 多条件分页获取(流程实例)
     * @param bpmInstance
     * @param searchVo
     * @param pageable
     * @return
     */
    public Page<BpmInstance> findByCondition(BpmInstance bpmInstance, SearchTimeVo searchVo, Pageable pageable) {
        return getRepository().findAll(new Specification<BpmInstance>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<BpmInstance> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                //创建时间
                Path<Date> createTimeField=root.get("createTime");
                Path<String> subjectField=root.get("subject");

                List<Predicate> list = new ArrayList<Predicate>();

                //创建时间
                if(searchVo.getStartDate()!=null){
                    Date start = DateUtil.date(searchVo.getStartDate());
                    list.add(cb.greaterThanOrEqualTo(createTimeField, start));
                }
                if(searchVo.getEndDate()!=null){
                    Date end = DateUtil.date(searchVo.getEndDate());
                    list.add(cb.lessThanOrEqualTo(createTimeField, end));
                }

                Predicate[] arr = new Predicate[list.size()];
                cq.where(list.toArray(arr));
                return null;
            }
        }, pageable);
    }
}