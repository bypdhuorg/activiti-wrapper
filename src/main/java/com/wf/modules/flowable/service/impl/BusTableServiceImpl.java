package com.wf.modules.flowable.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.common.exception.UserClientException;
import com.wf.common.table.ColumnSql;
import com.wf.common.table.TableSql;
import com.wf.modules.flowable.dao.BusColumnDao;
import com.wf.modules.flowable.dao.BusTableDao;
import com.wf.modules.flowable.dao.mapper.TableOperationMapper;
import com.wf.modules.flowable.entity.BusColumn;
import com.wf.modules.flowable.entity.BusTable;
import com.wf.modules.flowable.service.BusTableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务表定义接口实现
 * @author meibo
 */
@Slf4j
@Service
@Transactional
public class BusTableServiceImpl implements BusTableService {

    @Autowired
    private BusTableDao busTableDao;


    @Autowired
    private BusColumnDao busColumnDao;

    /**
     * 表 操作
     */
    @Autowired
    private TableOperationMapper tableOperationMapper;

    @Override
    public BusTableDao getRepository() {
        return busTableDao;
    }

    @Override
    public void saveCheck(BusTable busTable){
        if(getRepository().findByTableName(busTable.getTableName()) !=null){
            throw new UserClientException("业务表名已经存在");
        }
    };
    @Override
    public void updateCheck(BusTable busTable){
        //check 对象存在
        getRepository().findById(busTable.getId())
                .orElseThrow(()->new UserClientException("业务表不存在"));
        //check 表名
        if(getRepository().findByTableNameAndIdNot(busTable.getTableName(),busTable.getId()) !=null){
            throw new UserClientException("业务表名已经存在");
        }
    };
    @Override
    public void deleteCheck(String id){};

    @Override
    public BusTable save(BusTable businessTable){
        saveCheck(businessTable);
        //保存表
        getRepository().save(businessTable);

        //删除字段
        busColumnDao.deleteByTableId(businessTable.getId());

        //添加字段
        for (BusColumn busColumn :businessTable.getColumns()){
            busColumn.setTableId(businessTable.getId());
            busColumnDao.save(busColumn);
        }
        return businessTable;
    }

    @Override
    public BusTable update(BusTable businessTable){
        updateCheck(businessTable);
        //保存表
        getRepository().saveAndFlush(businessTable);

        //删除字段
        busColumnDao.deleteByTableId(businessTable.getId());

        //添加字段
        for (BusColumn busColumn :businessTable.getColumns()){
            busColumn.setTableId(businessTable.getId());
            busColumnDao.save(busColumn);
        }
        createTable(businessTable);
        return businessTable;
    }

    @Override
    public void delete(String id) {
        deleteCheck(id);
        //删除字段
        busColumnDao.deleteByTableId(id);
        //删除表
        BusTable businessTable =this.get(id);
        if(businessTable!=null){
            getRepository().delete(businessTable);
        }
    }

    @Override
    /**
     * 多条件分页获取(业务表定义)
     * @param busTable
     * @param searchVo
     * @param pageable
     * @return
     */
    public Page<BusTable> findByCondition(BusTable busTable, SearchTimeVo searchVo, Pageable pageable) {
        return getRepository().findAll(new Specification<BusTable>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<BusTable> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                Path<Date> createTimeField=root.get("createTime");

                List<Predicate> list = new ArrayList<Predicate>();

                //创建时间
                if(searchVo.getStartDate()!=null){
                    Date start = DateUtil.date(searchVo.getStartDate());
                    list.add(cb.greaterThanOrEqualTo(createTimeField, start));
                }
                if(searchVo.getEndDate()!=null){
                    Date end = DateUtil.date(searchVo.getEndDate());
                    list.add(cb.lessThanOrEqualTo(createTimeField, end));
                }

                Predicate[] arr = new Predicate[list.size()];
                cq.where(list.toArray(arr));
                return null;
            }
        }, pageable);
    }

    /**
     * 通过数据库表面获取数据库结构
     * @param tableName
     * @return
     */
    @Override
    public BusTable getByTableName(String tableName) {
        //取得数据库表
        BusTable busTable =getRepository().findByTableName(tableName);

        busTable.setColumns(busColumnDao.findByTableId(busTable.getId()));
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