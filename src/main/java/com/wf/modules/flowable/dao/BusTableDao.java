package com.wf.modules.flowable.dao;

import com.wf.base.BaseDao;
import com.wf.modules.flowable.entity.BusTable;

/**
 * 业务表定义数据处理层
 * @author meibo
 */
public interface BusTableDao extends BaseDao<BusTable,String> {

    /**
     * 通过表ID获取
     * @param tableName
     * @return
     */
    BusTable findByTableName(String tableName);

    BusTable findByTableNameAndIdNot(String tableName,String id);
}