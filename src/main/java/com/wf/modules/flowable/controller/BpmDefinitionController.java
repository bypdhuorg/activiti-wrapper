package com.wf.modules.flowable.controller;

import com.wf.base.BaseController;
import com.wf.bean.vo.PageVo;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.dto.BaseDTO;
import com.wf.common.utils.ResultUtil;
import com.wf.modules.flowable.entity.BpmDefinition;
import com.wf.modules.flowable.service.BpmDefinitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * 流程定义Controller层
 * @author meibo
 */
@Slf4j
@RestController
@Api(description = "流程定义管理接口")
@RequestMapping("/bpmDefinition")
@Transactional
public class BpmDefinitionController extends BaseController<BpmDefinition, String>{

    @Autowired
    private BpmDefinitionService bpmDefinitionService;

    @Override
    public BpmDefinitionService getService() {
        return bpmDefinitionService;
    }

    @RequestMapping(value = "/getByPage",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "分页获取")
    public BaseDTO<Page<BpmDefinition>> getByPage(
            @ModelAttribute BpmDefinition bpmDefinition,
            @ModelAttribute SearchTimeVo searchVo,
            @ModelAttribute PageVo page){

        Page<BpmDefinition> data = getService().findByCondition(bpmDefinition,searchVo,initPage(page));
        return new ResultUtil<Page<BpmDefinition>>().setData(data);
    }


    /**
     * suspend
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/suspend/{id}", method = RequestMethod.PUT)
    public BpmDefinition suspend(@PathVariable String id) throws Exception {
        return getService().suspend(id);
    }


    /**
     * active
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/active/{id}", method = RequestMethod.PUT)
    public BpmDefinition active(@PathVariable String id) throws Exception {
        return getService().active(id);
    }
}
