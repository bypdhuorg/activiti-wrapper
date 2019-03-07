package com.wf.common.constant;

import java.util.Arrays;

/**
 * 描述：Column中的type枚举
 */
public enum EnumColumnType {
    /**
     * 字符串
     */
    VARCHAR("varchar", "字符串","varchar", new String[] { "varchar", "varchar2", "char", "tinyblob", "tinytext" }),
    /**
     * 大文本
     */
    CLOB("clob", "大文本", "text",new String[] { "text", "clob", "blob", "mediumblob", "mediumtext", "longblob", "longtext" }),
    /**
     * 数字型
     */
    NUMBER("number", "数字型","decimal", new String[] { "tinyint", "number", "smallint", "mediumint", "int", "integer", "bigint", "float", "double", "decimal", "numeric" }),
    LONG("long", "数字型","bigint", new String[] { "bigint"}),

    /**
     * 日期型
     */
    DATE("date", "日期型","datetime", new String[] { "date", "time", "year", "datetime", "timestamp" });
    /**
     * key
     */
    private String key;
    /**
     * 描述
     */
    private String desc;
    /**
     * mysqltype
     */
    private String mysqltype;
    /**
     * 支持的数据库类型
     */
    private String[] supports;

    private EnumColumnType(String key, String desc,String mysqltype, String[] supports) {
        this.key = key;
        this.desc = desc;
        this.mysqltype=mysqltype;
        this.supports = supports;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public String getMysqltype() {
        return mysqltype;
    }


    public String[] getSupports() {
        return supports;
    }

    /**
     * <pre>
     * 根据key来判断是否跟当前一致
     * </pre>
     *
     * @param key
     * @return
     */
    public boolean equalsWithKey(String key) {
        return this.key.equals(key);
    }

    public static EnumColumnType getByKey(String key) {
        for (EnumColumnType type : EnumColumnType.values()) {
            if(type.getKey().equals(key)) {
                return type;
            }
        }
        throw null;
    }

    /**
     * <pre>
     * 根据数据库的字段类型获取type
     * 无视大小写
     * </pre>
     *
     * @param dbDataType
     *            数据库的字段类型
     * @return
     */
    public static EnumColumnType getByDbDataType(String dbDataType) {
        for (EnumColumnType type : EnumColumnType.values()) {
            for(String support: Arrays.asList(type.supports)) {
                if(dbDataType.toLowerCase().contains(support.toLowerCase())) {
                    return type;
                }
            }

        }

        return null;
    }

}
