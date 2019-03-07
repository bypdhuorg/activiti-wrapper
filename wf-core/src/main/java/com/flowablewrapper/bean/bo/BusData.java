package com.flowablewrapper.bean.bo;

import com.flowablewrapper.bean.db.BusTable;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


public class BusData {
    /**
     * 数据库表 结构
     */
    @Getter @Setter
    private BusTable busTable;

    /**
     * 数据本身
     * Map<columnKey,值>
     */
    @Getter
    private Map<String, Object> data=new HashMap<>();

    /**
     * 设置PK
     * @param id
     */
    public void setPkValue(String id) {
        this.data.put(busTable.getPkColumn().getFieldName(), id);
    }

    /**
     * 取得PK
     * @return
     */
    public String getPkValue() {
        return (String)this.data.get(busTable.getPkColumn().getFieldName());
    }

    /**
     * 设置数据
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        data.put(key, value);
    }


    /**
     *取得数据
     * @param key
     * @return
     */
    public Object get(String key) {
        return this.data.get(key);
    }
}
