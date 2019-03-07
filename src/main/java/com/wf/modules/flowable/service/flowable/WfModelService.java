package com.wf.modules.flowable.service.flowable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wf.common.exception.UserClientException;
import com.wf.common.utils.ImageGenerator;
import com.wf.modules.flowable.entity.BpmDefinition;
import com.wf.modules.flowable.entity.BpmModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.editor.constants.EditorJsonConstants;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.constants.StencilConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EnableAutoConfiguration
@Service
@Slf4j
public class WfModelService {

    private final static String MODEL_ID          = "modelId";
    private final static String MODEL_NAME        = "name";
    private final static String MODEL_REVISION    = "revision";
    private final static String MODEL_DESCRIPTION = "description";
    private final static String MODEL_KEY         = "key";

    private static final String BPMN_RESOURCE_SUFFIXES = "bpmn20.xml";

    private static float THUMBNAIL_WIDTH = 300f;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;



    /**
     * 校验
     * @param body
     * @return
     */
    public List<ValidationError> validate(JsonNode body) {
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(body);
        ProcessValidator validator = new ProcessValidatorFactory().createDefaultProcessValidator();
        List<ValidationError> errors = validator.validate(bpmnModel);
        return errors;
    }

    /**
     * 获取流程图
     * @param actModId
     * @return
     * @throws Exception
     */
    public byte[] getModelThumbnail(String actModId)  {
        try {
            BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
            ObjectNode editorJsonNode =(ObjectNode)this.getModelJson(actModId).getObjectNode().get("model");
            BpmnModel bpmnModel = bpmnJsonConverter.convertToBpmnModel(editorJsonNode);

            double scaleFactor = 1.0;
            GraphicInfo diagramInfo = calculateDiagramSize(bpmnModel);
            if (diagramInfo.getWidth() > THUMBNAIL_WIDTH) {
                scaleFactor = diagramInfo.getWidth() / THUMBNAIL_WIDTH;
                scaleDiagram(bpmnModel, scaleFactor);
            }

            BufferedImage modelImage = ImageGenerator.createImage(bpmnModel, scaleFactor);
            if (modelImage != null) {
                return ImageGenerator.createByteArrayForImage(modelImage, "png");
            }
        } catch (Exception e) {
            log.error("Error creating thumbnail image {}", actModId, e);
            throw new UserClientException("Error creating thumbnail image "+actModId,  e);
        }

        return null;

    }


//    /**
//     * 删除
//     * @param modelId
//     */
//    public void delete(String modelId){
//        Optional<BpmDefinition> bpmDefinitionOp=bpmDefinitionRepository.findById(modelId);
//
//        BpmDefinition bpmDefinition ;
//        if(bpmDefinitionOp.isPresent()){
//            bpmDefinition =bpmDefinitionOp.get();
//        }else{
//            return ;
//        }
//
//        //判断是否启动过流程
//        List<BpmInstance> bpmInstances =bpmInstanceRepository.findByDefId(bpmDefinition.getId());
//        if(bpmInstances.size()>0){
//            throw new BusinessException("该流程定义下存在流程实例，请勿删除！<br> 请清除数据后再来删除");
//        }
//
//        List<BpmDefinition>  bpmDefinitions =bpmDefinitionRepository.findByKey(bpmDefinition.getKey());
//        RepositoryService repositoryService=processEngine.getRepositoryService();
//
//        for (BpmDefinition bpmDefinitionUnit:bpmDefinitions){
//            //db delete
//            bpmDefinitionRepository.delete(bpmDefinitionUnit);
//
//            //flowable 处理
//            //有发布流程ID时候，删除ID
//            if(StringUtils.isNotEmpty(bpmDefinitionUnit.getActDeployId())){
//                repositoryService.deleteDeployment(bpmDefinitionUnit.getActDeployId());
//            }
//
//            //有发布ModelID时候，删除Model
//            if(StringUtils.isNotEmpty(bpmDefinitionUnit.getActModelId())){
//                repositoryService.deleteModel(bpmDefinitionUnit.getActModelId());
//            }
//        }
//    }

