package com.wf.modules.flowable.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wf.common.constant.EnumColumnType;
import com.wf.modules.flowable.entity.BusColumn;
import com.wf.modules.flowable.entity.BusTable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author meibo
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
public class BusTableControllerTest{

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    private MockHttpSession session;

    @Before
    public void setupMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        session = new MockHttpSession();
    }


    @Test
    public void create() throws Exception {
        BusTable busTable= new BusTable();
//        busTable.setId("112279867021594624");
        busTable.setBusKey("bizgitapply");
        busTable.setTableName("biz_gitapply");
        busTable.setTableComment("Gitlab申请");
        busTable.setIsCreated(false);

        List<BusColumn> columns=new ArrayList<>();
        //1
        BusColumn busColumn1=new BusColumn();
        busColumn1.setFieldName("id");
        busColumn1.setPrimaryKey(true);
        busColumn1.setFieldType(EnumColumnType.LONG);
        busColumn1.setIsAutoIncrement(true);

        BusColumn busColumn2=new BusColumn();
        busColumn2.setFieldName("status");
        busColumn2.setColumnComment("申请人ID");
        busColumn2.setFieldType(EnumColumnType.VARCHAR);
        busColumn2.setColumnIsCanNull(true);
        busColumn2.setColumnLength(20);

        BusColumn busColumn3=new BusColumn();
        busColumn3.setFieldName("result");
        busColumn3.setColumnComment("result");
        busColumn3.setFieldType(EnumColumnType.VARCHAR);
        busColumn3.setColumnIsCanNull(true);
        busColumn3.setColumnLength(20);

        columns.add(busColumn1);
        columns.add(busColumn2);
        columns.add(busColumn3);

        busTable.setColumns(columns);
        //TODO set
        JSONObject json = JSONUtil.parseObj(busTable);
        mvc.perform(MockMvcRequestBuilders.post("/busTable")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void page() throws Exception {

        String pageinfo="page=1&pageSize=10&sort=id&order=desc";
        String pageTimeinfo="&startDate=1519699822000&endDate=1551235822000";
        String busTableinfo="&departmentId=departmentId&userId=";
        mvc.perform(MockMvcRequestBuilders.get("/busTable/getByPage?" + pageinfo+pageTimeinfo+busTableinfo)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void get() throws Exception {
        String id = "";
        mvc.perform(MockMvcRequestBuilders.get("/busTable/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void update() throws Exception {
        String id = "113121531776733184";

        BusTable busTable= new BusTable();
        busTable.setBusKey("testbean");
        busTable.setTableName("biz_gitapply2");
        busTable.setTableComment("Gitlab申请5");
        busTable.setIsCreated(false);

        List<BusColumn> columns=new ArrayList<>();
        //1
        BusColumn busColumn1=new BusColumn();
        busColumn1.setFieldName("id");
        busColumn1.setPrimaryKey(true);
        busColumn1.setFieldType(EnumColumnType.LONG);
        busColumn1.setIsAutoIncrement(true);

        BusColumn busColumn2=new BusColumn();
        busColumn2.setFieldName("userIdaaa");
        busColumn2.setColumnComment("申请人ID2ss");
        busColumn2.setFieldType(EnumColumnType.VARCHAR);
        busColumn2.setColumnIsCanNull(true);
        busColumn2.setColumnLength(20);

        columns.add(busColumn1);
        columns.add(busColumn2);

        busTable.setColumns(columns);
        //TODO set
        JSONObject json = JSONUtil.parseObj(busTable);
        mvc.perform(MockMvcRequestBuilders.put("/busTable/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void delete() throws Exception {
        String id = "112279867021594624";
        mvc.perform(MockMvcRequestBuilders.delete("/busTable/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
