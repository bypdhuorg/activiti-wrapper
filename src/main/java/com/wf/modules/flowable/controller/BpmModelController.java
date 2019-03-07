package com.wf.modules.flowable.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wf.base.BaseController;
import com.wf.bean.vo.PageVo;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.dto.BaseDTO;
import com.wf.common.utils.ResultUtil;
import com.wf.modules.flowable.entity.BpmModel;
import com.wf.modules.flowable.service.BpmModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * 模型定义Controller层
 * @author meibo
 */
@Slf4j
@RestController
@Api(description = "模型定义管理接口")
@RequestMapping("/bpmModel")
@Transactional
public class BpmModelController extends BaseController<BpmModel, String>{

    @Autowired
    private BpmModelService bpmModelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public BpmModelService getService() {
        return bpmModelService;
    }

    @RequestMapping(value = "/getByPage",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "分页获取")
    public BaseDTO<Page<BpmModel>> getByPage(
            @ModelAttribute BpmModel bpmModel,
            @ModelAttribute SearchTimeVo searchVo,
            @ModelAttribute PageVo page){

        Page<BpmModel> data = getService().findByCondition(bpmModel,searchVo,initPage(page));
        return new ResultUtil<Page<BpmModel>>().setData(data);
    }


    /**
     * 获取图stencil
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/stencil-sets/editor", method = RequestMethod.GET)
    public JsonNode getStencilSetForEditor() throws IOException {
        JsonNode stencilNode = objectMapper.readTree(this.getClass().getClassLoader().getResourceAsStream("stencilset_bpmn.json"));
        return stencilNode;
    }

    /**
     * 获取流程图
     * @param actmodelId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/actmodel/{actmodelId}", method = RequestMethod.GET)
    public ObjectNode getEditorJson(@PathVariable String actmodelId) throws Exception {
        return getService().getByActModelId(actmodelId).getObjectNode();
    }

    /**
     * 获取流程图
     * @param modelId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/thumbnail/{modelId}", method = RequestMethod.GET)
    public byte[] getModelThumbnail(@PathVariable String modelId) throws Exception {
        return getService().getModelThumbnail(modelId);
    }


    /**
     * 获取流程图
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deploy/{id}", method = RequestMethod.PUT)
    public BpmModel deployById(@PathVariable String id) throws Exception {
        return getService().deployModel(id);
    }
}
