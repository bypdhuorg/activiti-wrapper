package com.flowablewrapper.engine.action.cmd;

import com.flowablewrapper.bean.db.BpmInstance;

public interface IInstanceActionCmd extends IActionCmd {
    String getSubject();

//    String getBusinessKey();

    String getInstanceId();

    BpmInstance getBpmInstance();
}
