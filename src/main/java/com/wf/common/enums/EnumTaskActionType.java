package com.wf.common.enums;

/**
 * 审批动作类型
 */
public enum EnumTaskActionType  {
    AGREE("agree", "同意", "taskAgreeActionHandler"),
    CREATE("create", "创建时", "null"),
    DISPENDSE("dispense", "分发", "null"),
    DRAFT("draft", "保存草稿", "instanceSaveActionHandler"),
    FLOWIMAGE("flowImage", "流程图", "instanceImageActionHandler"),
    LOCK("lock", "锁定", "taskLockActionHandler"),
    MANUALEND("manualEnd", "人工终止", "instanceManualEndActionHandler"),
    OPPOSE("oppose", "反对", "taskOpposeActionHandler"),
    PRINT("print", "打印", "instancePrintActionHandler"),
    RECOVER("recover", "撤销", "null"),
    REJECT("reject", "驳回", "taskRejectActionHandler"),
    REJECT2START("reject2Start", "驳回发起人", "taskReject2StartActionHandler"),
    SAVE("save", "保存", "taskSaveActionHandler"),

    START("start", "启动", "instanceStartActionHandler"),
    TASKOPINION("taskOpinion", "审批历史", "instanceTaskOpinionActionHandler"),

    UNLOCK("unlock", "解锁", "taskUnlockActionHandler");

    // 键
    private String key = "";
    // 值
    private String name = "";

    private String beanId = "";

    // 构造方法
    private EnumTaskActionType(String key, String name, String beanId) {
        this.key = key;
        this.name = name;
        this.beanId = beanId;
    }

    // =====getting and setting=====
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setName(String value) {
        this.name = value;
    }

    @Override
    public String toString() {
        return key;
    }

    /**
     * 通过key获取对象
     *
     * @param key
     * @return
     */
    public static EnumTaskActionType fromKey(String key) {
        for (EnumTaskActionType c : EnumTaskActionType.values()) {
            if (c.getKey().equalsIgnoreCase(key))
                return c;
        }
        return null;
        //throw new BusinessException(BpmStatusCode.NO_TASK_ACTION);
    }
}
