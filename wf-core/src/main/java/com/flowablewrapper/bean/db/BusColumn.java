package com.flowablewrapper.bean.db;

import com.baomidou.mybatisplus.annotations.TableName;
import com.flowablewrapper.base.BaseEntity;
import com.flowablewrapper.common.constant.EnumColumnType;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 */
@Data
@Entity
@ToString
@Table(name = "bus_column")
@TableName("bus_column")
@ApiModel(value = "业务字段表")
public class BusColumn extends BaseEntity {

    @Column(columnDefinition="varchar(255) COMMENT '表id'")
    private String tableId;

    @Column(columnDefinition="varchar(64) COMMENT '数据库字段名'")
    private String fieldName;

    @Column(columnDefinition="varchar(64) COMMENT '字段描述'")
    private String columnComment="";

    @Column(columnDefinition="BIT(1) COMMENT '是否是主键'")
    private Boolean primaryKey=false;


    @Column(columnDefinition="BIT(1) COMMENT '是否是自增长'")
    private Boolean isAutoIncrement=false;

    @Column(columnDefinition="varchar(64) COMMENT '字段类型'")
    private EnumColumnType fieldType=EnumColumnType.VARCHAR;

    @Column(columnDefinition="int(11) COMMENT '字段长度'")
    private int columnLength=0;

    @Column(columnDefinition="int(11) COMMENT '小数长度'")
    private Integer columnDecimalLength=0;

    @Column(columnDefinition="BIT(1) COMMENT '是否允许为Null'")
    private Boolean columnIsCanNull=true;

    @Column(columnDefinition="varchar(64) COMMENT '默认值'")
    private String defaultValue="";

}
