package com.wf.modules.flowable.entity;

import com.wf.base.BaseEntity;
import com.wf.common.enums.EnumInstanceResult;
import com.wf.common.enums.EnumInstanceStatus;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程实例 业务数据
 * @author meibo
 */
@Data
@Entity
@Table(name = "wf_bpm_instance")
@ApiModel(value = "流程实例")
public class BpmInstance extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
     * flowable流程定义ID
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
    @Enumerated(EnumType.STRING)
    private EnumInstanceResult result;
    /**
     * 实例状态
     */
    @Enumerated(EnumType.STRING)
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

    @Transient
    private String busData;

    @Transient
    private Map<String,Object> bizDBData=new HashMap<>();

    @Transient
    private BpmDefinition bpmDefinition;
}