package com.flowablewrapper.dao.mapper;

import com.flowablewrapper.common.table.TableSql;
import org.apache.ibatis.annotations.Param;

/**
 * 表操作基本
 * 提供创建表，修改，drop
 */
public interface TableOperationMapper {

    /**
     * 根据表名查询表在库中是否存在，存在返回1，不存在返回0
     * @param tableName 表结构的map
     * @return 存在返回1，不存在返回0
     */
    public int findTableCountByTableName(@Param("tableName") String tableName);


    /**
     * 根据结构注解解析出来的信息创建表
     * @param tableSql
     */
    public void createTable(TableSql tableSql);


    /**
     * 增加字段
     * @param tableSql
     */
    public void addTableField(TableSql tableSql);

    /**
     * 根据表名删除表
     * @param tableName 表结构的map
     */
    public void dorpTableByName(@Param("tableName") String tableName);
}
