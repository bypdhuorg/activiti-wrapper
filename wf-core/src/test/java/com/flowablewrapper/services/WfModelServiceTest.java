package com.flowablewrapper.services;

import com.flowablewrapper.Pager;
import com.flowablewrapper.bean.SortDirection;
import com.flowablewrapper.bean.db.BpmDefinition;
import com.flowablewrapper.bean.dto.ModelListBean;
import com.flowablewrapper.bean.vo.FlowModelPageVO;
import com.flowablewrapper.bean.vo.FlowModelVO;
import com.flowablewrapper.services.flowableable.WfModelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class WfModelServiceTest {

    @Autowired
    private WfModelService modelService;

    @Test
    public void createModel() throws Exception {
        BpmDefinition modelRepresentation=new BpmDefinition();
        modelRepresentation.setCategory("abcd1111");
//        modelRepresentation.setComment("dcdd");
        modelRepresentation.setName("abcd1111");
        modelRepresentation.setKey("abcd1111");
        modelRepresentation.setBunissnessTable("table");
        modelService.createModel(modelRepresentation);
    }

    @Test
    public void updateModel() {
        FlowModelVO modelRepresentation=new FlowModelVO();
        modelRepresentation.setCategory("testCate");
//        modelRepresentation.setComment("dcdd");
        modelRepresentation.setName("abcd");
        modelRepresentation.setKey("abcd");
        modelRepresentation.setId("7662d6bc-34d8-11e9-b0a4-2c6e855d3d37");
        try {
            modelRepresentation.setJson_xml(
                    "{\"modelId\":\"ef2323fe-30fc-11e9-bd75-2c6e855d3d37\",\"bounds\":{\"lowerRight\":{\"x\":1200,\"y\":1050},\"upperLeft\":{\"x\":0,\"y\":0}},\"properties\":{\"process_id\":\"abcddd\",\"name\":\"t1aa\",\"documentation\":\"t1\",\"process_author\":\"\",\"process_version\":\"\",\"process_namespace\":\"http://www.flowable.org/processdef\",\"process_historylevel\":\"audit\",\"isexecutable\":\"true\",\"dataproperties\":\"\",\"executionlisteners\":\"\",\"eventlisteners\":\"null\",\"signaldefinitions\":\"\",\"messagedefinitions\":\"null\",\"process_potentialstarteruser\":\"\",\"process_potentialstartergroup\":\"\",\"iseagerexecutionfetch\":\"false\"},\"childShapes\":[{\"resourceId\":\"startEvent1\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"executionlisteners\":\"\",\"initiator\":\"\",\"formkeydefinition\":\"\",\"formreference\":\"\",\"formproperties\":\"\"},\"stencil\":{\"id\":\"StartNoneEvent\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-B973F4E1-BBF9-412E-8533-6AE5E9A57198\"}],\"bounds\":{\"lowerRight\":{\"x\":245,\"y\":60},\"upperLeft\":{\"x\":215,\"y\":30}},\"dockers\":[]},{\"resourceId\":\"sid-BBC1A8DE-C993-43F2-A7F1-A296FCF7B6B1\",\"properties\":{\"overrideid\":\"\",\"name\":\"中文哦ceshi\",\"documentation\":\"\",\"asynchronousdefinition\":\"false\",\"exclusivedefinition\":\"false\",\"executionlisteners\":\"\",\"multiinstance_type\":\"None\",\"multiinstance_cardinality\":\"\",\"multiinstance_collection\":\"\",\"multiinstance_variable\":\"\",\"multiinstance_condition\":\"\",\"isforcompensation\":\"false\",\"usertaskassignment\":\"\",\"formkeydefinition\":\"\",\"formreference\":\"\",\"duedatedefinition\":\"\",\"prioritydefinition\":\"\",\"formproperties\":\"\",\"tasklisteners\":\"\",\"skipexpression\":\"\",\"categorydefinition\":\"\",\"type\":\"http://b3mn.org/stencilset/bpmn2.0#UserTask\",\"scriptformat\":\"\",\"scripttext\":\"\",\"scriptautostorevariables\":\"false\",\"shellcommand\":\"\",\"shellarg1\":\"\",\"shellarg2\":\"\",\"shellarg3\":\"\",\"shellarg4\":\"\",\"shellarg5\":\"\",\"shellwait\":\"\",\"shelloutputvariable\":\"\",\"shellerrorcodevariable\":\"\",\"shellredirecterror\":\"\",\"shellcleanenv\":\"\",\"shelldirectory\":\"\"},\"stencil\":{\"id\":\"UserTask\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-159F9D6D-4E07-41B5-BA5F-84CF4BDD551C\"}],\"bounds\":{\"lowerRight\":{\"x\":280,\"y\":218},\"upperLeft\":{\"x\":180,\"y\":138}},\"dockers\":[]},{\"resourceId\":\"sid-A89F7105-8E9A-44C1-AE78-99D9C320515C\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"asynchronousdefinition\":\"false\",\"exclusivedefinition\":\"false\",\"sequencefloworder\":\"\"},\"stencil\":{\"id\":\"ExclusiveGateway\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-70B43296-8B68-4BBB-8F6E-D029C75778ED\"},{\"resourceId\":\"sid-7721E924-091C-4CA0-9018-45E5BD68AB54\"}],\"bounds\":{\"lowerRight\":{\"x\":250,\"y\":280},\"upperLeft\":{\"x\":210,\"y\":240}},\"dockers\":[]},{\"resourceId\":\"sid-7993B78A-05B3-4E78-BB89-14B9CFF5FB0D\",\"properties\":{\"overrideid\":\"\",\"name\":\"安全审批\",\"documentation\":\"\",\"asynchronousdefinition\":\"false\",\"exclusivedefinition\":\"false\",\"executionlisteners\":\"\",\"multiinstance_type\":\"None\",\"multiinstance_cardinality\":\"\",\"multiinstance_collection\":\"\",\"multiinstance_variable\":\"\",\"multiinstance_condition\":\"\",\"isforcompensation\":\"false\",\"usertaskassignment\":\"\",\"formkeydefinition\":\"\",\"formreference\":\"\",\"duedatedefinition\":\"\",\"prioritydefinition\":\"\",\"formproperties\":\"\",\"tasklisteners\":\"\",\"skipexpression\":\"\",\"categorydefinition\":\"\"},\"stencil\":{\"id\":\"UserTask\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-0E4CAF6F-0B55-40E5-BE6E-5A3328B50346\"}],\"bounds\":{\"lowerRight\":{\"x\":280,\"y\":410},\"upperLeft\":{\"x\":180,\"y\":330}},\"dockers\":[]},{\"resourceId\":\"sid-0E4CAF6F-0B55-40E5-BE6E-5A3328B50346\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":\"false\",\"skipexpression\":\"\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-06F8E491-84D8-4CDC-ACFA-E2D33E27A65B\"}],\"bounds\":{\"lowerRight\":{\"x\":230,\"y\":464.9765625},\"upperLeft\":{\"x\":230,\"y\":410.0234375}},\"dockers\":[{\"x\":50,\"y\":40},{\"x\":50,\"y\":40}],\"target\":{\"resourceId\":\"sid-06F8E491-84D8-4CDC-ACFA-E2D33E27A65B\"}},{\"resourceId\":\"sid-23967F05-559B-47AE-B758-F6639157CEE4\",\"properties\":{\"overrideid\":\"\",\"name\":\"CMDB变更\",\"documentation\":\"\",\"asynchronousdefinition\":\"false\",\"exclusivedefinition\":\"false\",\"executionlisteners\":\"\",\"multiinstance_type\":\"None\",\"multiinstance_cardinality\":\"\",\"multiinstance_collection\":\"\",\"multiinstance_variable\":\"\",\"multiinstance_condition\":\"\",\"isforcompensation\":\"false\",\"usertaskassignment\":\"\",\"formkeydefinition\":\"\",\"formreference\":\"\",\"duedatedefinition\":\"\",\"prioritydefinition\":\"\",\"formproperties\":\"\",\"tasklisteners\":\"\",\"skipexpression\":\"\",\"categorydefinition\":\"\"},\"stencil\":{\"id\":\"UserTask\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-B9A59FC2-58F1-47B0-AB7E-07C37E8AD42F\"}],\"bounds\":{\"lowerRight\":{\"x\":280,\"y\":770},\"upperLeft\":{\"x\":180,\"y\":690}},\"dockers\":[]},{\"resourceId\":\"sid-FAA51A62-F153-4252-B2DB-238E75EE57D8\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"asynchronousdefinition\":\"false\",\"exclusivedefinition\":\"false\",\"sequencefloworder\":\"\"},\"stencil\":{\"id\":\"ExclusiveGateway\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-39435A87-5975-4595-92B5-9E4E1F88D7E5\"},{\"resourceId\":\"sid-3F1E949B-A91B-4A8F-AFDE-E26D8EE8FFBE\"}],\"bounds\":{\"lowerRight\":{\"x\":250,\"y\":655},\"upperLeft\":{\"x\":210,\"y\":615}},\"dockers\":[]},{\"resourceId\":\"sid-6BE15994-8F1D-4D24-93F5-71A4F9BE9912\",\"properties\":{\"overrideid\":\"\",\"name\":\"工单验收\",\"documentation\":\"\",\"asynchronousdefinition\":\"false\",\"exclusivedefinition\":\"false\",\"executionlisteners\":\"\",\"multiinstance_type\":\"None\",\"multiinstance_cardinality\":\"\",\"multiinstance_collection\":\"\",\"multiinstance_variable\":\"\",\"multiinstance_condition\":\"\",\"isforcompensation\":\"false\",\"usertaskassignment\":\"\",\"formkeydefinition\":\"\",\"formreference\":\"\",\"duedatedefinition\":\"\",\"prioritydefinition\":\"\",\"formproperties\":\"\",\"tasklisteners\":\"\",\"skipexpression\":\"\",\"categorydefinition\":\"\"},\"stencil\":{\"id\":\"UserTask\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-3694019C-B177-4C20-BD87-9B317728B09A\"}],\"bounds\":{\"lowerRight\":{\"x\":280,\"y\":950},\"upperLeft\":{\"x\":180,\"y\":870}},\"dockers\":[]},{\"resourceId\":\"sid-C60C9313-72AF-432A-A636-4C0F96CB8BF1\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":\"false\",\"skipexpression\":\"\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-FAA51A62-F153-4252-B2DB-238E75EE57D8\"}],\"bounds\":{\"lowerRight\":{\"x\":230,\"y\":614.703125},\"upperLeft\":{\"x\":230,\"y\":545.609375}},\"dockers\":[{\"x\":50,\"y\":40},{\"x\":20,\"y\":20}],\"target\":{\"resourceId\":\"sid-FAA51A62-F153-4252-B2DB-238E75EE57D8\"}},{\"resourceId\":\"sid-3F1E949B-A91B-4A8F-AFDE-E26D8EE8FFBE\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":false,\"skipexpression\":\"\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-23967F05-559B-47AE-B758-F6639157CEE4\"}],\"bounds\":{\"lowerRight\":{\"x\":230.39314656876758,\"y\":689.1328264970746},\"upperLeft\":{\"x\":230.21622843123242,\"y\":655.6952985029254}},\"dockers\":[{\"x\":20.5,\"y\":20.5},{\"x\":50,\"y\":40}],\"target\":{\"resourceId\":\"sid-23967F05-559B-47AE-B758-F6639157CEE4\"}},{\"resourceId\":\"sid-B9A59FC2-58F1-47B0-AB7E-07C37E8AD42F\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":\"false\",\"skipexpression\":\"\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-6BE15994-8F1D-4D24-93F5-71A4F9BE9912\"}],\"bounds\":{\"lowerRight\":{\"x\":230,\"y\":869.625},\"upperLeft\":{\"x\":230,\"y\":770.375}},\"dockers\":[{\"x\":50,\"y\":40},{\"x\":50,\"y\":40}],\"target\":{\"resourceId\":\"sid-6BE15994-8F1D-4D24-93F5-71A4F9BE9912\"}},{\"resourceId\":\"sid-10526B7D-86C1-4763-9E75-9E10982D9DAA\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"executionlisteners\":\"\"},\"stencil\":{\"id\":\"EndNoneEvent\"},\"childShapes\":[],\"outgoing\":[],\"bounds\":{\"lowerRight\":{\"x\":353,\"y\":924},\"upperLeft\":{\"x\":325,\"y\":896}},\"dockers\":[]},{\"resourceId\":\"sid-3694019C-B177-4C20-BD87-9B317728B09A\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":\"false\",\"skipexpression\":\"\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-10526B7D-86C1-4763-9E75-9E10982D9DAA\"}],\"bounds\":{\"lowerRight\":{\"x\":324.375,\"y\":910},\"upperLeft\":{\"x\":280.390625,\"y\":910}},\"dockers\":[{\"x\":50,\"y\":40},{\"x\":14,\"y\":14}],\"target\":{\"resourceId\":\"sid-10526B7D-86C1-4763-9E75-9E10982D9DAA\"}},{\"resourceId\":\"sid-B973F4E1-BBF9-412E-8533-6AE5E9A57198\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":\"false\",\"skipexpression\":\"\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-BBC1A8DE-C993-43F2-A7F1-A296FCF7B6B1\"}],\"bounds\":{\"lowerRight\":{\"x\":230,\"y\":137.515625},\"upperLeft\":{\"x\":230,\"y\":60.546875}},\"dockers\":[{\"x\":15,\"y\":15},{\"x\":50,\"y\":40}],\"target\":{\"resourceId\":\"sid-BBC1A8DE-C993-43F2-A7F1-A296FCF7B6B1\"}},{\"resourceId\":\"sid-159F9D6D-4E07-41B5-BA5F-84CF4BDD551C\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":\"false\",\"skipexpression\":\"\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-A89F7105-8E9A-44C1-AE78-99D9C320515C\"}],\"bounds\":{\"lowerRight\":{\"x\":230.37284575524222,\"y\":239.519549614967},\"upperLeft\":{\"x\":230.24824799475778,\"y\":218.960919135033}},\"dockers\":[{\"x\":50,\"y\":40},{\"x\":20.5,\"y\":20.5}],\"target\":{\"resourceId\":\"sid-A89F7105-8E9A-44C1-AE78-99D9C320515C\"}},{\"resourceId\":\"sid-7721E924-091C-4CA0-9018-45E5BD68AB54\",\"properties\":{\"overrideid\":\"\",\"name\":\"1111\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":false,\"skipexpression\":\"\",\"showdiamondmarker\":false},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-7993B78A-05B3-4E78-BB89-14B9CFF5FB0D\"}],\"bounds\":{\"lowerRight\":{\"x\":230,\"y\":330},\"upperLeft\":{\"x\":230,\"y\":280.25}},\"dockers\":[{\"x\":20,\"y\":34},{\"x\":50,\"y\":40}],\"target\":{\"resourceId\":\"sid-7993B78A-05B3-4E78-BB89-14B9CFF5FB0D\"}},{\"resourceId\":\"sid-70B43296-8B68-4BBB-8F6E-D029C75778ED\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":true,\"skipexpression\":\"\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-06F8E491-84D8-4CDC-ACFA-E2D33E27A65B\"}],\"bounds\":{\"lowerRight\":{\"x\":209.48046875,\"y\":505},\"upperLeft\":{\"x\":128,\"y\":260.5}},\"dockers\":[{\"x\":20.5,\"y\":20.5},{\"x\":128,\"y\":260.5},{\"x\":128,\"y\":505},{\"x\":50,\"y\":40}],\"target\":{\"resourceId\":\"sid-06F8E491-84D8-4CDC-ACFA-E2D33E27A65B\"}},{\"resourceId\":\"sid-39435A87-5975-4595-92B5-9E4E1F88D7E5\",\"properties\":{\"overrideid\":\"\",\"name\":\"\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":true,\"skipexpression\":\"\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-6BE15994-8F1D-4D24-93F5-71A4F9BE9912\"}],\"bounds\":{\"lowerRight\":{\"x\":209.28515625,\"y\":910},\"upperLeft\":{\"x\":127,\"y\":635.5}},\"dockers\":[{\"x\":20.5,\"y\":20.5},{\"x\":127,\"y\":635.5},{\"x\":127,\"y\":910},{\"x\":50,\"y\":40}],\"target\":{\"resourceId\":\"sid-6BE15994-8F1D-4D24-93F5-71A4F9BE9912\"}},{\"resourceId\":\"sid-06F8E491-84D8-4CDC-ACFA-E2D33E27A65B\",\"properties\":{\"overrideid\":\"\",\"name\":\"IDC审批\",\"documentation\":\"\",\"asynchronousdefinition\":\"false\",\"exclusivedefinition\":\"false\",\"executionlisteners\":\"\",\"multiinstance_type\":\"None\",\"multiinstance_cardinality\":\"\",\"multiinstance_collection\":\"\",\"multiinstance_variable\":\"\",\"multiinstance_condition\":\"\",\"isforcompensation\":\"false\",\"usertaskassignment\":\"\",\"formkeydefinition\":\"\",\"formreference\":\"\",\"duedatedefinition\":\"\",\"prioritydefinition\":\"\",\"formproperties\":\"\",\"tasklisteners\":\"\",\"skipexpression\":\"\",\"categorydefinition\":\"\",\"type\":\"http://b3mn.org/stencilset/bpmn2.0#ServiceTask\",\"servicetasktriggerable\":\"false\",\"servicetaskclass\":\"\",\"servicetaskexpression\":\"\",\"servicetaskdelegateexpression\":\"\",\"servicetaskfields\":\"\",\"servicetaskuselocalscopeforresultvariable\":\"false\"},\"stencil\":{\"id\":\"UserTask\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-C60C9313-72AF-432A-A636-4C0F96CB8BF1\"}],\"bounds\":{\"lowerRight\":{\"x\":280,\"y\":545},\"upperLeft\":{\"x\":180,\"y\":465}},\"dockers\":[]}],\"stencil\":{\"id\":\"BPMNDiagram\"},\"stencilset\":{\"namespace\":\"http://b3mn.org/stencilset/bpmn2.0#\",\"url\":\"../editor/stencilsets/bpmn2.0/bpmn2.0.json\"}}");
            modelRepresentation.setNewversion(true);
            modelService.updateModel(modelRepresentation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delModel() {
        try {
            modelService.delete("110825738415902720");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getList() {

        FlowModelPageVO inVo=new FlowModelPageVO();
        inVo.setPage(0);
        inVo.setPageSize(100);
        inVo.setSortby("id");
        inVo.setDirection(SortDirection.asc);
        Pager<ModelListBean> list =modelService.models(inVo);
//        System.out.println(list);

        System.out.println("总条数"+list.getTotal());
        for(ModelListBean task : list.getContent()){
            System.out.println("getCategory：" + task.getCategory());
            System.out.println("getName：" + task.getName());
            System.out.println("getVersion：" + task.getVersion());
            System.out.println("getKey：" + task.getKey());
            System.out.println("####################：" );
        }
    }

    @Test
    public void getModelJson() throws Exception {

//        ObjectNode node= modelService.getModelJson("563d14b4-1937-11e9-b0af-2c6e855d3d37");
//        System.out.println(node);
    }
}