package com.flowablewrapper.engine.action.cmd;

import com.flowablewrapper.engine.constants.EnumTaskActionType;

public interface ITaskActionCmd {


    /**
     * 动作类型。
     *
     * @return ActionType
     */
    EnumTaskActionType getActionType();

    /**
     * 获取任务ID
     *
     * @return
     */
    String getTaskId();

    String getComment();
}
