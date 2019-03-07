package com.wf.common.enums;

/**
 * 流程状态
 * @author meibo
 */
public enum EnumBpmDefinitionStatus {

    /**
     * 激活
     */
    ACTIVE("激活"),
    /**
     * 挂起
     */
    FORBIDDEN("挂起");

    // 值
    private String name = "";

    // 构造方法
    private EnumBpmDefinitionStatus( String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


}
