package com.wf.modules.flowable.controller;

import com.wf.base.BaseController;
import com.wf.bean.vo.PageVo;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.dto.BaseDTO;
import com.wf.common.utils.ResultUtil;
import com.wf.modules.flowable.entity.BpmInstance;
import com.wf.modules.flowable.service.BpmInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * 流程实例Controller层
 * @author meibo
 */
@Slf4j
@RestController
@Api(description = "流程实例管理接口")
@RequestMapping("/bpmInstance")
@Transactional
public class BpmInstanceController extends BaseController<BpmInstance, String>{

    @Autowired
    private BpmInstanceService bpmInstanceService;

    @Override
    public BpmInstanceService getService() {
        return bpmInstanceService;
    }

    @RequestMapping(value = "/getByPage",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "分页获取")
    public BaseDTO<Page<BpmInstance>> getByPage(
            @ModelAttribute BpmInstance bpmInstance,
            @ModelAttribute SearchTimeVo searchVo,
            @ModelAttribute PageVo page){

        Page<BpmInstance> data = getService().findByCondition(bpmInstance,searchVo,initPage(page));
        return new ResultUtil<Page<BpmInstance>>().setData(data);
    }


    /**
     * 启动流程
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/start", method = RequestMethod.PUT)
    public BpmInstance start(@RequestBody BpmInstance bpmInstance) throws Exception {
        return getService().saveAndStart(bpmInstance);
    }

    /**
     * suspend
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/suspend/{id}", method = RequestMethod.PUT)
    public BpmInstance suspend(@PathVariable String id) throws Exception {
        return getService().suspend(id);
    }


    /**
     * active
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/active/{id}", method = RequestMethod.PUT)
    public BpmInstance active(@PathVariable String id) throws Exception {
        return getService().active(id);
    }
}
