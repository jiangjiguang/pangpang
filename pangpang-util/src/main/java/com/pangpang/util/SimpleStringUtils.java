package com.pangpang.util;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author zhengby
 */
public class SimpleStringUtils {

    /**
     * 替换null为""
     *
     * @param c
     * @return
     */
    public static String replaceNull(String c) {
        if (c == null) {
            c = "";
        }
        return c;
    }

    public static String removeReturn(String c) {
        c = StringUtils.replace(c, "\r\n", "");
        c = StringUtils.replace(c, "\n", "");
        return c;
    }

    /**
     * 格式化数字
     *
     * @param number
     * @param scale 小数点位数
     * @return
     */
    public static String formatNumber(double number, int scale) {
        return "" + new java.math.BigDecimal(number).setScale(scale, java.math.BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 格式化数字为逗号分隔符格式
     *
     * @param number
     * @param scale 小数点位数
     * @return
     */
    public static String formatMoney(double number, int scale) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(scale);

        return nf.format(number);
    }

    /**
     * InputStream 转 String
     *
     * @param in
     * @param charsetName 编码
     * @return
     * @throws IOException
     */
    public static String InputStream2String(InputStream in, String charsetName) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int i; (i = in.read(b)) != -1;) {
            out.append(new String(b, 0, i, charsetName));
        }
        return out.toString();
    }

    /**
     * 全角转半角
     *
     * @param input
     * @return
     */
    public static String ToBanJiao(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public String lowerCaseFirstChar(String str) {
        if (str == null || str.trim().equals("")) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public String upperCaseFirstChar(String str) {
        if (str == null || str.trim().equals("")) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * 判断是否是数字字母下划线
     *
     * @param s
     * @return
     */
    public static boolean isNumberAndLetter(String s) {
        if (s != null) {
            Pattern p = Pattern.compile("^\\w+$");
            Matcher m = p.matcher(s);
            return m.find();
        } else {
            return false;
        }
    }
}
