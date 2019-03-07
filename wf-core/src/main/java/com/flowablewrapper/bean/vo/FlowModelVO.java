package com.flowablewrapper.bean.vo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.util.Date;

@Data
public class FlowModelVO {

    protected String id;
    protected String name;
    protected String category;
    protected String key;
    protected String description;
    protected String createdBy;
    protected String lastUpdatedBy;
    protected Date lastUpdated;
    protected boolean latestVersion;
    protected int version;
    protected String comment;
    protected Integer modelType;
    protected String tenantId;

    protected ObjectNode objectNode;
    protected String modeltype;
    protected String json_xml;
    protected boolean newversion;

}
