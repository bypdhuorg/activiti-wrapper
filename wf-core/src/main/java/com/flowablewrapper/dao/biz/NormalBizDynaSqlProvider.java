package com.flowablewrapper.dao.biz;

import com.flowablewrapper.bean.bo.BusData;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class NormalBizDynaSqlProvider {
    /**
     * 法中的关键字是区分大小写的  SQL SELECT WHERE
     * 该方法会根据传递过来的map中的参数内容  动态构建sql语句
     * @param param
     * @return
     */
    public String selectWhitParamSql(Map<String, Object> param) {
        return new SQL() {
            {
                SELECT("*");
                FROM("tb_employee");
                if (param.get("id")!=null) {
                    WHERE("id=#{id}");
                }
            }

        }.toString();
    }

    /**
     * 法中的关键字是区分大小写的  SQL SELECT WHERE
     * 该方法会根据传递过来的map中的参数内容  动态构建sql语句
     * @param busData
     * @return
     */
    public String insertBiz(BusData busData) {
        //表名
        String tableName=busData.getBusTable().getTableName();
        Map<String, Object> map=busData.getData();
        return new SQL() {
            {
                INSERT_INTO(tableName);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    VALUES(entry.getKey(),entry.getValue().toString());
                }
            }

        }.toString();
    }

    /**
     * update
     * @param busData
     * @return
     */
    public String updateBizByPk(BusData busData) {
        //表名
        String tableName=busData.getBusTable().getTableName();
        //PK 字段名
        String pkColumn=busData.getBusTable().getPkColumn().getFieldName();
        Map<String, Object> map=busData.getData();
        return new SQL() {
            {
                UPDATE(tableName);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    // 主键跳过
                    if(entry.getKey().equals(pkColumn)){
                        continue;
                    }else{
                        VALUES(entry.getKey(),entry.getValue().toString());
                    }
                }

                WHERE(pkColumn+"='"+busData.getPkValue()+"'" );
            }

        }.toString();
    }

    /**
     * update
     * @param busData
     * @return
     */
    public String deleteBizByPk(BusData busData) {
        //表名
        String tableName=busData.getBusTable().getTableName();
        //PK 字段名
        String pkColumn=busData.getBusTable().getPkColumn().getFieldName();

        return new SQL() {
            {
                DELETE_FROM(tableName);

                WHERE(pkColumn+"='"+busData.getPkValue()+"'" );
            }

        }.toString();
    }

}
