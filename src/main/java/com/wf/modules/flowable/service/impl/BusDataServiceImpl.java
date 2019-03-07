package com.wf.modules.flowable.service.impl;

import cn.hutool.json.JSONObject;
import com.wf.common.utils.SnowFlakeUtil;
import com.wf.modules.flowable.dao.mapper.NormalBizMapper;
import com.wf.modules.flowable.entity.BusColumn;
import com.wf.modules.flowable.entity.BusData;
import com.wf.modules.flowable.entity.BusTable;
import com.wf.modules.flowable.service.BusDataService;
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
    public BusData saveData(BusData busData){
        //
       if(StringUtils.isEmpty(busData.getPkValue())){
           busData.setPkValue(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()));
           normalBizMapper.insertBiz(busData);
//           normalBizMapper.insertBiz(busData.getBusTable().getTableName(),busData.getData());
       }else{
           normalBizMapper.updateBizByPk(busData);
       }

       return busData;
    }

}
