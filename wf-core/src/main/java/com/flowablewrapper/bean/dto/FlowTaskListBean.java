package com.flowablewrapper.bean.dto;

import lombok.Data;

@Data
public class FlowTaskListBean {
    /**
     * ID
     */
    private String id;

    private String executionId;

    /**
     * 申请人用户BuId
     */
    private String buId;

    /**
     * 申请人用户显示名
     */
    private String applyUserId;

    /**
     * 申请人用户显示名
     */
    private String applyUserName;

    /**
     * 标题
     */
    private String title;

    /**
     * 流程状态
     */
    private String status;

    /**
     * processInstanceId
     */
    private String processInstanceId;

    /**
     * 流程申请时间
     */
    private long startTime;

    /**
     * 更新时间
     */
    private long updateTime;


    /**
     * 当前TaskName
     */
    private String taskName;


    /**
     * 当前TaskKey
     */
    private String taskKey;


    /**
     * 当前 处理人
     */
    private String assignee;
}
