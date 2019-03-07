package com.flowablewrapper.dao;

import com.flowablewrapper.base.BaseRepository;
import com.flowablewrapper.bean.db.BusColumn;

import java.util.List;

/**
 * 业务库 字段 数据处理层
 */
public interface BusColumnRepository extends BaseRepository<BusColumn, String> {


    /**
     * 通过表ID获取
     * @param tableId
     * @return
     */
    List<BusColumn> findByTableId(String tableId);


    /**
     * 通过表ID删除
     * @param tableId
     */
    void deleteByTableId(String tableId);

}
