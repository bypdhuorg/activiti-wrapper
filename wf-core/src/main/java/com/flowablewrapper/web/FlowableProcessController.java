package com.flowablewrapper.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowablewrapper.Pager;
import com.flowablewrapper.bean.vo.FlowModelVO;
import com.flowablewrapper.bean.vo.FlowProcessDefinitionPageVO;
import com.flowablewrapper.services.flowableable.WfModelService;
import com.flowablewrapper.services.flowableable.WfProcessDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflow/process")
public class FlowableProcessController {
    @Autowired
    private WfProcessDefinitionService wfProcessDefinitionService;

    @Autowired
    private WfModelService wfModelService;

    @Autowired
    private ObjectMapper objectMapper;

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
    public Pager getList(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "category",required = false) String category,

            @RequestParam(value = "pagenum") Integer pageNum,
            @RequestParam(value = "pagesize") Integer pageSize,
            @RequestParam(value = "sort") String sort
    ) throws Exception {
        FlowProcessDefinitionPageVO inVo=new FlowProcessDefinitionPageVO();
        inVo.setPage(pageNum-1);
        inVo.setPageSize(pageSize);
        inVo.setCategory(category);
        inVo.setKey(key);
        inVo.setName(name);
        inVo.handleSort(sort);

        return wfProcessDefinitionService.processDefinitions(inVo);
    }

    /**
     * 发布流程
     * @param modelId @PathVariable String modelId
     * @throws Exception
     */
    @RequestMapping(value = "/deploy/{modelId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void deployByModelId(@PathVariable String modelId) throws Exception {

        FlowModelVO updateModel=wfModelService.getModelJson(modelId);

        wfProcessDefinitionService.deployByModelId(updateModel);
    }



}
