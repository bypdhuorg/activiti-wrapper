package com.wf.modules.flowable.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wf.modules.flowable.entity.BpmInstance;
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
public class BpmInstanceControllerTest{

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
        BpmInstance entity= new BpmInstance();
        entity.setDefId("113720584822591488");
        entity.setBizKey("mei1");
        entity.setBusData("{\"biz_gitapply\":{\n\t\"status\":\"this is status\",\n\t\"result\":\"this is result updated\",\n\t\"bu_id\":\"this is buid updated\",\n\t\"content\":\"this is content updated\"\n}}");

        //TODO set
        JSONObject json = JSONUtil.parseObj(entity);
        mvc.perform(MockMvcRequestBuilders.post("/bpmInstance")
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
        String bpmInstanceinfo="&departmentId=departmentId&userId=";
        mvc.perform(MockMvcRequestBuilders.get("/bpmInstance/getByPage?" + pageinfo+pageTimeinfo+bpmInstanceinfo)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void get() throws Exception {
        String id = "";
        mvc.perform(MockMvcRequestBuilders.get("/bpmInstance/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void update() throws Exception {

        String id = "114913402333171712";
        BpmInstance entity= new BpmInstance();
        entity.setDefId("113720584822591488");
        entity.setBizKey("mei1");
        entity.setBusData("{\"biz_gitapply\":{\"id\":\"114913406162571264\",\n\t\"status\":\"statusd\",\n\t\"result\":\"this is result updated\",\n\t\"bu_id\":\"this is buid updated\",\n\t\"content\":\"this is content updated\"\n}}");

        //TODO set
        JSONObject json = JSONUtil.parseObj(entity);
        mvc.perform(MockMvcRequestBuilders.put("/bpmInstance/" + id)
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
        mvc.perform(MockMvcRequestBuilders.delete("/bpmInstance/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void start() throws Exception {

        String id = "114913402333171712";
        BpmInstance entity= new BpmInstance();
        entity.setDefId("113720584822591488");
        entity.setBusData("{\"biz_gitapply\":{\"id\":\"114913406162571264\",\n\t\"status\":\"statusd\",\n\t\"result\":\"this is result updated\",\n\t\"bu_id\":\"this is buid updated\",\n\t\"content\":\"this is content updated\"\n}}");

        //TODO set
        JSONObject json = JSONUtil.parseObj(entity);
        mvc.perform(MockMvcRequestBuilders.put("/bpmInstance/start" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void suspend() throws Exception {
        String id = "115237452146282496";
        mvc.perform(MockMvcRequestBuilders.put("/bpmInstance/suspend/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void active() throws Exception {
        String id = "115237452146282496";
        mvc.perform(MockMvcRequestBuilders.put("/bpmInstance/active/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
