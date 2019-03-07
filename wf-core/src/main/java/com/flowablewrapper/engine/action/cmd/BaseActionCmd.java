package com.flowablewrapper.engine.action.cmd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flowablewrapper.bean.bo.BusData;
import com.flowablewrapper.bean.db.BpmDefinition;
import com.flowablewrapper.bean.db.BpmInstance;
import com.flowablewrapper.engine.action.handle.IActionHandler;
import com.flowablewrapper.engine.constants.EnumTaskActionType;
import com.flowablewrapper.engine.model.ISysIdentity;
import com.flowablewrapper.exception.BusinessException;
import com.flowablewrapper.exception.StatusCode;
import com.flowablewrapper.common.utils.ApplicationContextProvider;
import com.flowablewrapper.common.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseActionCmd implements IActionCmd{

    public abstract void initSpecialParam(JSONObject var1);

    /**
     * 是否在执行中
     */
    protected boolean hasExecuted = false;

    @Getter @Setter
    private String actionName;

    @Getter @Setter
    private String businessKey;

    @Getter @Setter
    private String busData;


    @Getter @Setter
    private BusData busDataObj;

    @Setter
    protected String defId;

    @Getter @Setter
    protected String instanceId;

    @Getter @Setter
    protected BpmInstance bpmInstance = null;

    @Getter @Setter
    protected BpmDefinition bpmDefinition;

    @Getter @Setter
    protected Map<String, Object> actionVariables = new HashMap();

    protected Map<String, List<ISysIdentity>> identityMap_ = new HashMap();

    public abstract String getNodeId();

    public BaseActionCmd(){}

    public BaseActionCmd(String flowParamStr) {

        //1.分解传入json字符串
        com.alibaba.fastjson.JSONObject flowParam = JSON.parseObject(flowParamStr);
        this.setActionName(flowParam.getString("action"));

        //2.如果有，设置task实例ID
        String defId = flowParam.getString("defId");
        if (StringUtils.isNotEmpty(defId)) {
            this.setDefId(defId);
        }

        String instanceId = flowParam.getString("instanceId");
        if (StringUtils.isNotEmpty(instanceId)) {
            this.setInstanceId(instanceId);
        }
        //初始化特殊属性
        initSpecialParam(flowParam);

        //3.取得执行人,设置执行人

        //4.取得数据，设置数据
        setBusData(JsonUtils.getString(flowParam, "data"));

    }

    /**
     * 执行
     * @return
     */
    @Override
    public synchronized String executeCmd() {
        if (this.hasExecuted) {
            throw new BusinessException("action cmd caonot be invoked twice", StatusCode.NO_PERMISSION);
        } else {
            this.hasExecuted = true;
            EnumTaskActionType actonType = EnumTaskActionType.fromKey(this.getActionName());
            IActionHandler handler = (IActionHandler) ApplicationContextProvider.getBean(actonType.getBeanId());
            if (handler == null) {
                throw new BusinessException("action beanId cannot be found :" + actonType.getName(), StatusCode.NO_TASK_ACTION);
            } else {
                    handler.execute(this);
                return handler.getActionType().getName();
            }
        }
    }

    @Override
    public String getDefId() {
        return this.defId==null && this.bpmInstance != null ? this.bpmInstance.getDefId() : this.defId;
    }


    public void setBpmIdentities(Map<String, List<ISysIdentity>> map) {
        this.identityMap_ = map;
    }

    public void clearBpmIdentities() {
        this.identityMap_.clear();
    }

    public void addBpmIdentity(String key, ISysIdentity bpmIdentity) {
        List<ISysIdentity> list = (List)this.identityMap_.get(key);
        if (list==null) {
            list = new ArrayList();
            list.add(bpmIdentity);
            this.identityMap_.put(key, list);
        } else {
            list.add(bpmIdentity);
        }

    }

    public void addBpmIdentity(String key, List<ISysIdentity> bpmIdentityList) {
        List<ISysIdentity> list = (List)this.identityMap_.get(key);
        if (list==null) {
            list = new ArrayList();
            list.addAll(bpmIdentityList);
            this.identityMap_.put(key, list);
        } else {
            list.addAll(bpmIdentityList);
        }

    }

    @Override
    public void setBpmIdentity(String key, List<ISysIdentity> bpmIdentityList) {
        List<ISysIdentity> list = (List)this.identityMap_.get(key);
        if (list==null) {
            list = new ArrayList();
            list.addAll(bpmIdentityList);
            this.identityMap_.put(key, list);
        } else {
            list.clear();
            list.addAll(bpmIdentityList);
        }

    }

    @Override
    public List<ISysIdentity> getBpmIdentity(String nodeId) {
        return (List)this.identityMap_.get(nodeId);
    }

    @Override
    public Map<String, List<ISysIdentity>> getBpmIdentities() {
        return this.identityMap_;
    }

}
