package com.flowablewrapper.services;

import com.flowablewrapper.services.flowableable.WfProcessDefinitionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ProcessServiceTest {

    @Autowired
    private WfProcessDefinitionService processService;
    @Before
    public void setUp() throws Exception {
//        ImageGenerator t=new ImageGenerator();
    }

    @Test
    public void deploy() {
        processService.deploy("办公流程","请假","testProcesskey1.bpmn","" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns:flowable=\"http://flowable.org/bpmn\" xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" targetNamespace=\"http://www.omg.org/spec/BPMN/20100524/MODEL\">\n" +
                "\n" +
                "    <process id=\"testProcessBB\">\n" +
                "\n" +
                "        <startEvent id=\"theStart\" name=\"Start\" flowable:initiator=\"initiator\" />\n" +
                "        <sequenceFlow sourceRef=\"theStart\" targetRef=\"userTask01\" />\n" +
                "\n" +
                "        <userTask id=\"userTask01\" name=\"User Task 01\" flowable:assignee=\"${initiator}\" />\n" +
                "        <sequenceFlow sourceRef=\"userTask01\" targetRef=\"theFork\" />\n" +
                "\n" +
                "        <parallelGateway id=\"theFork\" />\n" +
                "        <sequenceFlow sourceRef=\"theFork\" targetRef=\"userTask02\" />\n" +
                "        <sequenceFlow sourceRef=\"theFork\" targetRef=\"userTask03\" />\n" +
                "        <sequenceFlow sourceRef=\"theFork\" targetRef=\"nestedFork\" />\n" +
                "\n" +
                "        <userTask id=\"userTask02\" name=\"User Task 02\" flowable:assignee=\"${assignee02}\" />\n" +
                "        <sequenceFlow sourceRef=\"userTask02\" targetRef=\"serviceTask01\" />\n" +
                "\n" +
                "        <serviceTask id=\"serviceTask01\" flowable:class=\"org.flowable.delegate.DummyServiceTask\" />\n" +
                "        <sequenceFlow sourceRef=\"serviceTask01\" targetRef=\"serviceTask02\" />\n" +
                "\n" +
                "        <serviceTask id=\"serviceTask02\" flowable:class=\"org.flowable.delegate.DummyServiceTask\" />\n" +
                "        <sequenceFlow sourceRef=\"serviceTask02\" targetRef=\"serviceTask03\" />\n" +
                "\n" +
                "        <serviceTask id=\"serviceTask03\" flowable:class=\"org.flowable.delegate.DummyServiceTask\" />\n" +
                "        <sequenceFlow sourceRef=\"serviceTask03\" targetRef=\"theJoin\" />\n" +
                "\n" +
                "        <userTask id=\"userTask03\" name=\"User Task 03\" flowable:assignee=\"${assignee03}\" />\n" +
                "        <sequenceFlow sourceRef=\"userTask03\" targetRef=\"theJoin\" />\n" +
                "\n" +
                "        <parallelGateway id=\"nestedFork\" />\n" +
                "        <sequenceFlow sourceRef=\"nestedFork\" targetRef=\"userTask04\" />\n" +
                "        <sequenceFlow sourceRef=\"nestedFork\" targetRef=\"userTask05\" />\n" +
                "        <sequenceFlow sourceRef=\"nestedFork\" targetRef=\"userTask06\" />\n" +
                "        <sequenceFlow sourceRef=\"nestedFork\" targetRef=\"userTask07\" />\n" +
                "        <sequenceFlow sourceRef=\"nestedFork\" targetRef=\"userTask08\" />\n" +
                "\n" +
                "        <userTask id=\"userTask04\" name=\"User Task 04\" flowable:assignee=\"${assignee04}\" />\n" +
                "        <sequenceFlow sourceRef=\"userTask04\" targetRef=\"nestedJoin\" />\n" +
                "\n" +
                "        <userTask id=\"userTask05\" name=\"User Task 05\" flowable:assignee=\"${assignee05}\" />\n" +
                "        <sequenceFlow sourceRef=\"userTask05\" targetRef=\"nestedJoin\" />\n" +
                "\n" +
                "        <userTask id=\"userTask06\" name=\"User Task 06\" flowable:assignee=\"${assignee06}\" />\n" +
                "        <sequenceFlow sourceRef=\"userTask06\" targetRef=\"nestedJoin\" />\n" +
                "\n" +
                "        <userTask id=\"userTask07\" name=\"User Task 07\" flowable:assignee=\"${assignee07}\" />\n" +
                "        <sequenceFlow sourceRef=\"userTask07\" targetRef=\"nestedJoin\" />\n" +
                "\n" +
                "        <userTask id=\"userTask08\" name=\"User Task 08\" flowable:assignee=\"${assignee08}\" />\n" +
                "        <sequenceFlow sourceRef=\"userTask08\" targetRef=\"nestedJoin\" />\n" +
                "\n" +
                "        <parallelGateway id=\"nestedJoin\" />\n" +
                "        <sequenceFlow sourceRef=\"nestedJoin\" targetRef=\"userTask09\" />\n" +
                "\n" +
                "        <userTask id=\"userTask09\" name=\"User Task 09\" flowable:assignee=\"${assignee09}\" />\n" +
                "        <sequenceFlow sourceRef=\"userTask09\" targetRef=\"theJoin\" />\n" +
                "\n" +
                "        <parallelGateway id=\"theJoin\" />\n" +
                "        <sequenceFlow sourceRef=\"theJoin\" targetRef=\"serviceTask04\" />\n" +
                "\n" +
                "        <serviceTask id=\"serviceTask04\" flowable:class=\"org.flowable.delegate.DummyServiceTask\" />\n" +
                "        <sequenceFlow sourceRef=\"serviceTask04\" targetRef=\"serviceTask05\" />\n" +
                "\n" +
                "        <serviceTask id=\"serviceTask05\" flowable:class=\"org.flowable.delegate.DummyServiceTask\" />\n" +
                "        <sequenceFlow sourceRef=\"serviceTask05\" targetRef=\"serviceTask06\" />\n" +
                "\n" +
                "        <serviceTask id=\"serviceTask06\" flowable:class=\"org.flowable.delegate.DummyServiceTask\" />\n" +
                "        <sequenceFlow sourceRef=\"serviceTask06\" targetRef=\"theEnd\" />\n" +
                "\n" +
                "        <endEvent id=\"theEnd\" name=\"End\" />\n" +
                "\n" +
                "    </process>\n" +
                "\n" +
                "</definitions>\n");
    }
}