package com.flowablewrapper.bean.dto;

import lombok.Data;

@Data
public class ProcessDefinitionBean {

    /**
     * processDefinitionId
     */
    private String processDefinitionId;
    /**
     * 类别
     */
    private String category;

    /**
     * 显式名称
     */
    private String name;

    /**
     * 版本
     */
    private int version;

    /**
     * 流程Key
     */
    private String key;

    /**
     * 流程Key
     */
    private String resourceName;
    /**
     * 部署ID
     */
    private String deploymentId;

    /**
     * 部署ID
     */
    private String status;

    /**
     * 部署ID
     */
    private long deployTime;

    private Boolean isSuspended;
}
