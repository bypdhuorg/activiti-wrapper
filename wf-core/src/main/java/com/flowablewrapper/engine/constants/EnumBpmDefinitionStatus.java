package com.flowablewrapper.engine.constants;

/**
 * 流程状态
 */
public enum EnumBpmDefinitionStatus {

    DRAFT("draft", "草稿"),
    DEPLOY("deploy", "发布"),
    FORBIDDEN("forbidden", "禁用");

    // 键
    private String key = "";
    // 值
    private String name = "";

    // 构造方法
    private EnumBpmDefinitionStatus(String key, String name) {
        this.key = key;
        this.name = name;
    }

    // =====getting and setting=====
    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
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
    public static EnumBpmDefinitionStatus fromKey(String key) {
        for (EnumBpmDefinitionStatus c : EnumBpmDefinitionStatus.values()) {
            if (c.getKey().equalsIgnoreCase(key)){
                return c;
            }
        }
        return null;
    }
}
