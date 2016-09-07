package com.pangpang.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jiangjg on 2016/9/7.
 */
public class DateUtil {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        for(int i=list.size()-1; i>= 0; i--){
            System.out.println(i);
        }

        System.out.println(11);
    }
    private static final String[] STANDARM_DATE_FORMAT = { "yyyy-MM-dd", "yyyy/MM/dd",
            "MM-dd","HH:mm","hh:mm","yyyy-MM-dd HH:mm",
            "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'hh:mm:ss",
            "yyyy/MM/dd'T'HH:mm:ss","yyyy/MM/dd'T'hh:mm:ss",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd hh:mm:ss",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd hh:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss+08:00", "yyyy-MM-dd'T'hh:mm:ss+08:00",
            "yyyy/MM/dd'T'HH:mm:ss+08:00", "yyyy/MM/dd'T'hh:mm:ss+08:00",

    };

    public static Date str2Date(String string){
        Date date = null;
        try{
            if(StringUtils.isBlank(string)){
                return null;
            }
            if(string.length() > 19){
                string = string.substring(0, 19);
            }
            date = DateUtils.parseDate(string, STANDARM_DATE_FORMAT);
        }catch (Exception ex){
            return null;
        }
        return date;
    }

    public static long getNowTime(){
        return new Date().getTime();
    }

    public static long getOneDayTime(){
        return 86400000;
    }

    public static long getOneHourTime(){
        return 3600000;
    }

    public static int getDay(Date date){
        if(date == null){
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static long getlastYearTimePoint(){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.YEAR, -1);
        return rightNow.getTimeInMillis();
    }

    public static int getYesterday(){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.DATE, -1);
        return rightNow.get(Calendar.DAY_OF_YEAR);
    }

    public static int minusOneDay(Date date){
        if(date == null){
            return -1;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DATE, -1);
        return rightNow.get(Calendar.DAY_OF_YEAR);
    }

    public static long geBaseLongGMT8(){
        Date date = null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = df.parse("1970-01-01 00:00:00");
        } catch (Exception ex) {
        }
        return date.getTime();
    }

    public static Date long2Date(long number){
        return new Date(number);
    }

    public static Date maxDate(Date date1, Date date2){
        if(date1 == null){
            return date2;
        }
        if(date2 == null){
            return date1;
        }
        return date1.getTime() > date2.getTime() ? date1 : date2;
    }

    public static boolean isFirstHalfYear(Date nowDate){
        if(nowDate == null){
            return true;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(nowDate);
        int year = rightNow.get(Calendar.YEAR);
        String halfYear = String.format("%s-07-01", year);
        rightNow.setTime(str2Date(halfYear));
        return nowDate.getTime() < rightNow.getTimeInMillis() ? true : false;
    }

    public static int getDatsOfHalfYear(){
        return 183;
    }

    public static long date2Long(Date date){
        if(date == null){
            return 0;
        }
        return date.getTime();
    }
}
