package com.pangpang.util;

import java.util.regex.Pattern;

/**
 * Created by jiangjg on 2016/9/7.
 */
public class RegexUtil {
    public static void main(String[] args) {
        System.out.println(matchDate("2015-12-08"));
    }
    private static String lou = "\\d+楼";
    private static String dateNoTime = "(\\d{4}-\\d{2}-\\d{2})([\\s\\S]*)";

    private static Pattern pattern1;
    private static Pattern pattern2;
    static{
        pattern1 = Pattern.compile(lou);
        pattern2 = Pattern.compile(dateNoTime);

    }
    public static boolean matchLou(String strDate){
        return pattern1.matcher(strDate).matches();
    }

    public static boolean matchDate(String str){
        return pattern2.matcher(str).matches();
    }
}
