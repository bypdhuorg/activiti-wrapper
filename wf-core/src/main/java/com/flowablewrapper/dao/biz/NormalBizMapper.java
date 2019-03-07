package com.flowablewrapper.dao.biz;

import com.flowablewrapper.bean.bo.BusData;
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
    @SelectProvider(type=com.flowablewrapper.dao.biz.NormalBizDynaSqlProvider.class,method="selectWhitParamSql")
    List<Map<String,Object>> selectWithParam(Map<String,Object> param);


    /**
     * 动态插入
     * @param busData
     * @return
     */
    @InsertProvider(type=com.flowablewrapper.dao.biz.NormalBizDynaSqlProvider.class,method="insertBiz")
    int insertBiz(BusData busData);


    /**
     *动态更新
     * @param busData
     * @return
     */
    @UpdateProvider(type=com.flowablewrapper.dao.biz.NormalBizDynaSqlProvider.class,method="updateBizByPk")
    int updateBizByPk(BusData busData);


    /**
     * 动态删除
     * @param busData
     */
    @DeleteProvider(type=com.flowablewrapper.dao.biz.NormalBizDynaSqlProvider.class,method="deleteBizByPk")
    void deleteBizByPk(BusData busData);
}
