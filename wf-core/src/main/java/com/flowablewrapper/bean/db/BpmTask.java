package com.flowablewrapper.bean.db;

import com.baomidou.mybatisplus.annotations.TableName;
import com.flowablewrapper.base.BaseEntity;
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
@Table(name = "bpm_task")
@TableName("bpm_task")
@ApiModel(value = "流程任务")
public class BpmTask extends BaseEntity {

    @Column(length = 512)
    private String subject;

    @Column(length = 512)
    private String name;

    private String taskId;

    private String actExecutionId;

    private String nodeId;

    private String instId;

    private String defId;

    private String assigneeId;

    private String assigneeNames;

    private String status;

    private Integer priority;

    private Date dueTime;

    private String taskType;

    private String parentId;

    private String actInstId;

}
