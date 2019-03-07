package com.flowablewrapper.common.table;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 表SQL
 *
 * @author starmark
 * @create 2018-04-07 14:26
 **/
@Data
public class TableSql {

    /**
     * 表名
     */
    private String name;
    /**
     * 主键
     */
    private String primaryKey;

    private String comment;

    /**
     * 字段SQL
     */
    private List<ColumnSql> columnSqls=new ArrayList<ColumnSql>();

}
