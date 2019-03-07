package com.wf.common.enums;

/**
 * 流程实例状态
 * @author meibo
 */
public enum EnumInstanceStatus {
    /**
     *
     */
    DRAFT("草稿"),
    RUNNING("处理中"),
    END("结束"),
    SUSPEND( "挂起");

    // 值
    private String name = "";

    // 构造方法
    private EnumInstanceStatus( String name) {
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
