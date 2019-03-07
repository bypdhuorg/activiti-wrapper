package com.wf.modules.flowable.dao;

import com.wf.base.BaseDao;
import com.wf.modules.flowable.entity.BusColumn;

import java.util.List;

/**
 * 业务字段表数据处理层
 * @author meibo
 */
public interface BusColumnDao extends BaseDao<BusColumn,String> {

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