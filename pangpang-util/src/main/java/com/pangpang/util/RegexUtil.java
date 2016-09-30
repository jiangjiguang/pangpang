package com.pangpang.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by jiangjg on 2016/9/7.
 */
public class RegexUtil {
    public static void main(String[] args) {
        System.out.println(firstIsLetter(""));
        System.out.println(firstIsLetter("_"));
        System.out.println(firstIsLetter("a"));
        System.out.println(firstIsLetter("ba"));
    }
    private static String firstLetter = "^[a-zA-Z]*";
    private static String firstLetterIsUpper = "^[A-Z]*";
    private static String firstLetterIsLower = "^[a-z]*";
    private static String lou = "\\d+楼";
    private static String dateNoTime = "(\\d{4}-\\d{2}-\\d{2})([\\s\\S]*)";
    private static String toSlash = "\\{1,2}";

    private static Pattern pattern1;
    private static Pattern pattern2;
    private static Pattern patternToSlash;
    private static Pattern firstLetterPattern;
    private static Pattern firstLetterIsUpperPattern;
    private static Pattern firstLetterIsLowerPattern;

    static{
        pattern1 = Pattern.compile(lou);
        pattern2 = Pattern.compile(dateNoTime);
        patternToSlash = Pattern.compile(toSlash);
        firstLetterPattern = Pattern.compile(firstLetter);
        firstLetterIsUpperPattern = Pattern.compile(firstLetterIsUpper);
        firstLetterIsLowerPattern = Pattern.compile(firstLetterIsLower);

    }

    public static boolean firstLetterIsLower(String string){
        if(StringUtils.isBlank(string)){
            return false;
        }
        return firstLetterIsLowerPattern.matcher(string).matches();
    }

    public static boolean firstLetterIsUpper(String string){
        if(StringUtils.isBlank(string)){
            return false;
        }
        return firstLetterIsUpperPattern.matcher(string).matches();
    }

    public static boolean firstIsLetter(String string){
        if(StringUtils.isBlank(string)){
            return false;
        }
        return firstLetterPattern.matcher(string).matches();
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
