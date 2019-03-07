package com.flowablewrapper.dao;

import com.flowablewrapper.base.BaseRepository;
import com.flowablewrapper.bean.db.BpmInstance;

import java.util.List;

/**
 *
 */
public interface BpmInstanceRepository extends BaseRepository<BpmInstance, String> {

    /**
     * 通过DB 流程定义ID查询
     * @param defId
     * @return
     */
    List<BpmInstance> findByDefId(String defId);
}
