package com.flowablewrapper.services.db;

import com.alibaba.fastjson.JSONObject;
import com.flowablewrapper.bean.bo.BusData;
import com.flowablewrapper.bean.db.BusTable;

/**
 * 业务 处理接口
 * @author meibo
 */
public interface BusDataService {
    /**
     * 分析Json数据
     * @param busTable
     * @param initData
     * @return
     */
    public BusData parseJsonDataToBusData(BusTable busTable, JSONObject initData);


    /**
     * 保存业务数据
     * @param busData
     */
    public void saveData(BusData busData);
}
