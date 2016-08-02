package com.pangpang.dao.yixindb;

/**
 * Created by yxjiang on 2016/2/25.
 */
public class SqlColumn {
    private String name;
    private int type;
    private boolean autoIncrement;

    public SqlColumn(String name, int type, boolean autoIncrement) {
        this.name = name;
        this.type = type;
        this.autoIncrement = autoIncrement;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    @Override
    public String toString() {
        return "SqlColumn{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", autoIncrement=" + autoIncrement +
                '}';
    }
}
