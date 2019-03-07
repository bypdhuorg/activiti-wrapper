package com.flowablewrapper.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * @author Administrator
 */
public class JsonUtils {

    public JsonUtils() {
    }

    public static String getString(JSONObject obj, String key, String defaultValue) {
        return !obj.containsKey(key) ? defaultValue : obj.getString(key);
    }

    public static String getString(JSONObject obj, String key) {
        return getString(obj, key, "");
    }

    public static int getInt(JSONObject obj, String key) {
        return !obj.containsKey(key) ? 0 : obj.getIntValue(key);
    }

    public static int getInt(JSONObject obj, String key, int defaultValue) {
        return !obj.containsKey(key) ? defaultValue : obj.getIntValue(key);
    }

    public static boolean getBoolean(JSONObject obj, String key) {
        return !obj.containsKey(key) ? false : obj.getBoolean(key);
    }

    public static boolean getBoolean(JSONObject obj, String key, boolean defaultValue) {
        return !obj.containsKey(key) ? defaultValue : obj.getBoolean(key);
    }

    public static boolean isNotEmptyJsonArr(String jsonArrStr) {
        return !isEmptyJsonArr(jsonArrStr);
    }

    public static boolean isEmptyJsonArr(String jsonArrStr) {
        if (StringUtils.isEmpty(jsonArrStr)) {
            return true;
        } else {
            try {
                JSONArray jsonAry = JSONArray.parseArray(jsonArrStr);
                return jsonAry.size() <= 0;
            } catch (Exception var2) {
                return true;
            }
        }
    }

    public static String escapeSpecialChar(String str) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            switch(c) {
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(c);
            }
        }

        return sb.toString();
    }

    public static void removeNull(JSONObject jsonObject) {
        Iterator var1 = jsonObject.keySet().iterator();

        while(var1.hasNext()) {
            String key = (String)var1.next();
            Object val = jsonObject.get(key);
            if (val == null) {
                jsonObject.put(key, "");
            }
        }

    }

    public static void removeNull(JSONArray jsonArray) {
        for(int i = 0; i < jsonArray.size(); ++i) {
            removeNull(jsonArray.getJSONObject(i));
        }

    }

    public static JSONObject arrayToObject(JSONArray jsonArray, String keyName) {
        JSONObject jsonObject = new JSONObject();

        for(int i = 0; i < jsonArray.size(); ++i) {
            JSONObject temp = jsonArray.getJSONObject(i);
            jsonObject.put(temp.getString(keyName), temp);
        }

        return jsonObject;
    }

    public static JSONArray objectToArray(JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();
        Iterator var2 = jsonObject.keySet().iterator();

        while(var2.hasNext()) {
            Object key = var2.next();
            jsonArray.add(jsonObject.get(key));
        }

        return jsonArray;
    }

    public static <T> T parseObject(String jsonStr, Class<T> cls) {
        return StringUtils.isEmpty(jsonStr) ? null : JSON.parseObject(jsonStr, cls);
    }

    public static <T> List<T> parseArray(String jsonStr, Class<T> cls) {
        return StringUtils.isEmpty(jsonStr) ? null : JSON.parseArray(jsonStr, cls);
    }

    public static String toJSONString(Object obj) {
        return obj == null ? null : JSON.toJSONString(obj);
    }

//    public static void main(String[] args) {
//        String str = JSON.toJSONString((Object)null);
//        System.out.println();
//    }
}

