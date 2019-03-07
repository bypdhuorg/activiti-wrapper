package com.flowablewrapper.engine.action.handle.instance;

import com.flowablewrapper.bean.db.BpmDefinition;
import com.flowablewrapper.bean.db.BpmInstance;
import com.flowablewrapper.engine.action.cmd.DefaultInstanceActionCmd;
import com.flowablewrapper.engine.action.handle.AbsActionHandler;
import com.flowablewrapper.engine.constants.EnumInstanceStatus;
import com.flowablewrapper.engine.constants.EnumTaskActionType;
import com.flowablewrapper.exception.BusinessException;
import com.flowablewrapper.services.flowableable.WfProcessTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class InstanceSaveActionHandler extends AbsActionHandler<DefaultInstanceActionCmd> {
    @Autowired
    WfProcessTaskService wfProcessTaskService;

    @Override
    protected void doPre(DefaultInstanceActionCmd intanceCmdData) {
        //设置 流程定义
        intanceCmdData.setBpmDefinition(
                this.bpmDefinitionRepository.findById(intanceCmdData.getDefId())
                        .orElseThrow(() -> new BusinessException("流程定义不存在！："+intanceCmdData.getDefId())));

        //设置 流程实例
        setInstance(intanceCmdData);
    }


    @Override
    protected void doAction(DefaultInstanceActionCmd actionModel) {
        BpmInstance instance = actionModel.getBpmInstance();

        instance.setStatus(EnumInstanceStatus.STATUS_DRAFT);

        this.saveInstance(instance);
    }

    /**
     * 保存流程实例
     * @param instance
     */
    protected void saveInstance(BpmInstance instance){
        this.bpmInstanceRepository.save(instance);
    }

    @Override
    protected void doPost(DefaultInstanceActionCmd model) {

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


    /**
     * 流程实例设置
     * @param intanceCmdData
     */
    private void setInstance(DefaultInstanceActionCmd intanceCmdData){
        String instId = intanceCmdData.getInstanceId();
        BpmInstance instance = null;

        //根据流程ID，从数据库中查询出流程详情
        if(StringUtils.isNotEmpty(instId) ){
            instance =this.bpmInstanceRepository.findById(instId)
                    .orElseThrow(()->new BusinessException("流程实例不存在:"+instId));
            if(StringUtils.isNotEmpty(instance.getActInstId())){
                throw new BusinessException("草稿已经启动，不能再保存或者启动！");
            }
        }

        if(instance==null){
            BpmDefinition bpmDefinition = intanceCmdData.getBpmDefinition();

            instance = new BpmInstance();
            instance.setSubject(bpmDefinition.getName());

            instance.setDefId(bpmDefinition.getId());
            instance.setDefKey(bpmDefinition.getKey());
            instance.setActDefId(bpmDefinition.getActDefId());
            instance.setDefName(bpmDefinition.getName());
        }

        intanceCmdData.setBpmInstance(instance);
    }
}
