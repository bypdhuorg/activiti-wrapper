package com.flowablewrapper.services.db.impl;

import com.alibaba.fastjson.JSONObject;
import com.flowablewrapper.bean.bo.BusData;
import com.flowablewrapper.bean.db.BusColumn;
import com.flowablewrapper.bean.db.BusTable;
import com.flowablewrapper.common.utils.SnowFlakeUtils;
import com.flowablewrapper.dao.biz.NormalBizMapper;
import com.flowablewrapper.services.db.BusDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


/**
 * 业务库 操作实现
 * @author meibo
 */
@Service
@Slf4j
@Transactional
public class BusDataServiceImpl implements BusDataService {

    @Autowired
    private NormalBizMapper normalBizMapper;

    /**
     * 分析Json数据
     * @param busTable
     * @param initData
     * @return
     */
    @Override
    public BusData parseJsonDataToBusData(BusTable busTable, JSONObject initData){
        //column data
        BusData busData=new BusData();

        busData.setBusTable(busTable);

        Map<String, Object> dbData=new HashMap<>();
        //根据数据库字段，从JSONObject中取得数据
        for (BusColumn column : busTable.getColumns()) {
            busData.put(column.getFieldName(),initData.get(column.getFieldName()));
        }

        return busData;
    }

    /**
     * 保存业务数据
     * @param busData
     */
    @Override
    public void saveData(BusData busData){
        //
       if(StringUtils.isEmpty(busData.getPkValue())){
           busData.setPkValue(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
           normalBizMapper.insertBiz(busData);
       }else{
           normalBizMapper.updateBizByPk(busData);
       }
    }

}
