package com.pangpang.dao.yixindb;

import java.util.Map;

/**
 * Created by yxjiang on 2016/2/25.
 */
public class SqlTable {
    private String name;
    private Map<String, SqlColumn> primaryKeys;
    private Map<String, SqlColumn> columns;
    private Map<String, SqlColumn> indexes;

    public SqlTable(String name, Map<String, SqlColumn> primaryKeys, Map<String, SqlColumn> columns, Map<String, SqlColumn> indexes) {
        this.name = name;
        this.primaryKeys = primaryKeys;
        this.columns = columns;
        this.indexes = indexes;
    }

    public String getName() {
        return name;
    }

    public Map<String, SqlColumn> getPrimaryKeys() {
        return primaryKeys;
    }

    public Map<String, SqlColumn> getColumns() {
        return columns;
    }

    public Map<String, SqlColumn> getIndexes() {
        return indexes;
    }

    @Override
    public String toString() {
        return "SqlTable{" +
                "name='" + name + '\'' +
                ", primaryKeys=" + primaryKeys +
                ", columns=" + columns +
                ", indexes=" + indexes +
                '}';
    }
}
