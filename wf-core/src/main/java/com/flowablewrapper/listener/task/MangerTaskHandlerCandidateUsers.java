package com.flowablewrapper.listener.task;

import com.flowablewrapper.common.utils.Constants;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.Arrays;

/**
 * 监听器 动态分配用户实例
 */
public class MangerTaskHandlerCandidateUsers implements TaskListener {

    private static final long serialVersionUID = 8513750196548027535L;

    @Override
    public void notify(DelegateTask delegateTask) {

        delegateTask.getVariable(Constants.TASK_APPLY_USER_ID);

        String[] empLoyees = {"aa","bb","cc"};
        delegateTask.addCandidateUsers(Arrays.asList(empLoyees));

    }
}
