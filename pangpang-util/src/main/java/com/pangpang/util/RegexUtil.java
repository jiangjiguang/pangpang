package com.pangpang.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by jiangjg on 2016/9/7.
 */
public class RegexUtil {
    public static void main(String[] args) {
        String ss = "/usr/local\\local";
        System.out.println(toBackslash(ss));
    }
    private static String lou = "\\d+楼";
    private static String dateNoTime = "(\\d{4}-\\d{2}-\\d{2})([\\s\\S]*)";
    private static String toSlash = "\\{1,2}";

    private static Pattern pattern1;
    private static Pattern pattern2;
    private static Pattern patternToSlash;

    static{
        pattern1 = Pattern.compile(lou);
        pattern2 = Pattern.compile(dateNoTime);
        patternToSlash = Pattern.compile(toSlash);

    }
    public static boolean matchLou(String strDate){
        return pattern1.matcher(strDate).matches();
    }

    public static boolean matchDate(String str){
        return pattern2.matcher(str).matches();
    }

    //反斜杠化 /
    public static String toBackslash(String path){
        if(StringUtils.isBlank(path)){
            return "";
        }
        return patternToSlash.matcher(path).replaceAll("/");
    }
}
