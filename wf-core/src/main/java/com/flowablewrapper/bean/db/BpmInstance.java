package com.flowablewrapper.bean.db;

import com.baomidou.mybatisplus.annotations.TableName;
import com.flowablewrapper.base.BaseEntity;
import com.flowablewrapper.engine.constants.EnumInstanceStatus;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 *
 */
@Data
@Entity
@ToString
@Table(name = "bpm_instance")
@TableName("bpm_instance")
@ApiModel(value = "流程定义")
public class BpmInstance extends BaseEntity {

    /**
     * 流程实例标题
     */
    @Column(columnDefinition="varchar(512) COMMENT '流程实例标题'")
    private String subject;

    /**
     * 流程定义ID
     */
    private String defId;

    /**
     * BPMN流程定义ID
     */
    private String actDefId;

    /**
     * 流程定义Key
     */
    private String defKey;

    /**
     * 流程名称
     */
    private String defName;

    /**
     * 关联数据业务主键
     */
    private String bizKey;

    /**
     * 实例状态
     */
    private EnumInstanceStatus status;

    /**
     * 实例结束时间
     */
    private Date endTime;

    /**
     * 持续时间(ms)
     */
    private long duration;

    /**
     * 所属分类ID
     */
    private String typeId;

    /**
     * BPMN流程实例ID
     */
    private String actInstId;

    /**
     * 禁止
     */
    private Boolean isForbidden;


}
