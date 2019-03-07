package com.wf.modules.flowable.controller;

import com.wf.base.BaseController;
import com.wf.bean.vo.PageVo;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.dto.BaseDTO;
import com.wf.common.utils.ResultUtil;
import com.wf.modules.flowable.entity.BpmTask;
import com.wf.modules.flowable.service.BpmTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


/**
 * 流程任务Controller层
 * @author meibo
 */
@Slf4j
@RestController
@Api(description = "流程任务管理接口")
@RequestMapping("/bpmTask")
@Transactional
public class BpmTaskController extends BaseController<BpmTask, String>{

    @Autowired
    private BpmTaskService bpmTaskService;

    @Override
    public BpmTaskService getService() {
        return bpmTaskService;
    }

    @RequestMapping(value = "/getByPage",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "分页获取")
    public BaseDTO<Page<BpmTask>> getByPage(
            @ModelAttribute BpmTask bpmTask,
            @ModelAttribute SearchTimeVo searchVo,
            @ModelAttribute PageVo page){

        Page<BpmTask> data = getService().findByCondition(bpmTask,searchVo,initPage(page));
        return new ResultUtil<Page<BpmTask>>().setData(data);
    }
}
