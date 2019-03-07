package com.wf.modules.flowable.dao;

import com.wf.base.BaseDao;
import com.wf.modules.flowable.entity.BpmDefinition;
import org.springframework.data.jpa.repository.Query;

/**
 * 流程定义数据处理层
 * @author meibo
 */
public interface BpmDefinitionDao extends BaseDao<BpmDefinition,String> {

    @Query(value="select * from wf_bpm_definition where wf_bpm_definition.bpm_key =?1 order by wf_bpm_definition.bpm_version desc limit 1", nativeQuery = true)
    BpmDefinition findMaxVersionByKey(String bpm_key);
}