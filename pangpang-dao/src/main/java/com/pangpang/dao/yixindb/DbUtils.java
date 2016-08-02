package com.pangpang.dao.yixindb;


import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.apache.commons.beanutils.ConvertUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Created by yxjiang on 2016/2/29.
 */
public class DbUtils {
    private static final Converter<String, String> upperUnderScoreToLowerCamel = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

    public static String toCamel(String columnName) {
        return upperUnderScoreToLowerCamel.convert(columnName);
    }

    public static String toUpperUnderScore(String fieldName) {
        return upperUnderScoreToLowerCamel.reverse().convert(fieldName);
    }

    public static Object convertToDbValue(Object val, int type) throws Exception {
        if (val == null) {
            return null;
        }
        try {
            switch (type) {
                case Types.BIGINT:
                    return ConvertUtils.convert(val, Long.class);
                case Types.TINYINT:
                case Types.INTEGER:
                    return ConvertUtils.convert(val, Integer.class);
                case Types.DECIMAL:
                    return ConvertUtils.convert(val, BigDecimal.class);
                case Types.CHAR:
                case Types.VARCHAR:
                    return ConvertUtils.convert(val, String.class);
                case Types.TIMESTAMP:
                    return ConvertUtils.convert(val, Timestamp.class);
            }
            return val;
        } catch (Exception e) {
            return null;
        }
    }
}
