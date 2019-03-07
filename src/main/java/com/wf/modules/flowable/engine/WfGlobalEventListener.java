package com.wf.modules.flowable.engine;

import com.wf.modules.flowable.engine.events.WfProcessStartedEventHandler;
import com.wf.modules.flowable.engine.events.WfTaskCreatedEventHandler;
import com.wf.modules.flowable.engine.events.WfTaskCompleteEventHandler;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.springframework.stereotype.Component;

@Component
public class WfGlobalEventListener implements FlowableEventListener {
    @Override
    public void onEvent(FlowableEvent event) {

        if(event.getType() == FlowableEngineEventType.PROCESS_CREATED) {
            System.out.println("A process created");
        } else if (event.getType() == FlowableEngineEventType.PROCESS_STARTED) {
            System.out.println("A process started...");
            WfProcessStartedEventHandler handler = new WfProcessStartedEventHandler(event);
            handler.run();
        } else if (event.getType() == FlowableEngineEventType.TASK_CREATED) {
            System.out.println("A task created...");
            WfTaskCreatedEventHandler handler = new WfTaskCreatedEventHandler(event);
            handler.run();
        } else if (event.getType() == FlowableEngineEventType.TASK_ASSIGNED) {
            System.out.println("A task assigned...");
//            TaskAssignedEventHandler handler = new TaskAssignedEventHandler(event);
//            handler.run();
        } else if (event.getType() == FlowableEngineEventType.TASK_COMPLETED) {
            System.out.println("A task completed...");
            WfTaskCompleteEventHandler handler = new WfTaskCompleteEventHandler(event);
            handler.run();
        } else {
            System.out.println("Event received: " + event.getType());
        }
    }

    @Override
    public boolean isFailOnException() {
        // The logic in the onEvent method of this listener is not critical, exceptions
        // can be ignored if logging fails...
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
