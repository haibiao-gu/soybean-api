package com.infiext.soybean.utils;

import java.math.BigDecimal;

public class MathUtil {

    public static BigDecimal safeMultiply(BigDecimal a, BigDecimal b) {
        if (null == a || null == b) {
            return BigDecimal.ZERO;
        }
        return a.multiply(b);
    }
}
