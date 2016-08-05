/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangpang.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zhengby
 */
public class ObjectUtils {

    /**
     * 转相应的值对象转换成MAP的键值对
     *
     * @param record
     * @return
     */
    public static Map<String, Object> tranObj2Map(Object record) {
        Map<String, Object> param = new HashMap<String, Object>();
        Field[] fields = record.getClass().getDeclaredFields();
        Object val;
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                val = fields[i].get(record);
                if (val != null && !val.toString().equals("")) {
                    // param.put(fields[i].getName(), val.toString());
                    param.put(fields[i].getName(), val);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("fetch field error..");
        }
        return param;
    }
}
