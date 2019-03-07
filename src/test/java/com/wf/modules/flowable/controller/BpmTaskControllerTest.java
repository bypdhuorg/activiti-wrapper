package com.wf.modules.flowable.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wf.modules.flowable.entity.BpmTask;
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

/**
 * @author meibo
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
public class BpmTaskControllerTest{

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
        BpmTask entity= new BpmTask();
        //TODO set
        JSONObject json = JSONUtil.parseObj(entity);
        mvc.perform(MockMvcRequestBuilders.post("/bpmTask")
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
        String bpmTaskinfo="&departmentId=departmentId&userId=";
        mvc.perform(MockMvcRequestBuilders.get("/bpmTask/getByPage?" + pageinfo+pageTimeinfo+bpmTaskinfo)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void get() throws Exception {
        String id = "";
        mvc.perform(MockMvcRequestBuilders.get("/bpmTask/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void update() throws Exception {

        String id = "";
        BpmTask entity= new BpmTask();
        //TODO set
        JSONObject json = JSONUtil.parseObj(entity);
        mvc.perform(MockMvcRequestBuilders.put("/bpmTask/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void delete() throws Exception {
        String id = "";
        mvc.perform(MockMvcRequestBuilders.delete("/bpmTask/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
