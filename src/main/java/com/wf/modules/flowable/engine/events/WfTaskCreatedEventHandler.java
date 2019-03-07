package com.wf.modules.flowable.engine.events;

import com.wf.common.enums.EnumTaskStatus;
import com.wf.common.utils.ApplicationContextProvider;
import com.wf.modules.flowable.entity.BpmTask;
import com.wf.modules.flowable.service.BpmTaskService;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.task.api.Task;

public class WfTaskCreatedEventHandler {
    protected final FlowableEvent event;

    private BpmTaskService bpmTaskService;
    public WfTaskCreatedEventHandler(FlowableEvent event) {
        this.event = event;
    }

    public void run() {
        bpmTaskService =(BpmTaskService)ApplicationContextProvider.getBean(BpmTaskService.class);
        System.out.println("WfTaskCreatedEventHandler event handler...");
        Task task = (Task) ((FlowableEntityEvent) event).getEntity();

        BpmTask dbTask=new BpmTask();
        dbTask.setSubject("");
        dbTask.setTaskname(task.getName());

        dbTask.setActExecutionId(task.getExecutionId());
        dbTask.setActInstId(task.getProcessInstanceId());
//        dbTask.setDefId(iBpmInstance.getDefId());
        dbTask.setId(task.getId());
//        dbTask.setInstId(iBpmInstance.getId());
        dbTask.setActNodeId(task.getTaskDefinitionKey());
        dbTask.setParentId(task.getParentTaskId());
        dbTask.setPriority(task.getPriority());
        dbTask.setStatus(EnumTaskStatus.NORMAL);
//        dbTask.setdbTaskType(a(nodeDef.getType()));
//
//        dbTask.setTypeId(iBpmInstance.getTypeId());
        bpmTaskService.save(dbTask);

        System.out.println("task  name is: " + task.getName());
    }
}
