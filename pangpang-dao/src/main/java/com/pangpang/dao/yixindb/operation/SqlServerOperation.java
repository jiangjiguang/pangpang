package com.pangpang.dao.yixindb.operation;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pangpang.dao.yixindb.DbUtils;
import com.pangpang.dao.yixindb.SqlColumn;
import com.pangpang.dao.yixindb.SqlTable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections.MapUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yxjiang on 2016/2/25.
 */
public class SqlServerOperation extends AbstSqlOperation {

    private DataSource dataSource;

    public SqlServerOperation(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<String> getTables(String schema) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(schema, "dbo", null, new String[]{"TABLE"});
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
            ResultSet resultSet = metaData.getPrimaryKeys(schema, "dbo", table);
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
            ResultSet resultSet = metaData.getColumns(schema, "dbo", table, null);
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
            ResultSet resultSet = metaData.getIndexInfo(schema, "dbo", table, false, false);
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
        if (MapUtils.isEmpty(dbData)) {
            return 0;
        } else if (MapUtils.isEmpty(queryData)) {
            return 0;
        } else {
            String sp = createUpdateSp(table, dbData);
            List<String> selectColumns = Lists.newArrayList();
            for (String primaryColumn : table.getPrimaryKeys().keySet()) {
                selectColumns.add(DbUtils.toCamel(primaryColumn));
            }
            List<Map<String, Object>> list = select(table, selectColumns, queryData);
            if (CollectionUtils.isNotEmpty(list)) {
                for (Map<String, Object> primaryKeys : list) {
                    try (Connection connection = dataSource.getConnection()) {
                        //logger.debug("update sp statement: {}", sp);
                        try (CallableStatement cs = connection.prepareCall(sp)) {
                            prepareUpdate(cs, table, dbData, toDbData(table, primaryKeys));
                            doExecute(cs, 2);
                        }
                    }
                }
                return list.size();
            }
            return 0;
        }
    }


    @Override
    public Map<String, Object> insert(SqlTable table, Map<String, Object> data) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            Map<String, Object> dbData = toDbData(table, data);
            if (MapUtils.isEmpty(dbData)) {
                //logger.warn("no data, ignore insert table {}", table.getName());
                return null;
            } else {
                String sp = createInsertSp(table, dbData);
                //logger.debug("insert sp statement: {}", sp);
                try (CallableStatement cs = connection.prepareCall(sp)) {
                    prepareInsert(cs, table, dbData);
                    doExecute(cs, 2);
                    Map<String, Object> primaryKeys = getRecordPrimaryKeys(cs, table, dbData);
                    return primaryKeys;
                }
            }
        }
    }

    private Map<String, Object> getRecordPrimaryKeys(CallableStatement cs, SqlTable table, Map<String, Object> data) throws SQLException {
        Map<String, Object> keys = Maps.newLinkedHashMap();
        int idx = 1;
        for (Map.Entry<String, SqlColumn> entry : table.getPrimaryKeys().entrySet()) {
            String colName = entry.getKey();
            if (entry.getValue().isAutoIncrement() && data.get(colName) == null) {
                keys.put(colName, cs.getObject(idx++));
            }
        }
        return keys;
    }

    private void prepareInsert(CallableStatement cs, SqlTable table, Map<String, Object> data) throws SQLException {
        int idx = 1;
        for (Map.Entry<String, SqlColumn> entry : table.getPrimaryKeys().entrySet()) {
            if (entry.getValue().isAutoIncrement() && data.get(entry.getKey()) == null) {
                cs.registerOutParameter(idx++, entry.getValue().getType());
            }
        }
        setCallStatementData(cs, table, data, idx);
    }

    private void prepareUpdate(CallableStatement cs, SqlTable table, Map<String, Object> data, Map<String, Object> primaryKeys) throws SQLException {
        int idx = 1;
        for (Map.Entry<String, SqlColumn> entry : table.getPrimaryKeys().entrySet()) {
            String colName = entry.getKey();
            Object val = primaryKeys.get(colName);
            if (val != null) {
                SqlColumn sqlColumn = table.getColumns().get(colName);
                cs.setObject(idx++, val, sqlColumn.getType());
            }
        }
        setCallStatementData(cs, table, data, idx);
    }

    private int setCallStatementData(CallableStatement cs, SqlTable table, Map<String, Object> data, int idx) throws SQLException {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String colName = entry.getKey();
            Object val = entry.getValue();
            if (val != null) {
                SqlColumn sqlColumn = table.getColumns().get(colName);
                cs.setObject(idx++, val, sqlColumn.getType());
            }
        }
        return idx;
    }

    /**
     * 执行sp调用，异常重试2次
     *
     * @param cs
     * @param retryTimes
     * @throws SQLException
     */
    private void doExecute(CallableStatement cs, int retryTimes) throws SQLException {
        try {
            cs.execute();
        } catch (SQLException e) {
            if ("Lock request time out period exceeded.".equals(e.getMessage())) {
                if (retryTimes < 1) {
                    throw e;
                } else {
                    doExecute(cs, retryTimes - 1);
                }
            } else {
                throw e;
            }
        }
    }

    private String createInsertSp(SqlTable table, Map<String, Object> data) {
        String sqa = "{call spA_" + table.getName() + "_i ( %s )}";
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, SqlColumn> entry : table.getPrimaryKeys().entrySet()) {
            String colName = entry.getKey();
            if (entry.getValue().isAutoIncrement() && data.get(colName) == null) {
                list.add("@" + colName + " = ?");
            }
        }
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String colName = entry.getKey();
            Object val = entry.getValue();
            if (val != null) {
                if (!table.getPrimaryKeys().containsKey(colName)) {
                    list.add("@" + colName + " = ?");
                } else {
                    list.add("@" + colName + " = ?");
                }
            }
        }
        String join = Joiner.on(',').join(list);
        if (StringUtils.isNotBlank(join)) {
            return String.format(sqa, join);
        } else {
            return null;
        }
    }

    private String createUpdateSp(SqlTable table, Map<String, Object> updateData) {
        String sqa = "{call spA_" + table.getName() + "_u ( %s )}";
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, SqlColumn> entry : table.getPrimaryKeys().entrySet()) {
            String colName = entry.getKey();
            list.add("@" + colName + " = ?");
        }
        for (Map.Entry<String, Object> entry : updateData.entrySet()) {
            String colName = entry.getKey();
            Object val = entry.getValue();
            if (val != null) {
                list.add("@" + colName + " = ?");
            }
        }
        String join = Joiner.on(',').join(list);
        if (StringUtils.isNotBlank(join)) {
            return String.format(sqa, join);
        } else {
            return null;
        }
    }

    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }
}
