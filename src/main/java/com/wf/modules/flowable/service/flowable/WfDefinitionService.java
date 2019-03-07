package com.wf.modules.flowable.service.flowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

@EnableAutoConfiguration
@Service
@Slf4j
public class WfDefinitionService {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    /**
     * 挂起某一特定版本流程(by id )
     *
     * @param id
     */
    public void suspendById(String id) {
        //check if exist
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.suspendProcessDefinitionById(id);
    }

    /**
     * 激活某一特定版本流程(by id )
     *
     * @param id
     */
    public void activeById(String id) {
        //check if exist
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.activateProcessDefinitionById(id);
    }
}
