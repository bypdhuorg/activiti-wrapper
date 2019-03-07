package com.wf.modules.flowable.engine;

import org.flowable.engine.RuntimeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EventRegisterCommandLineRunner implements CommandLineRunner {
    protected final RuntimeService runtimeService;

    public EventRegisterCommandLineRunner(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public void run(String... args) {
        System.out.println("register flowable event listener...");
        WfGlobalEventListener e = new WfGlobalEventListener();
        runtimeService.addEventListener(e);
    }
}
