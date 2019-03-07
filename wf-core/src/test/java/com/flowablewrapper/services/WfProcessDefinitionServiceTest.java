package com.flowablewrapper.services;

import com.flowablewrapper.Pager;
import com.flowablewrapper.bean.SortDirection;
import com.flowablewrapper.bean.dto.ProcessDefinitionBean;
import com.flowablewrapper.bean.vo.FlowProcessDefinitionPageVO;
import com.flowablewrapper.services.flowableable.WfProcessDefinitionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class WfProcessDefinitionServiceTest {

    @Autowired
    private WfProcessDefinitionService processService;

    @Test
    public void deploy() {
    }

    @Test
    public void processDefinitions() {
        FlowProcessDefinitionPageVO inVo=new FlowProcessDefinitionPageVO();
        inVo.setPage(0);
        inVo.setPageSize(100);
        inVo.setSortby("id");
        inVo.setDirection(SortDirection.asc);
        Pager<ProcessDefinitionBean> list =processService.processDefinitions(inVo);
//        System.out.println(list);

        System.out.println("总条数"+list.getTotal());
        for(ProcessDefinitionBean task : list.getContent()){
            System.out.println("getProcessDefinitionId：" + task.getProcessDefinitionId());
            System.out.println("getCategory：" + task.getCategory());
            System.out.println("getName：" + task.getName());
            System.out.println("getVersion：" + task.getVersion());
            System.out.println("getDeploymentId：" + task.getDeploymentId());
            System.out.println("getKey：" + task.getKey());
            System.out.println("getDeployTime：" + task.getDeployTime());
            System.out.println("####################：" );
        }
    }

    @Test
    public void getProcessDefinitionDetail() {
    }
}