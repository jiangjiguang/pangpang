package com.pangpang.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ 金额格式转换.
 * @version 2.0 Created on 2007-05-15
 */
public class AmountUtil {

    private static final BigDecimal MAG = BigDecimal.valueOf(1000);
    private static final BigDecimal MAG4 = BigDecimal.valueOf(10000);
    /**
     * 保留小数位数
     */
    private static final int SCALE_VALUE = 3;
    public static final int DEFAULT_MONERY_SCALE_VALUE = 2;

    /**
     * 四舍五入保留两位小数
     *
     * @param amount
     * @return amount四舍五入结果
     */
    public static double formatAmount(double amount) {
        BigDecimal b = new BigDecimal(amount);
        return b.setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 验证输入的字符串是否是金额格式
     *
     * @param amount
     * @return
     */
    public static boolean checkAmount(String amountString) {
        if (null == amountString || amountString.trim().length() == 0) {
            return false;
        }
        amountString = amountString.trim();
        String str = "^(0(\\.\\d{0,2})?|([1-9]+[0]*)+(\\.\\d{0,2})?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(amountString);
        return m.find();
    }

    /**
     * 格式化
     *
     * @param amount
     * @return
     */
    public static String formatAmountStr(String amountString) {
        int pos = amountString.indexOf(".");
        if (pos != -1) {
            int tempInt = amountString.substring(pos + 1, amountString.length()).length();
            if (tempInt == 1) {
                amountString += "0";
            }
            if (tempInt > 2) {
                amountString = amountString.substring(0, pos + 3);
            }
        } else {
            amountString += ".00";
        }
        return amountString;
    }

    /**
     * 除1000得到真实钱值
     *
     * @param money
     * @return
     */
    public static BigDecimal divide1000(Long money) {
        if (money == null) {
            return null;
        }
        BigDecimal result = BigDecimal.valueOf(money);
        return result.divide(MAG).setScale(DEFAULT_MONERY_SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除1000得到真实钱值(保留3位小数)
     *
     * @param money
     * @return
     */
    public static BigDecimal divide1000Scale3(Long money) {
        if (money == null) {
            return null;
        }
        BigDecimal result = BigDecimal.valueOf(money);
        return result.divide(MAG).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除1000得到真实钱值
     *
     * @param money
     * @return
     */
    public static BigDecimal divide1000(BigDecimal money) {
        if (money == null) {
            return null;
        }
        return money.divide(MAG).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除10000得到真实钱值
     *
     * @param money
     * @return
     */
    public static BigDecimal divide10000(Long money) {
        if (money == null) {
            return null;
        }
        BigDecimal result = BigDecimal.valueOf(money);
        return result.divide(MAG4).setScale(DEFAULT_MONERY_SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除10000得到真实钱值,保留4为小数
     *
     * @param money
     * @return
     */
    public static BigDecimal divide10000For4Scale(Long money) {
        if (money == null) {
            return null;
        }
        BigDecimal result = BigDecimal.valueOf(money);
        return result.divide(MAG4).setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除10000得到真实钱值
     *
     * @param money
     * @return
     */
    public static BigDecimal divide10000(BigDecimal money) {
        if (money == null) {
            return null;
        }
        return money.divide(MAG4).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 乘以1000得到计算用钱值
     *
     * @param money
     * @return
     */
    public static Long multiply1000(BigDecimal money) {
        if (money == null) {
            return null;
        }
        return money.multiply(MAG).setScale(2, BigDecimal.ROUND_HALF_UP).longValue();
    }

    /**
     * 乘以10000得到计算用钱值
     *
     * @param money
     * @return
     */
    public static Long multiply10000(BigDecimal money) {
        if (money == null) {
            return null;
        }
        return money.multiply(MAG4).setScale(2, BigDecimal.ROUND_HALF_UP).longValue();
    }

    /**
     * 乘以1000得到计算用钱值
     *
     * @param money
     * @return
     */
    public static Long multiply1000(Double money) {
        if (money == null) {
            return null;
        }
        BigDecimal result = BigDecimal.valueOf(money);
        return result.multiply(MAG).setScale(2, BigDecimal.ROUND_HALF_UP).longValue();
    }

    /**
     * 乘以1000得到计算用钱值
     *
     * @param money
     * @return
     */
    public static Long multiply1000(Long money) {
        if (money == null) {
            return null;
        }
        BigDecimal result = BigDecimal.valueOf(money);
        return result.multiply(MAG).setScale(2, BigDecimal.ROUND_HALF_UP).longValue();
    }

    public static void main(String[] args) {
//        long id = 1234567890L;
//        BigDecimal bigDecimal = AmountUtil.divide1000Scale3(id);
//        System.out.println(bigDecimal);

        long total = 1000L;
        long amount = 88L;
        BigDecimal b1 = new BigDecimal(total);
        BigDecimal b2 = new BigDecimal(amount);
        double result = b2.divide(b1).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(result);
    }
}