    /**
     *取得Mode的Json数据
     * @param modelId
     * @return
     * @throws Exception
     */
    public BpmModel getModelJson(String modelId) {
        RepositoryService repositoryService=processEngine.getRepositoryService();
        Model model = repositoryService.getModel(modelId);

        if (model == null){ throw new NullPointerException("模型不存在");}

        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(MODEL_ID, model.getId());
        modelNode.put(MODEL_NAME, model.getName());
        modelNode.put("category", model.getCategory());
        modelNode.put("key", model.getKey());

        try {
            String tmpDes = "";
            if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                String jsonMetaInfo = new String(repositoryService.getModelEditorSource(model.getId()));
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(jsonMetaInfo);
                editorJsonNode.put("modelType", "model");
                modelNode.set("model", editorJsonNode);
                if (editorJsonNode.get(StencilConstants.PROPERTY_DOCUMENTATION) != null) {
                    tmpDes = editorJsonNode.get(StencilConstants.PROPERTY_DOCUMENTATION).toString();
                }
                modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, tmpDes);
            } else {
                ObjectNode editorJsonNode = objectMapper.createObjectNode();
                editorJsonNode.put("id", "canvas");
                editorJsonNode.put("resourceId", "canvas");
                ObjectNode stencilSetNode = objectMapper.createObjectNode();
                stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
                editorJsonNode.put("modelType", "model");
                modelNode.set("model", editorJsonNode);
            }
            BpmModel vo=new BpmModel();
            vo.setCategory(model.getCategory());
            vo.setName(model.getName());
            vo.setActModelId(model.getId());
            vo.setKey(model.getKey());
            vo.setObjectNode(modelNode);
            vo.setVersion(model.getVersion());
            vo.setDescription(tmpDes);
            return vo;
        }

        catch (java.io.IOException e) {
            throw new UserClientException("取得flowable模块定义失败！", e);
        }
    }

    /**
     * 在Flowerable 中创建一个模块
     * @param bpmModel
     * @return
     */
    public void createModelToFlowable(BpmModel bpmModel){

        try {
            RepositoryService repositoryService=processEngine.getRepositoryService();

            Model modelData = repositoryService.newModel();

            modelData.setKey(bpmModel.getKey());
            modelData.setName(bpmModel.getName());
            modelData.setCategory(bpmModel.getCategory());

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, bpmModel.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, bpmModel.getDescription());
            modelData.setMetaInfo(modelObjectNode.toString());

            //1.save model
            repositoryService.saveModel(modelData);

            //2.
            String jsonMetaInfo = this.createModelJson(bpmModel);
            repositoryService.addModelEditorSource(modelData.getId(), jsonMetaInfo.getBytes("utf-8"));

            //设置 flowableID 到数据库对应字段
            bpmModel.setActModelId(modelData.getId());
        } catch (UnsupportedEncodingException e) {
            throw new UserClientException("创建flowable流程定义失败！", e);
        }
    }

    /**
     * 部署流程到flowable
     * @param bpmModel
     */
    public BpmDefinition deployFlowableModel(BpmModel bpmModel){
        try{
            //1.get flowable
            RepositoryService repositoryService=processEngine.getRepositoryService();
            Model modelData  = repositoryService.getModel(bpmModel.getActModelId());
            ObjectNode modelNode = (ObjectNode) new ObjectMapper()
                    .readTree(repositoryService.getModelEditorSource(modelData.getId()));

            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

            String processName = bpmModel.getKey() +BPMN_RESOURCE_SUFFIXES;

            Deployment deployment = this.processEngine.getRepositoryService().createDeployment()
                    .name(bpmModel.getKey())
                    .addString(processName, new String(bpmnBytes))
                    .deploy();

            ProcessDefinition proDefinition = (ProcessDefinition)this.processEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            if (proDefinition == null){
                throw new UserClientException("发布flowable流程定义失败！");
            }
            BpmDefinition definition=new BpmDefinition();
            definition.setActDefId(proDefinition.getId());
            definition.setActDeployId(deployment.getId());
            definition.setActModelId(bpmModel.getActModelId());
            return definition;
    } catch (Exception e) {
        throw new UserClientException("发布flowable流程失败！", e);
    }
    }


    /**
     * 更新模块信息
     * @param bpmModel
     */
    public void updateFlowableModel(BpmModel bpmModel){
        RepositoryService repositoryService=processEngine.getRepositoryService();
        Model model = repositoryService.getModel(bpmModel.getActModelId());
        String json =bpmModel.getJsonXml();
        try {
            //1.flowable 更新
            byte[] graphBytes =json.getBytes("utf-8");
            repositoryService.addModelEditorSource(model.getId(), graphBytes);

            ObjectNode modelNode = (ObjectNode)new ObjectMapper().readTree(graphBytes);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

            ObjectNode modelObjectNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, bpmModel.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, bpmModel.getVersion());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, bpmModel.getDescription());

            model.setMetaInfo(modelObjectNode.toString());
            model.setKey(bpmModel.getKey());
            model.setName(bpmModel.getName());
            model.setCategory(bpmModel.getCategory());
            repositoryService.saveModel(model);

        }catch (Exception e) {
            throw new UserClientException("修改flowable流程定义失败！"+ bpmModel.getActModelId(), e);
          //  throw new Exception("Process model could not be saved " + model.getId()+";message:"+e.getMessage());
        }
    }


    /**
     * 新增Model json
     * @param model
     * @return
     */
    private String createModelJson(BpmModel model) {
        //type ==0
        String json = null;
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put(EditorJsonConstants.EDITOR_STENCIL_ID, "canvas");
        editorNode.put(EditorJsonConstants.EDITOR_SHAPE_ID, "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.set("stencilset", stencilSetNode);
        ObjectNode propertiesNode = objectMapper.createObjectNode();
        propertiesNode.put(StencilConstants.PROPERTY_PROCESS_ID, model.getKey());
        propertiesNode.put(StencilConstants.PROPERTY_NAME, model.getName());
        if (StringUtils.isNotEmpty(model.getDescription())) {
            propertiesNode.put(StencilConstants.PROPERTY_DOCUMENTATION, model.getDescription());
        }
        editorNode.set("properties", propertiesNode);

        ArrayNode childShapeArray = objectMapper.createArrayNode();
        editorNode.set(EditorJsonConstants.EDITOR_CHILD_SHAPES, childShapeArray);
        ObjectNode childNode = objectMapper.createObjectNode();
        childShapeArray.add(childNode);
        ObjectNode boundsNode = objectMapper.createObjectNode();
        childNode.set(EditorJsonConstants.EDITOR_BOUNDS, boundsNode);
        ObjectNode lowerRightNode = objectMapper.createObjectNode();
        boundsNode.set(EditorJsonConstants.EDITOR_BOUNDS_LOWER_RIGHT, lowerRightNode);
        lowerRightNode.put(EditorJsonConstants.EDITOR_BOUNDS_X, 130);
        lowerRightNode.put(EditorJsonConstants.EDITOR_BOUNDS_Y, 193);
        ObjectNode upperLeftNode = objectMapper.createObjectNode();
        boundsNode.set(EditorJsonConstants.EDITOR_BOUNDS_UPPER_LEFT, upperLeftNode);
        upperLeftNode.put(EditorJsonConstants.EDITOR_BOUNDS_X, 100);
        upperLeftNode.put(EditorJsonConstants.EDITOR_BOUNDS_Y, 163);
        childNode.set(EditorJsonConstants.EDITOR_CHILD_SHAPES, objectMapper.createArrayNode());
        childNode.set(EditorJsonConstants.EDITOR_DOCKERS, objectMapper.createArrayNode());
        childNode.set(EditorJsonConstants.EDITOR_OUTGOING, objectMapper.createArrayNode());
        childNode.put(EditorJsonConstants.EDITOR_SHAPE_ID, "startEvent1");
        ObjectNode stencilNode = objectMapper.createObjectNode();
        childNode.set(EditorJsonConstants.EDITOR_STENCIL, stencilNode);
        stencilNode.put(EditorJsonConstants.EDITOR_STENCIL_ID, "StartNoneEvent");
        json = editorNode.toString();

        return json;
    }

    /**
     * calculateDiagramSize
     * @param bpmnModel
     * @return
     */
    protected GraphicInfo calculateDiagramSize(BpmnModel bpmnModel) {
        GraphicInfo diagramInfo = new GraphicInfo();

        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            double elementMaxX = graphicInfo.getX() + graphicInfo.getWidth();
            double elementMaxY = graphicInfo.getY() + graphicInfo.getHeight();

            if (elementMaxX > diagramInfo.getWidth()) {
                diagramInfo.setWidth(elementMaxX);
            }
            if (elementMaxY > diagramInfo.getHeight()) {
                diagramInfo.setHeight(elementMaxY);
            }
        }

        for (Process process : bpmnModel.getProcesses()) {
            calculateWidthForFlowElements(process.getFlowElements(), bpmnModel, diagramInfo);
            calculateWidthForArtifacts(process.getArtifacts(), bpmnModel, diagramInfo);
        }
        return diagramInfo;
    }

    protected void calculateWidthForFlowElements(Collection<FlowElement> elementList, BpmnModel bpmnModel, GraphicInfo diagramInfo) {
        for (FlowElement flowElement : elementList) {
            List<GraphicInfo> graphicInfoList = new ArrayList<>();
            if (flowElement instanceof SequenceFlow) {
                List<GraphicInfo> flowGraphics = bpmnModel.getFlowLocationGraphicInfo(flowElement.getId());
                if (flowGraphics != null && flowGraphics.size() > 0) {
                    graphicInfoList.addAll(flowGraphics);
                }
            } else {
                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowElement.getId());
                if (graphicInfo != null) {
                    graphicInfoList.add(graphicInfo);
                }
            }

            processGraphicInfoList(graphicInfoList, diagramInfo);
        }
    }

    protected void calculateWidthForArtifacts(Collection<Artifact> artifactList, BpmnModel bpmnModel, GraphicInfo diagramInfo) {
        for (Artifact artifact : artifactList) {
            List<GraphicInfo> graphicInfoList = new ArrayList<>();
            if (artifact instanceof Association) {
                graphicInfoList.addAll(bpmnModel.getFlowLocationGraphicInfo(artifact.getId()));
            } else {
                graphicInfoList.add(bpmnModel.getGraphicInfo(artifact.getId()));
            }

            processGraphicInfoList(graphicInfoList, diagramInfo);
        }
    }

    protected void processGraphicInfoList(List<GraphicInfo> graphicInfoList, GraphicInfo diagramInfo) {
        for (GraphicInfo graphicInfo : graphicInfoList) {
            double elementMaxX = graphicInfo.getX() + graphicInfo.getWidth();
            double elementMaxY = graphicInfo.getY() + graphicInfo.getHeight();

            if (elementMaxX > diagramInfo.getWidth()) {
                diagramInfo.setWidth(elementMaxX);
            }
            if (elementMaxY > diagramInfo.getHeight()) {
                diagramInfo.setHeight(elementMaxY);
            }
        }
    }

    protected void scaleDiagram(BpmnModel bpmnModel, double scaleFactor) {
        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            scaleGraphicInfo(graphicInfo, scaleFactor);
        }

        for (Process process : bpmnModel.getProcesses()) {
            scaleFlowElements(process.getFlowElements(), bpmnModel, scaleFactor);
            scaleArtifacts(process.getArtifacts(), bpmnModel, scaleFactor);
            for (Lane lane : process.getLanes()) {
                scaleGraphicInfo(bpmnModel.getGraphicInfo(lane.getId()), scaleFactor);
            }
        }
    }

    protected void scaleGraphicInfoList(List<GraphicInfo> graphicInfoList, double scaleFactor) {
        for (GraphicInfo graphicInfo : graphicInfoList) {
            scaleGraphicInfo(graphicInfo, scaleFactor);
        }
    }

    protected void scaleGraphicInfo(GraphicInfo graphicInfo, double scaleFactor) {
        graphicInfo.setX(graphicInfo.getX() / scaleFactor);
        graphicInfo.setY(graphicInfo.getY() / scaleFactor);
        graphicInfo.setWidth(graphicInfo.getWidth() / scaleFactor);
        graphicInfo.setHeight(graphicInfo.getHeight() / scaleFactor);
    }

    protected void scaleFlowElements(Collection<FlowElement> elementList, BpmnModel bpmnModel, double scaleFactor) {
        for (FlowElement flowElement : elementList) {
            List<GraphicInfo> graphicInfoList = new ArrayList<>();
            if (flowElement instanceof SequenceFlow) {
                List<GraphicInfo> flowList = bpmnModel.getFlowLocationGraphicInfo(flowElement.getId());
                if (flowList != null) {
                    graphicInfoList.addAll(flowList);
                }

                // no graphic info for Data Objects
            } else if (!DataObject.class.isInstance(flowElement)) {
                graphicInfoList.add(bpmnModel.getGraphicInfo(flowElement.getId()));
            }

            scaleGraphicInfoList(graphicInfoList, scaleFactor);

            if (flowElement instanceof SubProcess) {
                SubProcess subProcess = (SubProcess) flowElement;
                scaleFlowElements(subProcess.getFlowElements(), bpmnModel, scaleFactor);
            }
        }
    }


    protected void scaleArtifacts(Collection<Artifact> artifactList, BpmnModel bpmnModel, double scaleFactor) {
        for (Artifact artifact : artifactList) {
            List<GraphicInfo> graphicInfoList = new ArrayList<>();
            if (artifact instanceof Association) {
                List<GraphicInfo> flowList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
                if (flowList != null) {
                    graphicInfoList.addAll(flowList);
                }
            } else {
                graphicInfoList.add(bpmnModel.getGraphicInfo(artifact.getId()));
            }

            scaleGraphicInfoList(graphicInfoList, scaleFactor);
        }
    }
}
