package com.flowablewrapper.bean.db;

import java.util.Date;

public interface ICreateInfoModel {
    Date getCreateTime();

    void setCreateTime(Date var1);

    String getCreateBy();

    void setCreateBy(String var1);

    Date getUpdateTime();

    void setUpdateTime(Date var1);

    String getUpdateBy();

    void setUpdateBy(String var1);
}
