package com.wf.modules.flowable.engine.events;

import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.task.api.Task;

public class WfTaskCompleteEventHandler {
    protected final FlowableEvent event;

    public WfTaskCompleteEventHandler(FlowableEvent event) {
        this.event = event;
    }

    public void run() {
        System.out.println("WfTaskAddedEventHandler event handler...");
        Task process = (Task) ((FlowableEntityEvent) event).getEntity();
        System.out.println("task  name is: " + process.getName());
    }
}
