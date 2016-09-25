package com.pangpang.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by root on 16-9-25.
 */
public class StringUtil {

    public static String captureName(String name) {
        if(StringUtils.isBlank(name) || RegexUtil.firstLetterIsUpper(name)){
            return name;
        }
        char[] cs=name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    public static String lowerName(String name) {
        if(StringUtils.isBlank(name) || RegexUtil.firstLetterIsLower(name)){
            return name;
        }
        char[] cs=name.toCharArray();
        cs[0] += 32;
        return String.valueOf(cs);
    }
}
