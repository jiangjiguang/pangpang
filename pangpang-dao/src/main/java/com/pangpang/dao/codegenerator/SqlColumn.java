package com.pangpang.dao.codegenerator;

/**
 * Created by root on 16-9-3.
 */
public class SqlColumn {
    //列名
    private String name;
    //列类型
    private int type;
    //是否主键
    private boolean primaryKey;
    //是否自增
    private boolean autoIncrement;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    @Override
    public String toString() {
        return "SqlColumn{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", primaryKey=" + primaryKey +
                ", autoIncrement=" + autoIncrement +
                '}';
    }
}
