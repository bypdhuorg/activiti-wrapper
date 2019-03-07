package com.wf.modules.flowable.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.enums.EnumBpmDefinitionStatus;
import com.wf.common.exception.UserClientException;
import com.wf.modules.flowable.dao.BpmDefinitionDao;
import com.wf.modules.flowable.entity.BpmDefinition;
import com.wf.modules.flowable.service.BpmDefinitionService;
import com.wf.modules.flowable.service.flowable.WfDefinitionService;
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
 * 流程定义接口实现
 * @author meibo
 */
@Slf4j
@Service
@Transactional
public class BpmDefinitionServiceImpl implements BpmDefinitionService {

    @Autowired
    private BpmDefinitionDao bpmDefinitionDao;
    @Autowired
    private WfDefinitionService wfDefinitionService;

    @Override
    public BpmDefinitionDao getRepository() {
        return bpmDefinitionDao;
    }

    @Override
    public void saveCheck(BpmDefinition bpmDefinition){
       // if(getRepository().findByUserId(bpmDefinition.getUserId()) !=null){
       //     throw new UserClientException("XXX已经存在");
       // }
    };
    @Override
    public void updateCheck(BpmDefinition bpmDefinition){};

    public BpmDefinition getAndExistCheck(String id){
         return getRepository().findById(id)
                 .orElseThrow(()->new UserClientException("流程DB中不存在"));
    };

    @Override
    /**
     * 多条件分页获取(流程定义)
     * @param bpmDefinition
     * @param searchVo
     * @param pageable
     * @return
     */
    public Page<BpmDefinition> findByCondition(BpmDefinition bpmDefinition, SearchTimeVo searchVo, Pageable pageable) {
        return getRepository().findAll(new Specification<BpmDefinition>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<BpmDefinition> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //部署时间
                Path<Date> createTimeField=root.get("createTime");
                //标识Key
                Path<String> bpm_keyField=root.get("key");
                //流程名称
                Path<String> nameField=root.get("name");

                //流程状态
                Path<EnumBpmDefinitionStatus> statusField=root.get("status");

                List<Predicate> list = new ArrayList<Predicate>();

                //标识Key
                if(StringUtils.isNotEmpty(bpmDefinition.getKey())){
                    list.add(cb.like(bpm_keyField, '%'+bpmDefinition.getKey()+'%'));
                }

                //流程名称
                if(StringUtils.isNotEmpty(bpmDefinition.getName())){
                    list.add(cb.like(nameField, '%'+bpmDefinition.getName()+'%'));
                }


                //流程状态
                if(bpmDefinition.getStatus()!=null){
                    list.add(cb.equal(statusField, bpmDefinition.getStatus()));
                }

                //部署时间
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

    @Override
    public BpmDefinition suspend(String id) {
        BpmDefinition bpmDefinition =this.getAndExistCheck(id);

        //判断数据库状态,如果已经是禁止，则直接返回
        if(bpmDefinition.getStatus()==EnumBpmDefinitionStatus.FORBIDDEN){
            return bpmDefinition;
        }
        wfDefinitionService.suspendById(bpmDefinition.getActDefId());
        bpmDefinition.setStatus(EnumBpmDefinitionStatus.FORBIDDEN);
        getRepository().save(bpmDefinition);

        return bpmDefinition;
    }

    @Override
    public BpmDefinition active(String id) {
        BpmDefinition bpmDefinition =this.getAndExistCheck(id);

        //判断数据库状态,如果已经是激活，则直接返回
        if(bpmDefinition.getStatus()==EnumBpmDefinitionStatus.ACTIVE){
            return bpmDefinition;
        }

        wfDefinitionService.activeById(bpmDefinition.getActDefId());
        bpmDefinition.setStatus(EnumBpmDefinitionStatus.ACTIVE);
        getRepository().save(bpmDefinition);

        return bpmDefinition;
    }
}