package com.wf.modules.flowable.service;

import com.wf.base.BaseService;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.modules.flowable.entity.BpmTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 流程任务接口
 * @author meibo
 */
public interface BpmTaskService extends BaseService<BpmTask,String> {

    /**
     * 多条件分页获取(流程任务)
     * @param bpmTask
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<BpmTask> findByCondition(BpmTask bpmTask, SearchTimeVo searchVo, Pageable pageable);
}