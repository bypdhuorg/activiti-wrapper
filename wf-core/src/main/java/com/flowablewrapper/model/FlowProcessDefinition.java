package com.flowablewrapper.model;

import lombok.Data;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.bpmn.data.IOSpecification;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *流程定义
 */
@Data
public class FlowProcessDefinition {
    protected String name;
    protected String description;
    protected String key;
    protected int version;
    protected String category;
    protected String deploymentId;
    protected String resourceName;
    protected String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    protected Integer historyLevel;
    protected String diagramResourceName;
    protected boolean isGraphicalNotationDefined;
    protected Map<String, Object> variables;
    protected boolean hasStartFormKey;
    protected int suspensionState ;
    protected boolean isIdentityLinksInitialized;
    protected List<IdentityLinkEntity> definitionIdentityLinkEntities = new ArrayList<>();
    protected IOSpecification ioSpecification;
    protected String derivedFrom;
    protected String derivedFromRoot;
    protected int derivedVersion;

    protected String engineVersion;
}
