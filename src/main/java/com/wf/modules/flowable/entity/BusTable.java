package com.wf.modules.flowable.entity;

import com.wf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务表定义 业务数据
 * @author meibo
 */
@Data
@Entity
@Table(name = "wf_bus_table")
@ApiModel(value = "业务表定义")
public class BusTable extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition="varchar(64) COMMENT '业务表key'")
    private String busKey;

    @Column(columnDefinition="varchar(64) COMMENT '表名'")
    private String tableName;

    @Column(columnDefinition="varchar(64) COMMENT '描述'")
    private String tableComment;


    @Column(columnDefinition="BIT(1) COMMENT '是否创建'")
    private Boolean isCreated;

    /**
     *列
     */
    @Transient
    protected List<BusColumn> columns;


    /**
     * <pre>
     * 系统只支持唯一主键的场景
     * </pre>
     *
     * @return
     */
    public BusColumn getPkColumn() {
        if (this.columns == null) {
            return null;
        }
        List<BusColumn> list = new ArrayList<>();
        for (BusColumn c : this.getColumns()) {
            if (c.getPrimaryKey()) {
                list.add(c);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * <pre>
     * 根据name获取字段
     * </pre>
     * @param name
     * @return
     */
    public BusColumn getColumn(String name) {
        if (this.columns == null) {
            return null;
        }
        for (BusColumn c : columns) {
            if (name.equals(c.getFieldName())) {
                return c;
            }
        }
        return null;
    }
}