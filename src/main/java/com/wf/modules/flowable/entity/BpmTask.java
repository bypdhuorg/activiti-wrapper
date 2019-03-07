package com.wf.modules.flowable.entity;

import com.wf.base.BaseEntity;
import com.wf.common.enums.EnumTaskStatus;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 流程任务 业务数据
 * @author meibo
 */
@Data
@Entity
@Table(name = "wf_bpm_task")
@ApiModel(value = "流程任务")
public class BpmTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 流程实例标题
     */
    @Column(columnDefinition="varchar(512) COMMENT '待办事项标题'")
    private String subject;

    /**
     * 流程实例标题
     */
    @Column(columnDefinition="varchar(512) COMMENT '任务名称'")
    private String taskname;

    /**
     * 流程实例ID
     */
    private String instId;

    /**
     *  流程定义ID
     */
    private String defId;

    /**
     * BPMN流程实例ID
     */
    private String actInstId;


    /**
     * BPMN执行id
     */
    private String actExecutionId;

    /**
     * BPMN关联 - 任务节点ID
     */
    private String actNodeId;
    /**
     * BPMN关联 - 任务节点ID
     */
    private String parentId;
    /**
     * 任务执行人ID
     */
    private String assigneeId;

    /**
     * 任务执行人
     */
    private String assigneeName;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private EnumTaskStatus status;
    /**
     * 任务优先级
     */
    private Integer priority;
    /**
     * 任务到期时间
     */
    private Date dueTime;

    /**
     * 任务到期时间
     */
    private String taskType;
}