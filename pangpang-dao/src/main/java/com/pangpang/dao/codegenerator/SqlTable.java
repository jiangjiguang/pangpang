package com.pangpang.dao.codegenerator;


import java.util.Map;

/**
 * Created by root on 16-9-3.
 */
public class SqlTable {
    private String name;
    private Map<String, SqlColumn> primaryKeys;
    private Map<String, SqlColumn> columns;
    private Map<String, SqlColumn> indexes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, SqlColumn> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(Map<String, SqlColumn> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public Map<String, SqlColumn> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, SqlColumn> columns) {
        this.columns = columns;
    }

    public Map<String, SqlColumn> getIndexes() {
        return indexes;
    }

    public void setIndexes(Map<String, SqlColumn> indexes) {
        this.indexes = indexes;
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
