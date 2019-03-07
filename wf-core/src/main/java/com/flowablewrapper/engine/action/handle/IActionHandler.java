package com.flowablewrapper.engine.action.handle;

import com.flowablewrapper.engine.action.cmd.IActionCmd;
import com.flowablewrapper.engine.constants.EnumTaskActionType;

/**
 * 动作执行处理器
 * @param <T>
 */
public interface IActionHandler<T extends IActionCmd> {

    /**
     * 执行入口
     *
     * @param model
     */
    void execute(T model);


    /**
     * 定义动作的key name beanId
     *
     * @return
     */
    EnumTaskActionType getActionType();
}
