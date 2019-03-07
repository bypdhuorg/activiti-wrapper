package com.flowablewrapper.services.db;

import com.flowablewrapper.base.BaseService;
import com.flowablewrapper.bean.db.BusTable;


/**
 * 业务库表接口
 * @author meibo
 */
public interface BusTableService extends BaseService<BusTable,String> {
    boolean isTableCreated(String tableName);

    void createTable(BusTable busTable);

    void dropTable(String tableName);

    /**
     * 通过数据库表面获取数据库结构
     * @param tableName
     * @return
     */
    public BusTable getByTableName(String tableName);
}
