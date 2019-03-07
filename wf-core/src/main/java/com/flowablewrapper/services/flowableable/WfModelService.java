package com.flowablewrapper.services.flowableable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowablewrapper.Pager;
import com.flowablewrapper.bean.SortDirection;
import com.flowablewrapper.bean.db.BpmDefinition;
import com.flowablewrapper.bean.db.BpmInstance;
import com.flowablewrapper.bean.dto.ModelListBean;
import com.flowablewrapper.bean.vo.FlowModelPageVO;
import com.flowablewrapper.bean.vo.FlowModelVO;
import com.flowablewrapper.dao.BpmDefinitionRepository;
import com.flowablewrapper.dao.BpmInstanceRepository;
import com.flowablewrapper.engine.constants.EnumBpmDefinitionStatus;
import com.flowablewrapper.exception.BusinessException;
import com.flowablewrapper.common.utils.ImageGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.editor.constants.EditorJsonConstants;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.constants.StencilConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.bpmn.deployer.BpmnDeployer;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.persistence.deploy.DeploymentManager;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    private ProcessEngine processEngine;
    private ObjectMapper objectMapper;
    private ProcessEngineConfiguration processEngineConfiguration;

    private BpmDefinitionRepository bpmDefinitionRepository;
    private BpmInstanceRepository bpmInstanceRepository;

    @Autowired
    public WfModelService(ProcessEngine processEngine,
                          ObjectMapper objectMapper,
                          BpmDefinitionRepository bpmDefinitionRepository,
                          ProcessEngineConfiguration processEngineConfiguration,
                          BpmInstanceRepository bpmInstanceRepository) {
        this.processEngine = processEngine;
        this.objectMapper=objectMapper;
        this.bpmDefinitionRepository=bpmDefinitionRepository;
        this.processEngineConfiguration=processEngineConfiguration;
        this.bpmInstanceRepository=bpmInstanceRepository;
    }

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

    public byte[] getModelThumbnail(String modelId) throws Exception {
        try {
            BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
            ObjectNode editorJsonNode =(ObjectNode)this.getModelJson(modelId).getObjectNode().get("model");
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
            log.error("Error creating thumbnail image {}", modelId, e);
        }

        return null;

    }

    /**
     * 创建新Model
     * @param bpmDefinition
     * @throws Exception
     */
    public void createModel(BpmDefinition bpmDefinition) throws Exception{
        //check if exist
        List<BpmDefinition> BpmDefinitionlist =bpmDefinitionRepository.findByKey(bpmDefinition.getKey());
        if(BpmDefinitionlist.size()>0){
            throw new BusinessException("流程定义Key重复：" + bpmDefinition.getKey());
        }
        bpmDefinition.setIsMain(true);
        bpmDefinition.setStatus(EnumBpmDefinitionStatus.DRAFT);
        bpmDefinition.setVersion(1);

        //保存到flowable
        String modelId = this.saveModelToFlowable(bpmDefinition);

        bpmDefinition.setActModelId(modelId);
        bpmDefinitionRepository.save(bpmDefinition);

    }


    /**
     * 修改Model
     * @param updateModel
     * @throws Exception
     */
    public void updateModel(FlowModelVO updateModel) throws Exception {
        RepositoryService repositoryService=processEngine.getRepositoryService();

        //
        Model model = repositoryService.getModel(updateModel.getId());

        String json =updateModel.getJson_xml();
        try {
            //1.flowable 更新
            byte[] graphBytes =json.getBytes("utf-8");
            repositoryService.addModelEditorSource(model.getId(), graphBytes);

            //2.
            ObjectNode modelNode = (ObjectNode)new ObjectMapper().readTree(graphBytes);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

            BpmDefinition bpmDefinition =bpmDefinitionRepository.findByKeyAndIsMain(updateModel.getKey(),true);
            bpmDefinition.setName(updateModel.getName());
            bpmDefinition.setDescription(updateModel.getDescription());

            //流程定义ID 空
            Integer version=bpmDefinition.getVersion();
            Boolean isActExist=StringUtils.isNotEmpty(bpmDefinition.getActDefId());
            if(!isActExist || updateModel.isNewversion()){
               this.deployFlowableModel(bpmDefinition,model,bpmnBytes);

               //如果之前没有发布过，仅仅需要保存
               if(!isActExist){
                   bpmDefinitionRepository.save(bpmDefinition);
               }else{
                   //修改最新版本标志，保存原始版本
                   bpmDefinition.setIsMain(false);
                   bpmDefinitionRepository.save(bpmDefinition);

                   //保存新版本
                   bpmDefinition.setId(null);
                   bpmDefinition.setIsMain(true);
                   version=version+1;
                   bpmDefinition.setVersion(version);
                   bpmDefinitionRepository.save(bpmDefinition);
               }
            }else{
               this.updateFlowableModel(bpmDefinition,model,bpmnBytes);
                bpmDefinitionRepository.save(bpmDefinition);
            }

            //3.

            ObjectNode modelObjectNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, updateModel.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, version);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, updateModel.getDescription());

            model.setMetaInfo(modelObjectNode.toString());
            model.setKey(updateModel.getKey());
            model.setName(updateModel.getName());
            model.setCategory(updateModel.getCategory());
            repositoryService.saveModel(model);

        }catch (Exception e) {
            throw new Exception("Process model could not be saved " + model.getId()+";message:"+e.getMessage());
        }
    }



    /**
     * 删除
     * @param modelId
     */
    public void delete(String modelId){
        Optional<BpmDefinition> bpmDefinitionOp=bpmDefinitionRepository.findById(modelId);

        BpmDefinition bpmDefinition ;
        if(bpmDefinitionOp.isPresent()){
            bpmDefinition =bpmDefinitionOp.get();
        }else{
            return ;
        }

        //判断是否启动过流程
        List<BpmInstance> bpmInstances =bpmInstanceRepository.findByDefId(bpmDefinition.getId());
        if(bpmInstances.size()>0){
            throw new BusinessException("该流程定义下存在流程实例，请勿删除！<br> 请清除数据后再来删除");
        }

        List<BpmDefinition>  bpmDefinitions =bpmDefinitionRepository.findByKey(bpmDefinition.getKey());
        RepositoryService repositoryService=processEngine.getRepositoryService();

        for (BpmDefinition bpmDefinitionUnit:bpmDefinitions){
            //db delete
            bpmDefinitionRepository.delete(bpmDefinitionUnit);

            //flowable 处理
            //有发布流程ID时候，删除ID
            if(StringUtils.isNotEmpty(bpmDefinitionUnit.getActDeployId())){
                repositoryService.deleteDeployment(bpmDefinitionUnit.getActDeployId());
            }

            //有发布ModelID时候，删除Model
            if(StringUtils.isNotEmpty(bpmDefinitionUnit.getActModelId())){
                repositoryService.deleteModel(bpmDefinitionUnit.getActModelId());
            }
        }
    }

    /**
     * 取得流程分页
     *
     * @return
     */
    public Pager<ModelListBean> models(FlowModelPageVO inVo) {

        RepositoryService repositoryService = processEngine.getRepositoryService();

        ModelQuery modelQuery = repositoryService.createModelQuery();

        //组织条件
        if (StringUtils.isNotEmpty(inVo.getCategory())) {
            modelQuery.modelCategoryLike(inVo.getCategory());
        }
        if (StringUtils.isNotEmpty(inVo.getName())) {
            modelQuery.modelNameLike(inVo.getName());
        }

        //取得总条数
        int total = Math.toIntExact(modelQuery.count());

        //设置排序字段
        switch (inVo.getSortby().toLowerCase()) {
            case "id":
                modelQuery.orderByModelId();
                break;
            case "name":
                modelQuery.orderByModelKey();
                break;
            case "deployTime":
                modelQuery.orderByModelName();
                break;
            default:
                modelQuery.orderByLastUpdateTime();
        }

        //设置排序方向
        if (SortDirection.asc.equals(inVo.getDirection())) {
            modelQuery.asc();
        } else {
            modelQuery.desc();
        }

        List<Model> modelDefinitions = modelQuery.listPage(inVo.getPage(), inVo.getPageSize());

        //后处理
        List<ModelListBean> returnList = new ArrayList<>();
        for (Model deployment : modelDefinitions) {

            ModelListBean tempBean = new ModelListBean();
                //流程类别
                tempBean.setCategory(deployment.getCategory());
                //流程名称
                tempBean.setName(deployment.getName());
                //部署的key
                tempBean.setKey(deployment.getKey());
                tempBean.setId(deployment.getId());
                tempBean.setDeploymentId(deployment.getDeploymentId());
                //流程Key
                tempBean.setVersion(deployment.getVersion());
                //deployment.getCreateTime()
                //时间
                tempBean.setLastUpdateTime(deployment.getLastUpdateTime());
                returnList.add(tempBean);

        }
        return new Pager<ModelListBean>(total, returnList);
    }

    /**
     *取得Mode的Json数据
     * @param modelId
     * @return
     * @throws Exception
     */
    public FlowModelVO getModelJson(String modelId) throws Exception {
        RepositoryService repositoryService=processEngine.getRepositoryService();
        Model model = repositoryService.getModel(modelId);

        if (model == null){ throw new NullPointerException("模型不存在");}

        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(MODEL_ID, model.getId());
        modelNode.put(MODEL_NAME, model.getName());
        modelNode.put("category", model.getCategory());
        modelNode.put("key", model.getKey());

        String tmpDes="";
        if (StringUtils.isNotEmpty(model.getMetaInfo())) {
            String jsonMetaInfo =new String(repositoryService.getModelEditorSource(model.getId()));
            ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(jsonMetaInfo);
            editorJsonNode.put("modelType", "model");
            modelNode.set("model", editorJsonNode);
            if(editorJsonNode.get(StencilConstants.PROPERTY_DOCUMENTATION)!=null){
                tmpDes=editorJsonNode.get(StencilConstants.PROPERTY_DOCUMENTATION).toString();
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

        FlowModelVO vo=new FlowModelVO();
        vo.setCategory(model.getCategory());
        vo.setName(model.getName());
        vo.setId(model.getId());
        vo.setKey(model.getKey());
        vo.setObjectNode(modelNode);
        vo.setVersion(model.getVersion());
        vo.setDescription(tmpDes);
        return vo;
    }

    /**
     * 在Flowerable 中创建一个模块
     * @param bpmDefinition
     * @return
     */
    private String saveModelToFlowable(BpmDefinition bpmDefinition){

        try {
            RepositoryService repositoryService=processEngine.getRepositoryService();

            org.flowable.engine.repository.Model modelData = repositoryService.newModel();

            modelData.setKey(bpmDefinition.getKey());
            modelData.setName(bpmDefinition.getName());
            modelData.setCategory(bpmDefinition.getCategory());

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, bpmDefinition.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, bpmDefinition.getDescription());
            modelData.setMetaInfo(modelObjectNode.toString());

            //1.save model
            repositoryService.saveModel(modelData);

            //2.
            String jsonMetaInfo = this.createModelJson(bpmDefinition);
            repositoryService.addModelEditorSource(modelData.getId(), jsonMetaInfo.getBytes("utf-8"));

            return modelData.getId();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("创建flowable流程定义失败！", e);
        }
    }

    /**
     * 部署流程到flowable
     * @param definition
     * @param model
     * @param bpmnBytes
     */
    private void deployFlowableModel(BpmDefinition definition, Model model, byte[] bpmnBytes){
        String processName = model.getKey() +BPMN_RESOURCE_SUFFIXES;

        Deployment deployment = this.processEngine.getRepositoryService().createDeployment()
                .name(model.getKey())
                .addString(processName, new String(bpmnBytes))
                .deploy();

        ProcessDefinition proDefinition = (ProcessDefinition)this.processEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        if (proDefinition == null){
            throw new RuntimeException("deploy error");
        }
        //设置已经部署的信息
        model.setDeploymentId(deployment.getId());
        definition.setActDefId(proDefinition.getId());
        definition.setActDeployId(deployment.getId());
        definition.setActModelId(model.getId());
    }

    private void updateFlowableModel(BpmDefinition definition, Model model, byte[] bpmnBytes){
        RepositoryService repositoryService=this.processEngine.getRepositoryService();
        ProcessDefinition bpmnProcessDef = (ProcessDefinition)repositoryService.getProcessDefinition(definition.getActDefId());

        ProcessEngineConfigurationImpl conf = (ProcessEngineConfigurationImpl)this.processEngineConfiguration;
        DeploymentManager deploymentManager = conf.getDeploymentManager();
//
        BpmnDeployer deployer = (BpmnDeployer)deploymentManager.getDeployers().get(0);
        DeploymentEntity deploy = (DeploymentEntity)repositoryService.createDeploymentQuery().deploymentId(definition.getActDeployId()).list().get(0);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bpmnBytes);

        BpmnParse bpmnParse = conf.getBpmnParser().createParse()
                .sourceInputStream(inputStream)
//                .sourceString(new String (bpmnBytes))
                .setSourceSystemId(model.getKey() + BPMN_RESOURCE_SUFFIXES)
                .deployment(deploy)
                .name(model.getKey() + BPMN_RESOURCE_SUFFIXES);
        bpmnParse.setValidateProcess(false);
        bpmnParse.execute();

        BpmnModel bpmnModel = bpmnParse.getBpmnModel();

        deploymentManager.getAppResourceCache().add(bpmnProcessDef.getId(), bpmnModel);

        byte[] diagramBytes = IoUtil.readInputStream(this.processEngineConfiguration.getProcessDiagramGenerator().generateDiagram
                (bpmnModel,
                        "png",
                        this.processEngineConfiguration.getActivityFontName(),
                        this.processEngineConfiguration.getLabelFontName(),
                        this.processEngineConfiguration.getAnnotationFontName(),
                        this.processEngineConfiguration.getClassLoader(),true)
                , null);

        //TODO
    }


    /**
     * 新增Model json
     * @param model
     * @return
     */
    private String createModelJson(BpmDefinition model) {
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
