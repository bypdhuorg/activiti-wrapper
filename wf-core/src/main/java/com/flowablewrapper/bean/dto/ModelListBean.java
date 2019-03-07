package com.flowablewrapper.bean.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ModelListBean {
    /**
     * ID
     */
    private String id;

    private String name;

    private String deploymentId;
    /**
     *
     */
    private String key;

    /**
     *
     */
    private String category;

    /**
     *
     */
    private Integer version;

    /**
     *
     */
    private Date lastUpdateTime;

}
