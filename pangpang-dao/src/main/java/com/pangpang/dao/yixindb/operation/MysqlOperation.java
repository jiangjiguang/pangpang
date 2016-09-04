package com.pangpang.dao.yixindb.operation;

import com.google.common.collect.Maps;
import com.pangpang.dao.yixindb.SqlColumn;
import com.pangpang.dao.yixindb.SqlTable;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yxjiang on 2016/2/25.
 */
public class MysqlOperation extends AbstSqlOperation {

    private DataSource dataSource;

    public MysqlOperation(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<String> getTables(String schema) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(schema, "", null, new String[]{"TABLE"});
            List<String> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString("TABLE_NAME"));
            }
            return list;
        } finally {
        }
    }

    @Override
    public List<String> getPrimaryKeys(String schema, String table) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            List<String> list = new ArrayList<>();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getPrimaryKeys(schema, "", table);
            while (resultSet.next()) {
                String key = resultSet.getString("COLUMN_NAME");
                if (StringUtils.isNotBlank(key)) {
                    list.add(key);
                }
            }
            return list;
        }
    }

    @Override
    public List<SqlColumn> getColumns(String schema, String table) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            List<SqlColumn> list = new ArrayList<>();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(schema, "", table, null);
            while (resultSet.next()) {
                int jdbcType = resultSet.getInt("DATA_TYPE");
                String columnName = resultSet.getString("COLUMN_NAME");
                String isAutoincrement = resultSet.getString("IS_AUTOINCREMENT");
                final SqlColumn col = new SqlColumn(columnName, jdbcType, StringUtils.equals(isAutoincrement, "YES"));
                list.add(col);
            }
            return list;
        }
    }

    @Override
    public List<String> getIndexInfo(String schema, String table) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            List<String> list = new ArrayList<>();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getIndexInfo(schema, "", table, false, false);
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                list.add(columnName);
            }
            return list;
        }
    }

    @Override
    public int update(SqlTable table, Map<String, Object> updateData, Map<String, Object> queryData) throws Exception {
        Map<String, Object> dbData = toDbData(table, updateData);
        Map<String, Object> dbQueryData = toDbData(table, queryData);
        if (MapUtils.isEmpty(dbData)) {
            return 0;
        } else if (MapUtils.isEmpty(dbQueryData)) {
            return 0;
        } else  {
            String sql = createUpdateSql(table, dbData, dbQueryData);
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    prepareUpdate(ps, table, dbData, dbQueryData);
                    return doExecuteUpdate(ps, 2);
                }
            }
        }
    }


    @Override
    public Map<String, Object> insert(SqlTable table, Map<String, Object> data) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            Map<String, Object> dbData = toDbData(table, data);
            if (MapUtils.isEmpty(dbData)) {
                return null;
            } else {
                String sql = createInsertSql(table, dbData);
                try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    prepareInsert(table, ps, dbData);
                    doExecuteUpdate(ps, 2);
                    Map<String, Object> primaryKeys = getRecordPrimaryKeys(ps, table, dbData);
                    return primaryKeys;
                }
            }
        }
    }

    private void prepareInsert(SqlTable table, PreparedStatement ps, Map<String, Object> data) throws SQLException {
        int idx = 1;
        for (Map.Entry<String, SqlColumn> entry : table.getPrimaryKeys().entrySet()) {
            if (!entry.getValue().isAutoIncrement()) {
                String columnName = entry.getKey();
                Object value = data.get(columnName);
                int type = table.getColumns().get(columnName).getType();
                setPrepareStatement(ps, idx++, value, type);
            }
        }
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String colName = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                SqlColumn sqlColumn = table.getColumns().get(colName);
                if (sqlColumn != null && !table.getPrimaryKeys().containsKey(sqlColumn.getName()) && !sqlColumn.isAutoIncrement()) {
                    int type = sqlColumn.getType();
                    setPrepareStatement(ps, idx++, value, type);
                }
            }
        }
    }

    private Map<String, Object> getRecordPrimaryKeys(PreparedStatement ps, SqlTable table, Map<String, Object> data) throws SQLException {
        Map<String, Object> keys = Maps.newLinkedHashMap();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        while (generatedKeys.next()) {
            for (Map.Entry<String, SqlColumn> entry : table.getPrimaryKeys().entrySet()) {
                String colName = entry.getKey();
                if (entry.getValue().isAutoIncrement() && data.get(colName) == null) {
                    keys.put(colName, generatedKeys.getObject("GENERATED_KEY"));
                }
            }
        }
        return keys;
    }

    private void prepareUpdate(PreparedStatement ps, SqlTable table, Map<String, Object> data, Map<String, Object> queryData) throws SQLException {
        int idx = 1;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String colName = entry.getKey();
            Object value = data.get(colName);
            if (value != null) {
                SqlColumn sqlColumn = table.getColumns().get(colName);
                setPrepareStatement(ps, idx++, value, sqlColumn.getType());
            }
        }
        for (Map.Entry<String, Object> entry : queryData.entrySet()) {
            String colName = entry.getKey();
            Object value = queryData.get(colName);
            if (value != null) {
                SqlColumn sqlColumn = table.getColumns().get(colName);
                setPrepareStatement(ps, idx++, value, sqlColumn.getType());
            }
        }
    }

    /**
     * 执行sp调用，异常重试2次
     *
     * @param ps
     * @param retryTimes
     * @throws SQLException
     */
    private int doExecuteUpdate(PreparedStatement ps, int retryTimes) throws SQLException {
        try {
            return ps.executeUpdate();
        } catch (SQLException e) {
            if (retryTimes < 1) {
                throw e;
            } else {
                return doExecuteUpdate(ps, retryTimes - 1);
            }
        }
    }

    private String createInsertSql(SqlTable table, Map<String, Object> data) {
        String sql = " insert into " + table.getName() + " ( ";
        int parameterCount = 0;
        for (Map.Entry<String, SqlColumn> entry : table.getPrimaryKeys().entrySet()) {
            String colName = entry.getKey();
            if (!entry.getValue().isAutoIncrement()) {
                if (parameterCount > 0) {
                    sql += ", ";
                }
                sql += colName;
                parameterCount++;
            }
        }
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String colName = entry.getKey();
            Object val = entry.getValue();
            if (val != null) {
                if (!table.getPrimaryKeys().containsKey(colName)) {
                    if (parameterCount > 0) {
                        sql += ", ";
                    }
                    sql += colName;
                    parameterCount++;
                }
            }
        }
        sql += " ) values ( ";
        for (int i = 0; i < parameterCount; i++) {
            if (i > 0) {
                sql += " ,";
            }
            sql += "?";
        }
        sql += " ) ";
        return sql;
    }

    private String createUpdateSql(SqlTable table, Map<String, Object> updateData, Map<String, Object> queryData) {
        String sql = " update " + table.getName() + " set ";
        int idx = 0;
        for (Map.Entry<String, Object> entry : updateData.entrySet()) {
            String colName = entry.getKey();
            Object val = entry.getValue();
            if (idx > 0) {
                sql += ", ";
            }
            if (val != null) {
                sql += colName + " = ?";
            }
            idx++;
        }
        sql += " where ";
        idx = 0;
        for (Map.Entry<String, Object> entry : queryData.entrySet()) {
            String colName = entry.getKey();
            Object val = entry.getValue();
            if (idx > 0) {
                sql += " and ";
            }
            if (val != null) {
                sql += colName + " = ?";
            }
            idx++;
        }
        return sql;
    }

    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }
}
