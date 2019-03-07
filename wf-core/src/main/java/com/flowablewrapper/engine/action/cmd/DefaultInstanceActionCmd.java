package com.flowablewrapper.engine.action.cmd;

import lombok.Getter;
import lombok.Setter;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;

public class DefaultInstanceActionCmd extends BaseActionCmd
        implements IInstanceActionCmd{

    @Getter @Setter
    protected ExecutionEntity executionEntity;
    public DefaultInstanceActionCmd(String flowParam)
    {
        super(flowParam);
    }

//    @Override
//    public void initSpecialParam(JSONObject var1) {
//
//    }

    public DefaultInstanceActionCmd()
    {
    }

    @Override
    public void initSpecialParam(com.alibaba.fastjson.JSONObject var1) {

    }

    /**
     * 取得当前点ID
     * @return
     */
    @Override
    public String getNodeId()
    {
        return this.executionEntity.getActivityId();
    }

    @Override
    public String getSubject() {
        return getBpmInstance().getSubject();
    }


}
