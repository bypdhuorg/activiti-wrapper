package com.flowablewrapper.services.flowableable;

import com.flowablewrapper.Pager;
import com.flowablewrapper.bean.SortDirection;
import com.flowablewrapper.bean.dto.FlowTaskListBean;
import com.flowablewrapper.bean.vo.FlowRunningTaskPageVO;
import com.flowablewrapper.bean.vo.FlowStartTaskVO;
import com.flowablewrapper.common.utils.Constants;
import liquibase.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@EnableAutoConfiguration
@Service
@Slf4j
public class WfProcessTaskService {

    private ProcessEngine processEngine;


    @Autowired
    public WfProcessTaskService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }


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
        processEngine.getRuntimeService().activateProcessInstanceById(bpmnInstanceId);
    }

    /**
     * 挂起流程
     * @param bpmnInstanceId
     */
    public void suspendProcessInstanceById(String bpmnInstanceId) {
        processEngine.getRuntimeService().suspendProcessInstanceById(bpmnInstanceId);
    }

    /**
     * 删除流程
     * @param bpmnInstId
     * @param reason
     */
    public void deleteProcessInstance(String bpmnInstId, String reason) {
        processEngine.getRuntimeService().deleteProcessInstance(bpmnInstId, reason);
    }

    /**
     * 启动流程
     * @param instartVo
     */
    public void start(FlowStartTaskVO instartVo){

        //设置流程发起人
        Authentication.setAuthenticatedUserId(instartVo.getUserId());

        Map<String, Object> variables=new HashMap<>();
        variables.put(Constants.TASK_TITLE,instartVo.getTitle());
        variables.put(Constants.TASK_BUID,instartVo.getBuId());
        variables.put(Constants.TASK_APPLY_USER_ID,instartVo.getUserId());
        variables.put(Constants.TASK_APPLY_USERNAME,instartVo.getUserName());
        variables.put(Constants.TASK_PARAMS,instartVo.getProcessParams());

        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey(instartVo.getProcessDefinitionKey(),variables);


        //清除流程发起人
        Authentication.setAuthenticatedUserId(null);
    }

    /**
     * 公共任务-认领任务
     * @param taskId
     * @param userId
     */
    public void claim(String taskId,String userId){

        TaskService taskService =processEngine.getTaskService();
        // 让指定userId的用户认领指定taskId的任务
        taskService.claim(taskId, userId);
    }

    /**
     * 转办任务
     * @param taskId
     * @param userId
     */
    public void transfer(String taskId,String userId){

        TaskService taskService =processEngine.getTaskService();
        // 让指定userId的用户认领指定taskId的任务
        taskService.setAssignee(taskId, userId);
    }

    /**
     * 取消任务
     * @param processInstanceId
     * @param deleteReason
     */
    public void cancel(String processInstanceId, String deleteReason){

//        RuntimeService runtimeService =processEngine.getRuntimeService();
//        //删除流程实例
//        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    /**
     * 完成任务
     * @param taskId
     */
    public void complete(String taskId, Map<String, Object> variables){

        TaskService taskService =processEngine.getTaskService();
        taskService.setVariablesLocal(taskId,variables);
        // 完成任务
        taskService.complete(taskId);
    }

    /**
     * 取得当前正在运行中的任务
     * @param inVo
     * @return
     */
    public Pager<FlowTaskListBean> runningTasks(FlowRunningTaskPageVO inVo){
        TaskService taskService =processEngine.getTaskService();

        RuntimeService runtimeService =processEngine.getRuntimeService();

        TaskQuery taskQuery=taskService.createTaskQuery();

        //组织条件
        if(StringUtils.isNotEmpty(inVo.getDefinitionKey())){
            taskQuery.processDefinitionKey(inVo.getDefinitionKey());
        }
        //申请者
        if(StringUtils.isNotEmpty(inVo.getUserId())){
            taskQuery.processVariableValueEquals(Constants.TASK_APPLY_USER_ID,inVo.getUserId());
        }
        //Bu_id
        if(StringUtils.isNotEmpty(inVo.getBuId())){
            taskQuery.processVariableValueEquals(Constants.TASK_BUID,inVo.getBuId());
        }
        //任务名称
        if(StringUtils.isNotEmpty(inVo.getTaskName())){
            taskQuery.taskNameLike("%" +inVo.getTaskName()+ "%");
        }
        //当前处理人
        if(StringUtils.isNotEmpty(inVo.getAssigned())){
            taskQuery.taskCandidateOrAssigned(inVo.getAssigned());
        }
        //流程ID
        if(StringUtils.isNotEmpty(inVo.getProcessInstanceId())){
            taskQuery.processInstanceId(inVo.getProcessInstanceId());
        }

        //取得总条数
        int total =Math.toIntExact(taskQuery.count());

        //设置排序字段
        switch (inVo.getSortby().toLowerCase()){
//            case "id":
//                taskQuery.or;
//                break;
//            case "key":
//                taskQuery.orderByProcessDefinitionKey();
//                break;
//            case "category":
//                taskQuery.orderByProcessDefinitionCategory();
//                break;
//            case "name":
//                taskQuery.orderByProcessDefinitionName();
//                break;
            default:
                taskQuery.orderByTaskCreateTime();
        }

        //设置排序方向
        if(SortDirection.asc.equals(inVo.getDirection())){
            taskQuery.asc();
        }else{
            taskQuery.desc();
        }

        List<Task> orgtasklist= taskQuery.listPage(inVo.getPage(),inVo.getPageSize());


        //后处理
        List<FlowTaskListBean> returnList=new ArrayList<>();
        for (Task task : orgtasklist) {
//            Map<String, Object> params = runtimeService.getVariables(task.getExecutionId());
            Map<String, Object> params = taskService.getVariables(task.getId());
            FlowTaskListBean tempBean=new FlowTaskListBean();
            //任务ID
            tempBean.setId(task.getId());
            //任务名称x
            tempBean.setTaskName(task.getName());
            //执行对象的ID
            tempBean.setExecutionId(task.getExecutionId());
            //任务的执行人
            tempBean.setAssignee(task.getAssignee());
            //流程定义ID
            tempBean.setProcessInstanceId(task.getProcessInstanceId());
            //任务名称
            tempBean.setTitle((String)params.get(Constants.TASK_TITLE));
            //申请人
            tempBean.setApplyUserId((String)params.get(Constants.TASK_APPLY_USER_ID));
            //任务提出时间 开始时间
            tempBean.setStartTime(getStartTime(task.getProcessInstanceId()));
            tempBean.setUpdateTime(task.getCreateTime().getTime());

            returnList.add(tempBean);
        }
        return new Pager<FlowTaskListBean>(total,returnList);
    }

    public void historyTasks(){

    }

    /**
     * 取得当前任务的流程图
     * @param taskId
     * @return
     * @throws Exception
     */
    public InputStream getImageStream(String taskId) throws Exception{
        RuntimeService runtimeService =processEngine.getRuntimeService();
        RepositoryService repositoryService =processEngine.getRepositoryService();

        ProcessDiagramGenerator processDiagramGenerator=processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();

        //取得流程实例
        ProcessInstance processInstance= findProcessInstanceByTaskId(taskId);

        //流程走完的不显示图
        if(processInstance==null){
           return null;
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId=findTaskById(taskId).getProcessDefinitionId();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(InstanceId).list();

        //得到正在执行的的Id
        List<String> activityIds = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        InputStream imageStream = processDiagramGenerator.generateDiagram(
                bpmnModel, "png",
                activityIds,true);
        return imageStream;
    }



    /**
     * 根据任务ID获取对应的流程实例
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    public ProcessInstance findProcessInstanceByTaskId(String taskId)
            throws Exception {
        RuntimeService runtimeService =processEngine.getRuntimeService();
        // 找到流程实例
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery().processInstanceId(
                        findTaskById(taskId).getProcessInstanceId())
                .singleResult();
//
//        if (processInstance == null) {
//            throw new Exception("流程实例未找到!");
//        }
        return processInstance;
    }


    /**
     * 根据任务ID获得任务实例
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    private TaskEntity findTaskById(String taskId) throws Exception {
        TaskService taskService =processEngine.getTaskService();
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(
                taskId).singleResult();
        if (task == null) {
            throw new Exception("任务实例未找到!");
        }
        return task;
    }


    /**
     * 取得变量
     * @param processInstanceId
     * @param key
     * @return
     */
    protected String getVariableValue(String processInstanceId,String key) {
        HistoryService historyService =processEngine.getHistoryService();
        HistoricVariableInstance param = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).variableName(key).singleResult();

        if (param == null) {
            return null;
        } else {
            return param.getValue().toString();
        }
    }


    protected long getStartTime(String processInstanceId) {
        HistoryService historyService =processEngine.getHistoryService();
        HistoricProcessInstance his = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return his.getStartTime().getTime();
    }

}
