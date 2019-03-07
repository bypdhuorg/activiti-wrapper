package com.wf.modules.flowable.dao;

import com.wf.base.BaseDao;
import com.wf.modules.flowable.entity.BpmModel;

/**
 * 模型定义数据处理层
 * @author meibo
 */
public interface BpmModelDao extends BaseDao<BpmModel,String> {
    /**
     * 通过Key查询
     * @param key
     * @return
     */
    BpmModel findByKey(String key);

    BpmModel findByName(String name);

    BpmModel findByKeyAndIdNot(String key,String id);

    BpmModel findByNameAndIdNot(String name,String id);

    BpmModel findByActModelId(String actModelId);
    /**
     * 通过Key查询
     * @param key
     * @return
     */
    BpmModel findByKeyAndIsMain(String key,Boolean isMain);
}