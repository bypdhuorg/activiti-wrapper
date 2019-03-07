package com.flowablewrapper.services.db;

import com.flowablewrapper.bean.db.BusColumn;
import com.flowablewrapper.bean.db.BusTable;
import com.flowablewrapper.common.constant.EnumColumnType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class BusTableServiceImplTest {

    @Autowired
    private BusTableService busTableService;
    @Before
    public void setUp() throws Exception {
//        ImageGenerator t=new ImageGenerator();
    }

    @Test
    public void save() {
        BusTable busTable=new BusTable();
        busTable.setId("112279867021594624");
        busTable.setBusKey("testbean");
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
        busColumn2.setFieldName("userId");
        busColumn2.setColumnComment("申请人ID");
        busColumn2.setFieldType(EnumColumnType.VARCHAR);
        busColumn2.setColumnIsCanNull(true);
        busColumn2.setColumnLength(20);

        columns.add(busColumn1);
        columns.add(busColumn2);

        busTable.setColumns(columns);
        busTableService.save(busTable);
    }

    @Test
    public void createTable() {
        BusTable busTable =busTableService.get("112279867021594624");
        busTableService.createTable(busTable);
    }
}