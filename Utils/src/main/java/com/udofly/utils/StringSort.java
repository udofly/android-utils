package com.udofly.utils;


import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * describe：
 * author： Gao Chunfa.
 * time： 2017/3/10-11:01.
 */
public class StringSort {

    /**
     * 字母序排序
     *
     * @param map 数据源
     * @return 排序后的字符串
     */
    public static String sort(Map<String, String> map) {
        String   temp = null;
        int      n    = map.size();
        String[] s    = new String[n];
        s = map.keySet().toArray(s);
//遍历key
//        Set entries = map.entrySet();
//        if (entries != null) {
//            Iterator iterator = entries.iterator();
//            while (iterator.hasNext()) {
//                Map.Entry entry = (Map.Entry) iterator.next();
//                Object key = entry.getKey();
//                Object value = entry.getValue();
//            }
//        }

        for (int i = 0; i < s.length; i++) {
            for (int j = i + 1; j < s.length; j++) {
                if (compare(s[i], s[j]) == false) {
                    temp = s[i];
                    s[i] = s[j];
                    s[j] = temp;
                }
            }
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < s.length; i++) {
            if (i != s.length - 1) {
                builder.append(s[i] + "=" + map.get(s[i]) + ",");
            } else {
                builder.append(s[i] + "=" + map.get(s[i]));
            }
        }
        return builder.toString();
    }

    /**
     * 字母序排序
     *
     * @param object 数据源
     * @return 排序后的字符串
     */
    public static String sort(JSONObject object) {


        if (object == null) {
            return "";
        }

        List<String> list = new ArrayList<>();
        for (Object key : object.keySet()) {
            if (!"image".equals(key)) {
                list.add((String) key);
            }
        }

        if (list.size() == 0) {
            return "";
        }
        String temp = null;

        int      n = list.size();
        String[] s = new String[n];

        for (int i = 0; i < list.size(); i++) {
            s[i] = list.get(i);
        }

        for (int i = 0; i < s.length; i++) {
            for (int j = i + 1; j < s.length; j++) {
                if (compare(s[i], s[j]) == false) {
                    temp = s[i];
                    s[i] = s[j];
                    s[j] = temp;
                }
            }
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < s.length; i++) {
            if (i != s.length - 1) {
                builder.append(s[i] + "=" + object.getString(s[i]) + ",");
            } else {
                builder.append(s[i] + "=" + object.getString(s[i]));
            }
        }
        return builder.toString();
    }

    /**
     * 字母序比较
     *
     * @param s1 数据源
     * @param s2 数据源
     * @return 大小
     */
    static boolean compare(String s1, String s2) {
        boolean result = true;
        for (int i = 0; i < s1.length() && i < s2.length(); i++) {
            if (s1.charAt(i) > s2.charAt(i)) {
                result = false;
                break;
            } else if (s1.charAt(i) < s2.charAt(i)) {
                result = true;
                break;
            } else {
                if (s1.length() < s2.length()) {
                    result = true;
                } else {
                    result = false;
                }
            }
        }
        return result;

    }

}



