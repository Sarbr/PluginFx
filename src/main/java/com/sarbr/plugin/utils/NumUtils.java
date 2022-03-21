package com.sarbr.plugin.utils;

import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumUtils extends NumberUtils {

    /**
     * 精确的加法运算.
     * @return 结果
     */
    public static double add(Number v1, Number v2) {
        BigDecimal b1 = BigDecimal.valueOf(filterNull(v1));
        BigDecimal b2 = BigDecimal.valueOf(filterNull(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     *
     * 精确的减法运算.
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static double subtract(Number v1, Number v2) {
        BigDecimal b1 = BigDecimal.valueOf(filterNull(v1));
        BigDecimal b2 = BigDecimal.valueOf(filterNull(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算.
     * @param v1 乘数
     * @param v2 乘数
     * @return 积
     */
    public static double multiply(Number v1, Number v2) {
        BigDecimal b1 = BigDecimal.valueOf(filterNull(v1));
        BigDecimal b2 = BigDecimal.valueOf(filterNull(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算，并对运算结果截位.
     * @param scale 运算结果小数后精确的位数
     * @param v1 乘数
     * @param v2 乘数
     * @return 积
     */
    public static double multiply(Number v1, Number v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(filterNull(v1));
        BigDecimal b2 = BigDecimal.valueOf(filterNull(v2));
        return b1.multiply(b2).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算.
     * @param v1 被除数
     * @param v2 除数
     * @return 商
     */
    public static double divide(Number v1, Number v2) {
        BigDecimal b1 = BigDecimal.valueOf(filterNull(v1));
        BigDecimal b2 = BigDecimal.valueOf(filterNull(v2));
        return b1.divide(b2,10, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算. 由scale参数指定精度，以后的数字四舍五入.
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位
     * @return 商
     */
    public static double divide(Number v1, Number v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(filterNull(v1));
        BigDecimal b2 = BigDecimal.valueOf(filterNull(v2));
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 过滤空的Double对象，如果为空返回0.0，否则返回double类型
     * @param num 待过滤对象
     * @return 过滤结果
     */
    public static double filterNull(Number num){
        if(num != null){
            return Double.parseDouble(num.toString());
        }
        return 0.0;
    }

}
