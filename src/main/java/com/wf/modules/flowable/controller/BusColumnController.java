package com.wf.modules.flowable.controller;

import com.wf.base.BaseController;
import com.wf.bean.vo.PageVo;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.dto.BaseDTO;
import com.wf.common.utils.ResultUtil;
import com.wf.modules.flowable.entity.BusColumn;
import com.wf.modules.flowable.service.BusColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


/**
 * 业务字段表Controller层
 * @author meibo
 */
@Slf4j
@RestController
@Api(description = "业务字段表管理接口")
@RequestMapping("/busColumn")
@Transactional
public class BusColumnController extends BaseController<BusColumn, String>{

    @Autowired
    private BusColumnService busColumnService;

    @Override
    public BusColumnService getService() {
        return busColumnService;
    }

    @RequestMapping(value = "/getByPage",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "分页获取")
    public BaseDTO<Page<BusColumn>> getByPage(
            @ModelAttribute BusColumn busColumn,
            @ModelAttribute SearchTimeVo searchVo,
            @ModelAttribute PageVo page){

        Page<BusColumn> data = getService().findByCondition(busColumn,searchVo,initPage(page));
        return new ResultUtil<Page<BusColumn>>().setData(data);
    }
}
