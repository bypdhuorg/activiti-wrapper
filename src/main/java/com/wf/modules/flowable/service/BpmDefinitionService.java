package com.wf.modules.flowable.service;

import com.wf.base.BaseService;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.modules.flowable.entity.BpmDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 流程定义接口
 * @author meibo
 */
public interface BpmDefinitionService extends BaseService<BpmDefinition,String> {

    /**
     * 多条件分页获取(流程定义)
     * @param bpmDefinition
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<BpmDefinition> findByCondition(BpmDefinition bpmDefinition, SearchTimeVo searchVo, Pageable pageable);

    BpmDefinition suspend(String id);

    BpmDefinition active(String id);
}