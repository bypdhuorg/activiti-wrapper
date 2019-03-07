package com.flowablewrapper.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowablewrapper.Pager;
import com.flowablewrapper.bean.db.BpmDefinition;
import com.flowablewrapper.bean.vo.FlowModelPageVO;
import com.flowablewrapper.bean.vo.FlowModelVO;
import com.flowablewrapper.services.flowableable.WfModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/workflow/model")
public class FlowableModelController {
    @Autowired
    private WfModelService wfModelService;

    @Autowired
    private ObjectMapper objectMapper;

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
     * @param modelId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{modelId}", method = RequestMethod.GET)
    public ObjectNode getEditorJson(@PathVariable String modelId) throws Exception {
        return wfModelService.getModelJson(modelId).getObjectNode();
    }

    /**
     * 获取流程图
     * @param modelId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/thumbnail/{modelId}", method = RequestMethod.GET)
    public byte[] getModelThumbnail(@PathVariable String modelId) throws Exception {
        return wfModelService.getModelThumbnail(modelId);
    }

    /**
     * 获取列表
     * @param name
     * @param key
     * @param category
     * @param pageNum
     * @param pageSize
     * @param sort
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Pager getModelList(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "category",required = false) String category,

            @RequestParam(value = "pagenum") Integer pageNum,
            @RequestParam(value = "pagesize") Integer pageSize,
            @RequestParam(value = "sort") String sort
    ) throws Exception {
        FlowModelPageVO inVo=new FlowModelPageVO();
        inVo.setPage(pageNum-1);
        inVo.setPageSize(pageSize);
        inVo.setCategory(category);
        inVo.setKey(key);
        inVo.setName(name);
        inVo.handleSort(sort);

        return wfModelService.models(inVo);
    }

    /**
     * 创建
     * @param updateModel
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void createModel(@RequestBody BpmDefinition updateModel) throws Exception {
        wfModelService.createModel(updateModel);
    }

    /**
     * 更新
     * @param modelId
     * @param updateModel
     * @throws Exception
     */
    @RequestMapping(value = "/{modelId}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateModel(@PathVariable String modelId,@RequestBody FlowModelVO updateModel) throws Exception {
        updateModel.setId(modelId);
        wfModelService.updateModel(updateModel);
    }


    @RequestMapping(value = "/{modelId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteModel(@PathVariable String modelId) throws Exception {
        wfModelService.delete(modelId);
    }

}
