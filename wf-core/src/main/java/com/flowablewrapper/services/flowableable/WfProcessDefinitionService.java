package com.flowablewrapper.services.flowableable;

import com.flowablewrapper.Pager;
import com.flowablewrapper.bean.SortDirection;
import com.flowablewrapper.bean.dto.ProcessDefinitionBean;
import com.flowablewrapper.bean.vo.FlowModelVO;
import com.flowablewrapper.bean.vo.FlowProcessDefinitionPageVO;
import liquibase.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.bpmn.deployer.ResourceNameUtil;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@EnableAutoConfiguration
@Service
@Slf4j
public class WfProcessDefinitionService {

    private ProcessEngine processEngine;

    @Autowired
    public WfProcessDefinitionService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    /**
     * 按照模块Id部署
     * @param inVo
     */
    public void deployByModelId(FlowModelVO inVo){
       this.deploy(
               inVo.getCategory(),
               inVo.getName(),
               inVo.getKey(),
               inVo.getObjectNode().toString()
       );
    }

    /**
     * 部署流程
     *
     * @param category     类别
     * @param processName  流程名称
     * @param resourceName 流程图资源名称
     * @param info         流程图内容
     */
    public void deploy(String category, String processName, String resourceName, String info) {
        //check if exist
//        if(!this.checkDefinitionKeyExisted(processKey)){
        log.debug("checkDefinitionKeyExisted");
        resourceName = changeToBpmnResource(resourceName);
        Deployment deployment = processEngine.getRepositoryService().createDeployment()
                .name(processName)
                .category(category)
                .addString(resourceName, info)
                .deploy();
//        }
    }

    /**
     * 挂起某一流程所有版本 (by key)
     *
     * @param processKey
     */
    public void suspendByProcessKey(String processKey) {
        //check if exist
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.suspendProcessDefinitionByKey(processKey);
    }

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
     * 激活某一流程所有版本 (by key)
     *
     * @param processKey
     */
    public void resumeByProcessKey(String processKey) {
        //check if exist
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.activateProcessDefinitionByKey(processKey);
    }


    /**
     * 激活某一特定版本流程(by id )
     *
     * @param id
     */
    public void resumeById(String id) {
        //check if exist
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.activateProcessDefinitionById(id);
    }

    /**
     * 取得流程分页
     *
     * @return
     */
    public Pager<ProcessDefinitionBean> processDefinitions(FlowProcessDefinitionPageVO inVo) {

        RepositoryService repositoryService = processEngine.getRepositoryService();

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //组织条件
        if (StringUtils.isNotEmpty(inVo.getName())) {
//            deploymentQuery.deploymentNameLike(inVo.getName());
            processDefinitionQuery.processDefinitionNameLike(inVo.getName());

        }

        if (StringUtils.isNotEmpty(inVo.getCategory())) {
//            deploymentQuery.deploymentNameLike(inVo.getCategory());
            processDefinitionQuery.processDefinitionCategoryLike(inVo.getCategory());
        }



        //取得总条数
//        int total = Math.toIntExact(deploymentQuery.count());
        int total = Math.toIntExact(processDefinitionQuery.count());

        //设置排序字段
        switch (inVo.getSortby().toLowerCase()) {
            case "id":
//                deploymentQuery.orderByDeploymentId();
                processDefinitionQuery.orderByDeploymentId();
                break;
            case "name":
//                deploymentQuery.orderByDeploymentName();
                processDefinitionQuery.orderByProcessDefinitionName();
                break;
            case "deployTime":
//                deploymentQuery.orderByDeploymenTime();
                processDefinitionQuery.orderByProcessDefinitionName();
                break;
            default:
//                deploymentQuery.orderByDeploymenTime();
                processDefinitionQuery.orderByProcessDefinitionName();
        }

        //设置排序方向
        if (SortDirection.asc.equals(inVo.getDirection())) {
            processDefinitionQuery.asc();
        } else {
            processDefinitionQuery.desc();
        }

        List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage(inVo.getPage(), inVo.getPageSize());

        //后处理
        List<ProcessDefinitionBean> returnList = new ArrayList<>();
        for (ProcessDefinition deployment : processDefinitions) {
//            ProcessDefinition processDefinition = this.getProcessDefinitionByDeploymentId(deployment.getId());
//            if (processDefinition != null) {
                ProcessDefinitionBean tempBean = new ProcessDefinitionBean();
                //流程类别
                tempBean.setCategory(deployment.getCategory());
                //流程名称
                tempBean.setName(deployment.getName());
                //部署的ID
                tempBean.setDeploymentId(deployment.getDeploymentId());
//                tempBean.setDeployTime(deployment.getDeploymentTime().getTime());

                //流程Key
                tempBean.setKey(deployment.getKey());
                //版本
                tempBean.setVersion(deployment.getVersion());
                //部署时间
                tempBean.setProcessDefinitionId(deployment.getId());
                returnList.add(tempBean);
//            }

        }
        return new Pager<ProcessDefinitionBean>(total, returnList);

    }

//    /**
//     * 取得流程 实例
//     *
//     * @param deploymentId DefinitionId
//     * @return
//     */
//    private ProcessDefinitionBean getProcessDefinitionByDeploymentId(String deploymentId) {
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
//        ProcessDefinition pd= processDefinitionQuery.deploymentId(deploymentId).singleResult();
//
//        if(pd!=null){
//            ProcessDefinitionBean processDefinitionBean=new ProcessDefinitionBean();
//            processDefinitionBean.setProcessDefinitionId(pd.getId());
//            processDefinitionBean.setCategory(pd.getCategory());
//
//            processDefinitionBean.setName(pd.getName());
//            processDefinitionBean.setDeploymentId(pd.getDeploymentId());
//            processDefinitionBean.setIsSuspended(pd.isSuspended());
//            processDefinitionBean.setKey(pd.getKey());
//            processDefinitionBean.setResourceName(pd.getResourceName());
//            return processDefinitionBean;
//        }
//
//        return null;
//    }

    /**
     * 取得流程 实例
     *
     * @param processDefinitionId
     * @return
     */
    public ProcessDefinitionBean getProcessDefinitionDetail(String processDefinitionId) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition pd = repositoryService.getProcessDefinition(processDefinitionId);
        if(pd!=null){
            ProcessDefinitionBean processDefinitionBean=new ProcessDefinitionBean();
            processDefinitionBean.setProcessDefinitionId(pd.getId());
            processDefinitionBean.setCategory(pd.getCategory());

            processDefinitionBean.setName(pd.getName());
            processDefinitionBean.setDeploymentId(pd.getDeploymentId());
            processDefinitionBean.setIsSuspended(pd.isSuspended());
            processDefinitionBean.setKey(pd.getKey());
            processDefinitionBean.setResourceName(pd.getResourceName());
            return processDefinitionBean;
        }

        return null;
    }


    /**
     * 判断是否processKey 是否存在
     *
     * @param processKey
     * @return
     */
    private boolean checkDefinitionKeyExisted(String processKey) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        long count = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .count();
        return count > 0;
    }

    private String changeToBpmnResource(String resourceName) {
        for (String suffix : ResourceNameUtil.BPMN_RESOURCE_SUFFIXES) {
            if (resourceName.endsWith(suffix)) {
                return resourceName;
            }
        }

        return resourceName + ResourceNameUtil.BPMN_RESOURCE_SUFFIXES[0];
    }

}
