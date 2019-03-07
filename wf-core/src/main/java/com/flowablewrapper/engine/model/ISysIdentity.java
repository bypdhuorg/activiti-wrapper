package com.flowablewrapper.engine.model;

import java.io.Serializable;

public interface ISysIdentity extends Serializable {

    String TYPE_USER = "user";
    String TYPE_ROLE = "role";
    String TYPE_GROUP = "group";
    String TYPE_ORG = "org";
    String TYPE_POST = "post";
    String TYPE_JOB = "job";

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getType();

    void setType(String type);

}
