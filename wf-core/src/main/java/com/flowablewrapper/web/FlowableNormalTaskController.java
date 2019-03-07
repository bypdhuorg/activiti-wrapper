package com.flowablewrapper.web;

import com.flowablewrapper.Pager;
import com.flowablewrapper.bean.vo.FlowRunningTaskPageVO;
import com.flowablewrapper.bean.vo.FlowStartTaskVO;
import com.flowablewrapper.services.flowableable.WfModelService;
import com.flowablewrapper.services.flowableable.WfProcessDefinitionService;
import com.flowablewrapper.services.flowableable.WfProcessTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/workflow/task")
public class FlowableNormalTaskController {
    @Autowired
    private WfProcessDefinitionService wfProcessDefinitionService;

    @Autowired
    private WfModelService wfModelService;

    @Autowired
    private WfProcessTaskService wfProcessTaskService;


    /**
     * 获取列表
     * @param buId
     * @param userId
     * @param taskName
     * @param assigned
     * @param definitionKey
     * @param processInstanceId
     * @param pageNum
     * @param pageSize
     * @param sort
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Pager getList(
            @RequestParam(value = "buId",required = false) String buId,
            @RequestParam(value = "userId",required = false) String userId,
            @RequestParam(value = "taskName",required = false) String taskName,
            @RequestParam(value = "assigned",required = false) String assigned,
            @RequestParam(value = "definitionKey",required = false) String definitionKey,
            @RequestParam(value = "processInstanceId",required = false) String processInstanceId,

            @RequestParam(value = "pagenum") Integer pageNum,
            @RequestParam(value = "pagesize") Integer pageSize,
            @RequestParam(value = "sort") String sort
    ) throws Exception {
        FlowRunningTaskPageVO inVo=new FlowRunningTaskPageVO();
        inVo.setPage(pageNum-1);
        inVo.setPageSize(pageSize);
        inVo.handleSort(sort);

        inVo.setBuId(buId);
        inVo.setUserId(userId);
        inVo.setTaskName(taskName);
        inVo.setAssigned(assigned);
        inVo.setDefinitionKey(definitionKey);
        inVo.setProcessInstanceId(processInstanceId);

        return wfProcessTaskService.runningTasks(inVo);
    }

    /**
     * 启动流程 (申请)
     * @param invo
     * @throws Exception
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void start(@RequestBody FlowStartTaskVO invo) throws Exception {

        wfProcessTaskService.start(invo);
    }

    /**
     * 转办
     * @param taskId
     * @param userId
     * @throws Exception
     */
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void transfer(
            @RequestParam(value = "taskId") String taskId,
            @RequestParam(value = "userId",required = false) String userId
    ) throws Exception {

        wfProcessTaskService.transfer(taskId,userId);
    }

    /**
     * 签收
     * @param taskId
     * @param userId
     * @throws Exception
     */
    @RequestMapping(value = "/claim", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void claim(
            @RequestParam(value = "taskId") String taskId,
            @RequestParam(value = "userId",required = false) String userId
    ) throws Exception {

        wfProcessTaskService.claim(taskId,userId);
    }
    /**
     * 签收
     * @param taskId
     * @param variables
     * @throws Exception
     */
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void complete(
            @RequestParam(value = "taskId") String taskId,
            @RequestBody Map<String, Object> variables
    ) throws Exception {
        wfProcessTaskService.complete(taskId,variables);
    }


}
