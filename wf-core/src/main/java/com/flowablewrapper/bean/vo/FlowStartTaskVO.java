package com.flowablewrapper.bean.vo;

import lombok.Data;

@Data
public class FlowStartTaskVO {

    /**
     * 发起人用户BuId
     */
    private String buId;

    /**
     * 发起人用户ID
     */
    private String userId;

    /**
     * 发起人用户显示名
     */
    private String userName;

    /**
     * 标题
     */
    private String title;

    /**
     * 流程Key
     */
    private String processDefinitionKey;

    /**
     * 流程参数,json字符串
     */
    private String processParams;
}
