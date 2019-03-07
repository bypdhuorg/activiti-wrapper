package com.wf.modules.flowable.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.enums.EnumBpmDefinitionStatus;
import com.wf.common.exception.UserClientException;
import com.wf.modules.flowable.dao.BpmDefinitionDao;
import com.wf.modules.flowable.dao.BpmModelDao;
import com.wf.modules.flowable.entity.BpmDefinition;
import com.wf.modules.flowable.entity.BpmModel;
import com.wf.modules.flowable.service.BpmModelService;
import com.wf.modules.flowable.service.flowable.WfModelService;
import lombok.extern.slf4j.Slf4j;
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
 * 模型定义接口实现
 * @author meibo
 */
@Slf4j
@Service
@Transactional
public class BpmModelServiceImpl implements BpmModelService {

    @Autowired
    private BpmModelDao bpmModelDao;


    @Autowired
    private BpmDefinitionDao bpmDefinitionDao;

    @Autowired
    private WfModelService wfModelService;

    @Override
    public BpmModelDao getRepository() {
        return bpmModelDao;
    }

    @Override
    public void saveCheck(BpmModel bpmModel){
        if(getRepository().findByKey(bpmModel.getKey()) !=null){
            throw new UserClientException("模型定义Key重复：" + bpmModel.getKey());
        }

        if(getRepository().findByName(bpmModel.getName()) !=null){
            throw new UserClientException("模型定义名称重复：" + bpmModel.getKey());
        }
    };
    @Override
    public void updateCheck(BpmModel bpmModel){
        if(getRepository().findByKeyAndIdNot(bpmModel.getKey(),bpmModel.getId()) !=null){
            throw new UserClientException("模型定义Key重复：" + bpmModel.getKey());
        }

        if(getRepository().findByNameAndIdNot(bpmModel.getName(),bpmModel.getId()) !=null){
            throw new UserClientException("模型定义名称重复：" + bpmModel.getKey());
        }
    };

    /**
     * 获取流程图
     * @param actModId
     * @return
     */
    @Override
    public byte[] getModelThumbnail(String actModId){
        //flowable 操作
        return wfModelService.getModelThumbnail(actModId);
    }

    @Override
    public BpmModel getByActModelId(String actModelId){

        BpmModel bpmModel =getRepository().findByActModelId(actModelId);

        if(bpmModel==null){
            throw new UserClientException("数据库没有该对象,actModelId:"+actModelId);
        }

        BpmModel wfbpmModel =wfModelService.getModelJson(bpmModel.getActModelId());

        bpmModel.setObjectNode(wfbpmModel.getObjectNode());

        return bpmModel;
    }

    @Override
    public BpmModel save(BpmModel bpmModel){
        saveCheck(bpmModel);

        bpmModel.setIsMain(true);
        bpmModel.setVersion(1);

        //flowable 操作
        wfModelService.createModelToFlowable(bpmModel);

        //保存表
        getRepository().save(bpmModel);

        return bpmModel;
    }


    @Override
    public BpmModel update(BpmModel bpmModel){
        updateCheck(bpmModel);

        //flowable 操作
        wfModelService.updateFlowableModel(bpmModel);

        //版本+1
        bpmModel.setVersion(bpmModel.getVersion()+1);
        //保存表
        getRepository().save(bpmModel);

        return bpmModel;
    }

    @Override
    public  BpmModel deployModel(String id){
        BpmModel bpmModel =getRepository().findById(id)
                .orElseThrow(()->new UserClientException("数据库没有该对象,Id:"+id));

        //发布流程
        BpmDefinition definition =wfModelService.deployFlowableModel(bpmModel);
        definition.setModelId(bpmModel.getId());
        definition.setCategory(bpmModel.getCategory());
        definition.setKey(bpmModel.getKey());
        definition.setName(bpmModel.getName());
        definition.setStatus(EnumBpmDefinitionStatus.ACTIVE);

        //版本处理
        BpmDefinition dbMaxVersionDefinition =bpmDefinitionDao.findMaxVersionByKey(bpmModel.getKey());
        if(dbMaxVersionDefinition==null){
            definition.setVersion(1);
        }else{
            definition.setVersion(dbMaxVersionDefinition.getVersion() + 1);
            dbMaxVersionDefinition.setIsMain(false);
            bpmDefinitionDao.save(dbMaxVersionDefinition);
        }

        definition.setIsMain(true);

        bpmDefinitionDao.save(definition);

        return bpmModel;
    }



    @Override
    /**
     * 多条件分页获取(模型定义)
     * @param bpmModel
     * @param searchVo
     * @param pageable
     * @return
     */
    public Page<BpmModel> findByCondition(BpmModel bpmModel, SearchTimeVo searchVo, Pageable pageable) {
        return getRepository().findAll(new Specification<BpmModel>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<BpmModel> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                Path<Date> createTimeField=root.get("createTime");

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