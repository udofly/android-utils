package com.udofly.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json 解析
 */
public class JsonUtils {

    private static JsonUtils instance;

    private JsonUtils() {
    }

    public static JsonUtils get() {
        if (instance == null) {
            synchronized (JsonUtils.class) {
                if (instance == null) {
                    instance = new JsonUtils();
                }
            }
        }
        return instance;
    }


    /**
     * JSONArray 转化
     *
     * @return
     */
    public static JSONArray parseJsonArray(String str_array) {


        if (StringUtil.isNull(str_array)) {
            return new JSONArray();
        }

        try {
            JSONArray array = JSONArray.parseArray(str_array);
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }

    }

    /**
     * JSONArray 转化
     *
     * @return
     */
    public static JSONObject parseJsonObject(String str_ob) {


        if (StringUtil.isNull(str_ob)) {
            return new JSONObject();
        }

        try {
            JSONObject ob = JSONObject.parseObject(str_ob);
            return ob;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }

    }

    /**
     * 取值 JSONArray 空指针判断
     *
     * @param object
     * @param key
     * @return
     */
    public static JSONArray getJsonArray(JSONObject object, String key) {

        if (object == null) {
            return new JSONArray();
        }

        if (object.containsKey(key) && object.get(key) instanceof JSONArray) {
            return object.getJSONArray(key) == null ? new JSONArray() : object.getJSONArray(key);
        }
        return new JSONArray();
    }

    /**
     * 取值 JSONArray 空指针判断
     *
     * @param array
     * @param index
     * @return
     */
    public static JSONArray getJsonArray(JSONArray array, int index) {

        if (index < 0 || array == null || array.size() == 0 || array.size() <= index) {
            return new JSONArray();
        }
        return array.getJSONArray(index);
    }

    /**
     * 取值 JSONObject 空指针判断
     *
     * @param object
     * @param key
     * @return
     */
    public static JSONObject getJsonObject(JSONObject object, String key) {

        if (object == null) {
            return new JSONObject();
        }
        if (object.containsKey(key) && object.get(key) instanceof JSONObject) {
            return object.getJSONObject(key) == null ? new JSONObject() : object.getJSONObject(key);
        }
        return new JSONObject();
    }

    /**
     * 取值 JSONObject 空指针判断
     *
     * @param array
     * @param index
     * @return
     */
    public static JSONObject getJsonObject(JSONArray array, int index) {

        if (index < 0 || array == null || array.size() == 0 || array.size() <= index) {
            return new JSONObject();
        }
        return array.getJSONObject(index);
    }

    /**
     * 取值 String 空指针判断
     *
     * @param object
     * @param key
     * @return
     */
    public static String getJsonString(JSONObject object, String key) {

        if (object == null) {
            return "";
        }
        if (object.containsKey(key)) {
            if (object.get(key) instanceof String) {
                return StringUtil.checkStringNull(object.getString(key));
            } else if (object.get(key) instanceof Integer) {
                return object.getInteger(key) + "";
            } else if (object.get(key) instanceof Boolean) {
                return object.getBoolean(key) + "";
            } else if (object.get(key) != null) {
                return String.valueOf(object.get(key));
            }
        }
        return "";
    }

    /**
     * 取值 String 空指针判断
     *
     * @param list     字符串数组
     * @param position 0 1  2   3  index
     * @return
     */

    public static String getJsonString(JSONArray list, int position) {
        if (list == null || list.size() <= position) {
            return "";
        }
        String str = "";
        try {
            Object object = list.get(position);
            str = object.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 取值 String 空指针判断
     *
     * @param object
     * @param key
     * @return
     */
    public static boolean getJsonBoolean(JSONObject object, String key, boolean def) {

        if (object == null) {
            return def;
        }
        if (object.containsKey(key) && object.get(key) instanceof Boolean) {
            return object.getBoolean(key);
        }
        return def;
    }

    /**
     * 取值 int 空指针判断
     *
     * @param object
     * @param key
     * @return
     */
    public static int getJsonInteger(JSONObject object, String key) {
        return getJsonInteger(object, key, 0);
    }

    public static int getJsonInteger(JSONObject object, String key, int def) {

        if (object == null) {
            return def;
        }
        if (object.containsKey(key)) {
            if (object.get(key) instanceof Integer) {
                return object.getInteger(key);
            } else if (object.get(key) instanceof String) {
                return getInteger(object.getString(key), def);
            } else if (object.get(key) != null) {
                return getInteger(String.valueOf(object.get(key)), def);
            }
        }
        return def;
    }

    /**
     * 获取int数值
     */
    public static int getInteger(String value, int def) {
        int number = def;
        if (StringUtil.isNull(value)) {
            return number;
        }
        //去掉所用空格
        if (value.contains(".")) {
            number = (int) getDouble(value);
        } else {
            String str = value;
            if (value.contains(",")) {
                str = value.replace(",", "");
            } else if (value.contains("，")) {
                str = value.replace("，", "");
            }
            try {
                number = Integer.parseInt(str);
            } catch (Exception e) {
            }
        }
        return number;
    }


    /**
     * 获取double数值
     */
    public static double getDouble(String value) {
        double number = 0;
        if (StringUtil.isNull(value)) {
            return number;
        }
        String str = value;
        //去掉所用空格
        if (value.contains(",")) {
            str = value.replace(",", "");
        } else if (value.contains("，")) {
            str = value.replace("，", "");
        }
        try {
            number = Double.parseDouble(str);
        } catch (Exception e) {
        }
        return number;
    }

    /**
     * 获取double数值
     */
    public static Long getLong(String value) {
        long number = 0;
        if (StringUtil.isNull(value)) {
            return number;
        }
        String str = value;
        //去掉所用空格
        if (value.contains(",")) {
            str = value.replace(",", "");
        } else if (value.contains("，")) {
            str = value.replace("，", "");
        }
        try {
            number = Long.parseLong(str);
        } catch (Exception e) {
        }
        return number;
    }

    /**
     * 获取double数值
     */
    public static float getFloat(String value) {
        float number = 0;
        if (StringUtil.isNull(value)) {
            return number;
        }
        String str = value;
        //去掉所用空格
        if (value.contains(",")) {
            str = value.replace(",", "");
        } else if (value.contains("，")) {
            str = value.replace("，", "");
        }
        try {
            number = Float.parseFloat(str);
        } catch (Exception e) {
        }
        return number;
    }


    public static String getTwoDouble(double value) {
        if (value == 0) {
            return "0.00";
        }
        String text = new java.text.DecimalFormat("#.00").format(value);
        if (value < 1) {
            return "0" + text;
        }
        return text;
    }

    public static boolean isObjectNull(JSONObject object) {
        return (object == null || object.size() == 0);
    }

    public static boolean isListNull(JSONArray list) {
        return (list == null || list.size() == 0);
    }


}