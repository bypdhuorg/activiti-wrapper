package com.wf.modules.flowable.service;

import com.wf.base.BaseService;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.modules.flowable.entity.BpmInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 流程实例接口
 * @author meibo
 */
public interface BpmInstanceService extends BaseService<BpmInstance,String> {

    /**
     * 多条件分页获取(流程实例)
     * @param bpmInstance
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<BpmInstance> findByCondition(BpmInstance bpmInstance, SearchTimeVo searchVo, Pageable pageable);

    /**
     * 申请(发起流程)
     * @param bpmInstance
     * @return
     */
    BpmInstance saveAndStart(BpmInstance bpmInstance);

    /**
     * 挂起
     * @param id
     * @return
     */
    BpmInstance suspend(String id);

    /**
     * 激活
     * @param id
     * @return
     */
    BpmInstance active(String id);
}