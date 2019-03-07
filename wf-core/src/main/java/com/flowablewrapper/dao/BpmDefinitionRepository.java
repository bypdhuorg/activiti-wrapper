package com.flowablewrapper.dao;

import com.flowablewrapper.base.BaseRepository;
import com.flowablewrapper.bean.db.BpmDefinition;

import java.util.List;

/**
 *
 */
public interface BpmDefinitionRepository extends BaseRepository<BpmDefinition, String> {


    /**
     * 通过Key查询
     * @param key
     * @return
     */
    List<BpmDefinition> findByKey(String key);

    /**
     * 通过Key查询
     * @param key
     * @return
     */
    BpmDefinition findByKeyAndIsMain(String key,Boolean isMain);
}
