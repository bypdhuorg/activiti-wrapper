package com.flowablewrapper.services;

import com.flowablewrapper.Pager;
import com.flowablewrapper.bean.SortDirection;
import com.flowablewrapper.bean.dto.FlowTaskListBean;
import com.flowablewrapper.bean.vo.FlowRunningTaskPageVO;
import com.flowablewrapper.bean.vo.FlowStartTaskVO;
import com.flowablewrapper.services.flowableable.WfProcessDefinitionService;
import com.flowablewrapper.services.flowableable.WfProcessTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class WfProcessTaskServiceTest {

    @Autowired
    private WfProcessDefinitionService processService;


    @Autowired
    private WfProcessTaskService taskService;

    @Test
    public void start() {

        FlowStartTaskVO vo=new FlowStartTaskVO();
        vo.setBuId("bu1");
        vo.setProcessParams("bu1");
        vo.setTitle("tddd");
        vo.setUserId("test2");
        vo.setUserName("test2d中文");
        vo.setProcessDefinitionKey("testProcessBB");
        taskService.start(vo);
    }

    @Test
    public void claim() {
    }

    @Test
    public void transfer() {
    }

    @Test
    public void complete() {

        taskService.complete("b15108b8-0ff6-11e9-964b-2c6e855d3d37",null);
    }



    @Test
    public void runningTasks() {

        FlowRunningTaskPageVO inVo=new FlowRunningTaskPageVO();
        inVo.setPage(0);
        inVo.setPageSize(10);
        inVo.setSortby("id");
        inVo.setDirection(SortDirection.asc);
        Pager<FlowTaskListBean> listbean= taskService.runningTasks(inVo);

        System.out.println("总条数"+listbean.getTotal());
        for(FlowTaskListBean task : listbean.getContent()){
            System.out.println("############################");
            System.out.println("任务ID：" + task.getId());
            System.out.println("title：" + task.getTitle());
            System.out.println("任务名称：" + task.getTaskName());
            System.out.println("任务时间：" + task.getStartTime());
            System.out.println("任务的办理人：" + task.getAssignee());
            System.out.println("流程的定义ID：" + task.getProcessInstanceId());
            System.out.println("执行对象的ID：" + task.getExecutionId());

        }
    }
}