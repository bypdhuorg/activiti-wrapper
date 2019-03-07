package com.flowablewrapper.engine.model;

import java.util.Date;

public interface IBpmInstance {
    Short INSTANCE_FORBIDDEN = Short.valueOf((short)1);
    Short INSTANCE_NO_FORBIDDEN = Short.valueOf((short)0);

    String getId();

    String getSubject();

    Long getDefId();

    String getActDefId();

    String getDefKey();

    String getDefName();

    String getBizKey();

    String getStatus();

    Date getEndTime();

    Long getDuration();

    String getTypeId();

    String getActInstId();

    String getCreateBy();

    Date getCreateTime();

    String getCreateOrgId();

    String getUpdateBy();

    Date getUpdateTime();

    Boolean getIsForbidden();

//    String getIsFormmal();
//
//    String getParentInstId();
//
//    String getDataMode();
//
//    Integer getSupportMobile();
//
//    String getSuperNodeId();
//
//    Boolean hasCreate();
//
//    void setHasCreate(Boolean var1);
}
