package com.wf.modules.flowable.service.flowable;

import com.wf.common.exception.UserClientException;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.Map;

@EnableAutoConfiguration
@Service
@Slf4j
public class WfInstanceService {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 启动流程
     * @param userID
     * @param actDefId
     * @param businessKey
     * @param variables
     * @return
     */
    public String startProcessInstance(String userID,String actDefId, String businessKey, Map<String, Object> variables){
        String instanceID=null;
        try {
            //设置流程发起人
            Authentication.setAuthenticatedUserId(userID);
            ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceById(
                    actDefId, businessKey, variables);
            instanceID = instance.getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //清除流程发起人
            Authentication.setAuthenticatedUserId(null);
        }

        return instanceID;
    }

    /**
     *激活 流程
     * @param bpmnInstanceId
     */
    public void activateProcessInstanceById(String bpmnInstanceId) {
        try {
            processEngine.getRuntimeService().activateProcessInstanceById(bpmnInstanceId);
        }catch (Exception e) {
            throw new UserClientException("flowablad流程执行错误"+e.getMessage());
        }
    }

    /**
     * 挂起流程
     * @param bpmnInstanceId
     */
    public void suspendProcessInstanceById(String bpmnInstanceId) {
        try {
          processEngine.getRuntimeService().suspendProcessInstanceById(bpmnInstanceId);
        }catch (Exception e) {
            throw new UserClientException("flowablad流程执行错误"+e.getMessage());
        }
    }

    /**
     * 删除流程
     * @param bpmnInstId
     * @param reason
     */
    public void deleteProcessInstance(String bpmnInstId, String reason) {
        processEngine.getRuntimeService().deleteProcessInstance(bpmnInstId, reason);
    }
}
