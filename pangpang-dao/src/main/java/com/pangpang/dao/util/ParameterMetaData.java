package com.pangpang.dao.util;

/**
 * Created by jiangjg on 2016/6/16.
 */
public class ParameterMetaData {
    private String parameterName;
    private int parameterType;
    private int sqlType;
    private String typeName;
    private boolean nullable;

    public ParameterMetaData(String columnName, int columnType, int sqlType, String typeName, boolean nullable) {
        this.parameterName = columnName;
        this.parameterType = columnType;
        this.sqlType = sqlType;
        this.typeName = typeName;
        this.nullable = nullable;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public int getParameterType() {
        return this.parameterType;
    }

    public int getSqlType() {
        return this.sqlType;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public boolean isNullable() {
        return this.nullable;
    }
}
