package com.wf.modules.flowable.service;

import com.wf.base.BaseService;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.modules.flowable.entity.BpmModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 模型定义接口
 * @author meibo
 */
public interface BpmModelService extends BaseService<BpmModel,String> {

    /**
     * 多条件分页获取(模型定义)
     * @param bpmModel
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<BpmModel> findByCondition(BpmModel bpmModel, SearchTimeVo searchVo, Pageable pageable);


    /**
     * 获取流程图
     * @param actModId
     * @return
     */
    byte[] getModelThumbnail(String actModId);

    /**
     * 根据ID获取
     * @param id
     * @return
     */
    BpmModel getByActModelId(String actModelId);

    BpmModel deployModel(String id);
}