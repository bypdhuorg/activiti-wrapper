package com.flowablewrapper.bean.db;

import com.baomidou.mybatisplus.annotations.TableName;
import com.flowablewrapper.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 */
@Data
@Entity
@ToString
@Table(name = "bpm_bus_link")
@TableName("bpm_bus_link")
@ApiModel(value = "流程实例与业务数据关系表")
public class BpmBizLink extends BaseEntity {

    /**
     * 流程定义ID
     */
    private String defId;

    /**
     * 流程实例ID
     */
    private String instId;

    /**
     * biz_code (业务表名)
     */
    private String bizCode;

    /**
     * 业务主键
     */
    private String bizId;



}
