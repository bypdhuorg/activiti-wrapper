package com.flowablewrapper.services.db;

import com.flowablewrapper.bean.db.BusColumn;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class BusColumnServiceImplTest {

    @Autowired
    private BusTableService busTableService;


    @Autowired
    private BusColumnService busColumnService;

    @Before
    public void setUp() throws Exception {
    }


    @Test
    public void save() {
        BusColumn busColumn=new BusColumn();
        busColumn.setTableId("111298837485719552");
//        busColumn.setTableName("biz_gitapply");
//        busColumn.setTableComment("Gitlab申请");
//        busColumn.setIsCreated(false);
//        busTableService.save(busTable);
    }
}