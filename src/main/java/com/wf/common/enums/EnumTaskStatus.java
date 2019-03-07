package com.wf.common.enums;

/**
 * 流程实例 结果
 * @author meibo
 */
public enum EnumTaskStatus {
    /**
     *
     */
    NORMAL("普通"),
    SUSPEND("SUSPEND"),
    LOCK("锁定"),
    AGENCY( "代理"),
    BACK("驳回"),
    DESIGNATE("指派");

    private String value = "";

    // 值
    private String name = "";

    // 构造方法
    private EnumTaskStatus(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }



//    public static EnumInstanceStatus getByActionName(String actionName) {
//        EnumTaskActionType action = EnumTaskActionType.fromKey(actionName);
//        switch(action) {
//            case AGREE:
//                return STATUS_RUNNING;
//            case OPPOSE:
//                return STATUS_RUNNING;
//            case REJECT:
//                return STATUS_BACK;
//            case REJECT2START:
//                return STATUS_BACK;
//            case RECOVER:
//                return STATUS_REVOKE;
//            case TASKOPINION:
//                return STATUS_RUNNING;
//            case MANUALEND:
//                return STATUS_MANUAL_END;
//            default:
//                return STATUS_RUNNING;
//        }
//    }
}
