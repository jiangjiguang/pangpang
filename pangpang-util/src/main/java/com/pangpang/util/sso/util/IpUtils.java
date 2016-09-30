package com.pangpang.util.sso.util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class IpUtils {

    /**
     * 验证是否是一个合法的IP, 允许 *.*.*.* , 192.168.0.*, 192.168.*.* , 192.168.0.1 这4种格式
     */
    public static boolean isValid(String ip) {
        String[] arr = StringUtils.split(ip, '.');
        if (arr.length != 4) {
            return false;
        }
        int[] newArr = new int[4];
        for (int i = 0; i < 4; i++) {
            if (!"*".equals(arr[i]) && !StringUtils.isNumeric(arr[i])) {
                return false;
            }
            if (!"*".equals(arr[i])) {
                newArr[i] = Integer.valueOf(arr[i]);
            } else {
                newArr[i] = 0;
            }
        }
        if (newArr[0] < 0 || newArr[0] >= 255
                || newArr[1] < 0 || newArr[1] >= 255
                || newArr[2] < 0 || newArr[2] >= 255
                || newArr[3] < 0 || newArr[3] >= 255) {
            return false;
        }
        return true;
    }

    /**
     * 转换 10.129.129.51 为 10.129.129.*
     */
    public static String toSingleWildcardIP(String source) {
        StringBuilder builder = new StringBuilder();
        builder.append(source.substring(0, source.lastIndexOf('.')));
        builder.append(".*");
        return builder.toString();
    }

    /**
     * 转换 10.129.129.51 为 10.129.*.*
     */
    public static String toDoubleWildcardIP(String source) {
        StringBuilder builder = new StringBuilder();
        builder.append(source.substring(0, source.lastIndexOf('.', source.lastIndexOf('.') - 1)));
        builder.append(".*.*");
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println("*.*.*.*: " + isValid("*.*.*.*"));
        System.out.println("192.168.0.1: " + isValid("192.168.0.1"));
        System.out.println("192.168.0.*: " + isValid("192.168.0.*"));
        System.out.println("192.168.*.*: " + isValid("192.168.*.*"));
        System.out.println("255.255.255.255: " + isValid("255.255.255.255"));

//        String ipStr = "10.241.73.96";
//        long longIp = IpUtils.ipToLong(ipStr);
        long ip1 = 16778240L;
//        System.out.println("192.168.0.1 的整数形式为：" + longIp);
//        System.out.println("整数" + longIp + "转化成字符串IP地址：" + IpUtils.longToIP(longIp));
//        System.out.println("整数" + ip1 + "转化成字符串IP地址：" + IpUtils.longToIP(ip1));
        //ip地址转化成二进制形式输出
//        System.out.println("192.168.0.1 的二进制形式为：" + Long.toBinaryString(longIp));
    }
}
