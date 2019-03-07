package com.flowablewrapper.engine.action.handle.instance;

import com.flowablewrapper.bean.db.BpmInstance;
import com.flowablewrapper.engine.action.cmd.DefaultInstanceActionCmd;
import com.flowablewrapper.engine.constants.EnumInstanceStatus;
import com.flowablewrapper.engine.constants.EnumTaskActionType;
import com.flowablewrapper.services.flowableable.WfProcessTaskService;
import org.springframework.beans.factory.annotation.Autowired;

public class InstanceStartActionHandler extends InstanceSaveActionHandler {
    @Autowired
    WfProcessTaskService wfProcessTaskService;


    @Override
    protected void doAction(DefaultInstanceActionCmd actionModel) {
        BpmInstance instance = actionModel.getBpmInstance();

//        this.wfProcessTaskService.start();

        instance.setStatus(EnumInstanceStatus.STATUS_RUNNING);

        super.saveInstance(instance);
    }



    @Override
    public void execute(DefaultInstanceActionCmd startActionModel) {

//        IBpmInstance instance = startActionModel.getBpmInstance();
//
//        wfProcessTaskService.start();

    }

    @Override
    public EnumTaskActionType getActionType() {
        return EnumTaskActionType.START;
    }
}
