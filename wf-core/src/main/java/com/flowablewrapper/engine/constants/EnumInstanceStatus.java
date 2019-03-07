package com.flowablewrapper.engine.constants;

public enum EnumInstanceStatus {
    STATUS_DRAFT("draft", "草稿"),
    STATUS_RUNNING("running", "运行中"),
    STATUS_END("end", "结束"),
    STATUS_MANUAL_END("manualend", "人工结束"),
    STATUS_BACK("back", "驳回"),
    STATUS_UNDEFINED("undefined", "未定义"),
    STATUS_REVOKE("revoke", "撤销");

    private String key = "";
    private String value = "";

    private EnumInstanceStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return this.key;
    }

    public static EnumInstanceStatus fromKey(String key) {
        EnumInstanceStatus[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            EnumInstanceStatus c = var1[var3];
            if (c.getKey().equalsIgnoreCase(key)) {
                return c;
            }
        }

        throw new IllegalArgumentException(key);
    }

    public static EnumInstanceStatus getByActionName(String actionName) {
        EnumTaskActionType action = EnumTaskActionType.fromKey(actionName);
        switch(action) {
            case AGREE:
                return STATUS_RUNNING;
            case OPPOSE:
                return STATUS_RUNNING;
            case REJECT:
                return STATUS_BACK;
            case REJECT2START:
                return STATUS_BACK;
            case RECOVER:
                return STATUS_REVOKE;
            case TASKOPINION:
                return STATUS_RUNNING;
            case MANUALEND:
                return STATUS_MANUAL_END;
            default:
                return STATUS_RUNNING;
        }
    }
}
