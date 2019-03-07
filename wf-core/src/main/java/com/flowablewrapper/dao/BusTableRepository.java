package com.flowablewrapper.dao;

import com.flowablewrapper.base.BaseRepository;
import com.flowablewrapper.bean.db.BusTable;

import java.util.List;

/**
 * 业务表字段 数据处理层
 */
public interface BusTableRepository extends BaseRepository<BusTable, String> {

    /**
     * 通过表ID获取
     * @param tableName
     * @return
     */
    List<BusTable> findByTableName(String tableName);
}
