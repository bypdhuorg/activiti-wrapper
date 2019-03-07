package com.wf.modules.flowable.dao.mapper;

import com.wf.modules.flowable.entity.BusData;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;
import java.util.Map;

/**
 * 动态创建帮助类
 */
public interface NormalBizMapper {

    /**
     * 动态查询
     * type:指定一个类    method:使用这个类中的selectWhitParamSql方法返回的sql字符串  作为查询的语句
     * @param param
     * @return
     */
    @SelectProvider(type= NormalBizDynaSqlProvider.class,method="selectWhitParamSql")
    List<Map<String,Object>> selectWithParam(Map<String, Object> param);


    /**
     * 动态插入
     * @param busData
     * @return
     */
    @InsertProvider(type= NormalBizDynaSqlProvider.class,method="insertBiz")
    int insertBiz(BusData busData);
//    int insertBiz(String tableName,Map<String, Object> map);


    /**
     *动态更新
     * @param busData
     * @return
     */
    @UpdateProvider(type= NormalBizDynaSqlProvider.class,method="updateBizByPk")
    int updateBizByPk(BusData busData);


    /**
     * 动态删除
     * @param busData
     */
    @DeleteProvider(type= NormalBizDynaSqlProvider.class,method="deleteBizByPk")
    void deleteBizByPk(BusData busData);
}
