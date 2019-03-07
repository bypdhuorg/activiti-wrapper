package com.flowablewrapper.engine.action.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flowablewrapper.bean.bo.BusData;
import com.flowablewrapper.bean.db.BpmDefinition;
import com.flowablewrapper.bean.db.BpmInstance;
import com.flowablewrapper.bean.db.BusTable;
import com.flowablewrapper.dao.BpmDefinitionRepository;
import com.flowablewrapper.dao.BpmInstanceRepository;
import com.flowablewrapper.engine.action.cmd.BaseActionCmd;
import com.flowablewrapper.engine.constants.EnumBpmDefinitionStatus;
import com.flowablewrapper.engine.context.BpmContext;
import com.flowablewrapper.exception.StatusCode;
import com.flowablewrapper.exception.WorkFlowException;
import com.flowablewrapper.services.db.BusDataService;
import com.flowablewrapper.services.db.BusTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
public abstract class AbsActionHandler<T extends BaseActionCmd>
        implements IActionHandler<T> {

    /**
     * 流程定义 DAO
     */
    @Resource
    protected BpmDefinitionRepository bpmDefinitionRepository;

    /**
     * 流程实例 DAO
     */
    @Resource
    protected BpmInstanceRepository bpmInstanceRepository;

    /**
     * 业务数据处理
     */
    @Resource
    protected BusTableService busTableService;

    /**
     * 业务数据处理
     */
    @Resource
    protected BusDataService busDataService;

    public AbsActionHandler(){

    }

    protected abstract void doPre(T model);
    protected abstract void doAction(T model);
    protected abstract void doPost(T model);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void execute(T model){
        this.doPre(model);
        this.doCheck(model);
        BpmContext.setActionModel(model);

        //业务数据处理
        this.doAction(model);

        //业务数据处理
        this.setBusData(model);

        BpmContext.removeActionModel();
        this.doPost(model);
    }

    /**
     * 业务数据处理
     * @param actionModel
     */
    protected void setBusData(BaseActionCmd actionModel){
        //流程实例
        BpmInstance instance = actionModel.getBpmInstance();
        BpmDefinition bpmDefinition = actionModel.getBpmDefinition();

        JSONObject data = JSON.parseObject(actionModel.getBusData());
        //取得对应的业务表
        String modelCode = bpmDefinition.getBunissnessTable();
        // 提交的数据中包换，做映射
        if (data.containsKey(modelCode)) {
            JSONObject bizData =  data.getJSONObject(modelCode);

            BusTable busTable=busTableService.getByTableName(modelCode);
            BusData busData =busDataService.parseJsonDataToBusData(busTable,bizData);
            actionModel.setBusDataObj(busData);

            busDataService.saveData(busData);

        }

    }

    /**
     * check
     * @param actionModel
     */
    protected void doCheck(BaseActionCmd actionModel){
        //流程实例check
        BpmInstance instance = actionModel.getBpmInstance();
        if (instance.getIsForbidden()) {
            throw new WorkFlowException("流程实例已经被禁止，请联系管理员", StatusCode.DEF_FORBIDDEN);
        }

        //流程定义check
        BpmDefinition bpmDefinition =this.bpmDefinitionRepository.getOne(instance.getDefId());

        if (EnumBpmDefinitionStatus.FORBIDDEN.equals(bpmDefinition.getStatus())) {
            throw new WorkFlowException("流程定义已经被禁用，请联系管理员", StatusCode.DEF_FORBIDDEN);
        }
        actionModel.setBpmDefinition(bpmDefinition);
        //TODO 可以添加权限相关认证Check

    };


}
