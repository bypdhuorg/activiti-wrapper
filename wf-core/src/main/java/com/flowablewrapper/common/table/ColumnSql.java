package com.flowablewrapper.common.table;

import com.flowablewrapper.bean.db.BusColumn;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 字段SQL
 *
 * @author meibo
 **/
@Data
public class ColumnSql   implements Serializable {

    /**
     * 字段拼接
     * @param column
     * @param primaryKeys
     */
    public ColumnSql(BusColumn column, List<String> primaryKeys ){
        this.setFieldName(column.getFieldName());
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(" ");
        stringBuilder.append(column.getFieldType().getMysqltype());

        if(column.getColumnLength()>0){
            if(column.getColumnDecimalLength()>0){
                stringBuilder.append("(").append(column.getColumnLength()).append(",").append(column.getColumnDecimalLength()).append(")");
            }else {
                stringBuilder.append("(").append(column.getColumnLength()).append(")");
            }
        }
        stringBuilder.append(" ");
        if(column.getColumnIsCanNull()&&!primaryKeys.contains(column.getFieldName())){
            stringBuilder.append("NULL");
        } else {
            stringBuilder.append("NOT NULL");
        }
        if(column.getIsAutoIncrement()){
            stringBuilder.append(" ");
            stringBuilder.append("AUTO_INCREMENT");
        }
        if(StringUtils.isNoneEmpty(column.getDefaultValue())){
            stringBuilder.append(" ");
            stringBuilder.append("DEFAULT " +column.getDefaultValue());
        }

        stringBuilder.append(" COMMENT '" + column.getColumnComment() + "'");
        this.setFieldType(stringBuilder.toString());
    }
    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 字段名称
     */
    private String fieldName;
}
