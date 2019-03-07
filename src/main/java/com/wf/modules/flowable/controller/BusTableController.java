package com.wf.modules.flowable.controller;

import com.wf.base.BaseController;
import com.wf.bean.vo.PageVo;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.dto.BaseDTO;
import com.wf.common.utils.ResultUtil;
import com.wf.modules.flowable.entity.BusTable;
import com.wf.modules.flowable.service.BusTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


/**
 * 业务表定义Controller层
 * @author meibo
 */
@Slf4j
@RestController
@Api(description = "业务表定义管理接口")
@RequestMapping("/busTable")
@Transactional
public class BusTableController extends BaseController<BusTable, String>{

    @Autowired
    private BusTableService busTableService;

    @Override
    public BusTableService getService() {
        return busTableService;
    }

    @RequestMapping(value = "/getByPage",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "分页获取")
    public BaseDTO<Page<BusTable>> getByPage(
            @ModelAttribute BusTable busTable,
            @ModelAttribute SearchTimeVo searchVo,
            @ModelAttribute PageVo page){

        Page<BusTable> data = getService().findByCondition(busTable,searchVo,initPage(page));
        return new ResultUtil<Page<BusTable>>().setData(data);
    }
}
