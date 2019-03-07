package com.wf.modules.flowable.service;

import com.wf.base.BaseService;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.modules.flowable.entity.BusTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 业务表定义接口
 * @author meibo
 */
public interface BusTableService extends BaseService<BusTable,String> {

    /**
     * 多条件分页获取(业务表定义)
     * @param busTable
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<BusTable> findByCondition(BusTable busTable, SearchTimeVo searchVo, Pageable pageable);

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