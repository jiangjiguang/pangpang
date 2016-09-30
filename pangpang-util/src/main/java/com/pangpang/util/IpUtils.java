/**
 *
 */
package com.pangpang.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author zhengbaiyun
 *
 */
public class IpUtils {

    public static void main(String[] args) {
        System.out.println(ipToLong("1.1.63.255"));
    }

    public static boolean startsWithAny(String str, Collection<String> prefixs) {
        Iterator<String> iter = prefixs.iterator();
        while (iter.hasNext()) {
            String prefix = iter.next();
            if (str.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换 10.129.129.51 为 <b>10.129.129.*</b>
     *
     *
     * @param source
     * @return
     */
    public static String toSingleWildcardIp(String source) {
        if (source == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(source.substring(0, source.lastIndexOf('.')));
        sb.append(".*");

        return sb.toString();
    }

    /**
     * 转换 10.129.129.51 为 <b>10.129.*.*</b>
     *
     *
     * @param source
     * @return
     */
    public static String toDoubleWildcardIp(String source) {
        if (source == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(source.substring(0, source.lastIndexOf('.', source.lastIndexOf('.') - 1)));
        sb.append(".*.*");

        return sb.toString();
    }

    public static long ipToLong(String strIp) {
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    //将十进制整数形式转换成127.0.0.1形式的ip地址
    public static String longToIP(long longIp) {
        StringBuilder sb = new StringBuilder("");
        //直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }
}
