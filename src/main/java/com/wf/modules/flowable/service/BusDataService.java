package com.wf.modules.flowable.service;

import cn.hutool.json.JSONObject;
import com.wf.modules.flowable.entity.BusData;
import com.wf.modules.flowable.entity.BusTable;

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
    public BusData saveData(BusData busData);
}
