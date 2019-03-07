package com.flowablewrapper.services.db.impl;

import com.flowablewrapper.bean.db.BusColumn;
import com.flowablewrapper.bean.db.BusTable;
import com.flowablewrapper.common.table.ColumnSql;
import com.flowablewrapper.common.table.TableSql;
import com.flowablewrapper.dao.BusColumnRepository;
import com.flowablewrapper.dao.BusTableRepository;
import com.flowablewrapper.dao.mapper.TableOperationMapper;
import com.flowablewrapper.exception.BusinessException;
import com.flowablewrapper.services.db.BusTableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 业务库表接口实现
 * @author meibo
 */
@Service
@Slf4j
@Transactional
public class BusTableServiceImpl  implements BusTableService {

    /**
     * 表 操作
     */
    @Autowired
    private BusTableRepository busTableRepository;

    /**
     * 表 操作
     */
    @Autowired
    private TableOperationMapper tableOperationMapper;

    /**
     * 字段 操作
     */
    @Autowired
    private BusColumnRepository busColumnRepository;

    @Override
    public BusTableRepository getRepository() {
        return busTableRepository;
    }

    @Override
    public BusTable save(BusTable businessTable){

        //保存表
        busTableRepository.save(businessTable);

        //删除字段
        busColumnRepository.deleteByTableId(businessTable.getId());

        //添加字段
        for (BusColumn busColumn :businessTable.getColumns()){
            busColumn.setTableId(businessTable.getId());
            busColumnRepository.save(busColumn);
        }
        return businessTable;
    }

    @Override
    public BusTable get(String id) {
        BusTable busTable= getRepository().findById(id).orElseThrow(()->new BusinessException("实例不存在:"+id));

        busTable.setColumns(busColumnRepository.findByTableId(id));
        return busTable;
    }

    /**
     * 通过数据库表面获取数据库结构
     * @param tableName
     * @return
     */
    @Override
    public BusTable getByTableName(String tableName) {
        //取得数据库表
        BusTable busTable =getRepository().findByTableName(tableName).get(0);

        busTable.setColumns(busColumnRepository.findByTableId(busTable.getId()));
        return busTable;
    }

    /**
     * 判断表是否生成
     * @param tableName
     * @return
     */
    @Override
    public boolean isTableCreated(String tableName) {
        return tableOperationMapper.findTableCountByTableName(tableName) >0;
    }


    /**
     * 创建表
     * @param busTable
     */
    @Override
    public void createTable(BusTable busTable) {
        if (this.isTableCreated(busTable.getTableName())) {
            log.debug("表[{}({})]已存在数据库中，无需再次生成",busTable.getTableName(),busTable.getTableComment());
            return;
        }

        log.info("开始创建表：" + busTable.getTableName());
        // 建表语句
        TableSql tableSql= getTableSql(busTable);

        tableOperationMapper.createTable(tableSql);

        log.info("完成创建表：" + busTable.getTableName());
    }

    @Override
    public void dropTable(String tableName) {
        tableOperationMapper.dorpTableByName(tableName);
    }


    /**
     * 构造执行的SQL
     *
     * @param busTable
     * @return
     */
    private TableSql getTableSql(BusTable busTable) {
        TableSql tableSql = new TableSql();
        tableSql.setName(busTable.getTableName());
        List<String> primaryKeys =new ArrayList<>();
        primaryKeys.add(busTable.getPkColumn().getFieldName());

        List<ColumnSql> columnSqls = new ArrayList<ColumnSql>();
        for (BusColumn bizcolumn : busTable.getColumns()) {
            columnSqls.add(new ColumnSql(bizcolumn,primaryKeys));
        }
        tableSql.setPrimaryKey(StringUtils.join(primaryKeys,","));

        tableSql.setComment(busTable.getTableComment());
        tableSql.setColumnSqls(columnSqls);
        return tableSql;
    }

}
