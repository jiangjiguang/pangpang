package com.pangpang.dao.yixindb.operation;



import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pangpang.dao.yixindb.DbUtils;
import com.pangpang.dao.yixindb.SqlColumn;
import com.pangpang.dao.yixindb.SqlTable;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * Created by yxjiang on 2016/2/29.
 */
public abstract class AbstSqlOperation implements SqlOperation {

    protected void setPrepareStatement(PreparedStatement ps, int idx, Object value, int type) throws SQLException {
        switch (type) {
            case Types.BIGINT:
                ps.setLong(idx, (Long) value);
                break;
            case Types.TINYINT:
            case Types.INTEGER:
                ps.setInt(idx, (Integer) value);
                break;
            case Types.DECIMAL:
                ps.setBigDecimal(idx, (BigDecimal) value);
                break;
            case Types.CHAR:
            case Types.VARCHAR:
                ps.setString(idx, (String) value);
                break;
            case Types.TIMESTAMP:
                ps.setTimestamp(idx, (Timestamp) value);
                break;
            default:
                throw new SQLException("不支持的列类型：" + type);
        }
    }

    protected abstract DataSource getDataSource();

    protected Map<String, Object> toDbData(SqlTable table, Map<String, Object> data) throws Exception {
        Map<String, Object> dbData = Maps.newHashMap();
        for (Map.Entry<String, SqlColumn> entry : table.getColumns().entrySet()) {
            String colName = entry.getKey();
            SqlColumn col = entry.getValue();
            Object val = data.get(DbUtils.toCamel(colName));
            if (val != null) {
                Object convertedValue = DbUtils.convertToDbValue(val, col.getType());
                if (convertedValue != null) {
                    dbData.put(colName, convertedValue);
                }
            }
        }
        return dbData;
    }

    protected List<String> toDbColumns(SqlTable table, List<String> columns) throws Exception {
        List<String> result = Lists.newArrayList();
        Map<String, SqlColumn> tableColumns = table.getColumns();
        for (String column : columns) {
            String dbColumn = DbUtils.toUpperUnderScore(column);
            if (tableColumns.containsKey(dbColumn)) {
                result.add(dbColumn);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> select(SqlTable table, List<String> columns, Map<String, Object> queryData) throws Exception {
        try (Connection connection = getDataSource().getConnection()) {
            Map<String, Object> dbData = toDbData(table, queryData);
            List<String> dbColumns = toDbColumns(table, columns);
            String sql = createSelectSql(table, dbColumns, dbData);
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                prepareSelect(table, ps, dbData);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Map<String, Object>> result = Lists.newArrayList();
                    while (rs.next()) {
                        Map<String, Object> row = Maps.newHashMap();
                        for (String dbColumn : dbColumns) {
                            int type = table.getColumns().get(dbColumn).getType();
                            switch (type) {
                                case Types.BIGINT:
                                    row.put(DbUtils.toCamel(dbColumn), rs.getLong(dbColumn));
                                    break;
                                case Types.TINYINT:
                                case Types.INTEGER:
                                    row.put(DbUtils.toCamel(dbColumn), rs.getInt(dbColumn));
                                    break;
                                case Types.DECIMAL:
                                    row.put(DbUtils.toCamel(dbColumn), rs.getBigDecimal(dbColumn));
                                    break;
                                case Types.CHAR:
                                case Types.VARCHAR:
                                    row.put(DbUtils.toCamel(dbColumn), rs.getString(dbColumn));
                                    break;
                                case Types.TIMESTAMP:
                                    row.put(DbUtils.toCamel(dbColumn), rs.getTimestamp(dbColumn));
                                    break;
                                default:
                                    throw new SQLException("不支持的列类型：" + type);
                            }
                        }
                        result.add(row);
                    }
                    return result;
                }
            }
        }
    }

    private void prepareSelect(SqlTable table, PreparedStatement ps, Map<String, Object> queryData) throws SQLException {
        int idx = 1;
        for (Map.Entry<String, Object> entry : queryData.entrySet()) {
            String columnName = entry.getKey();
            Object value = entry.getValue();
            int type = table.getColumns().get(columnName).getType();
            setPrepareStatement(ps, idx++, value, type);
        }
    }

    private String createSelectSql(SqlTable table, List<String> columns, Map<String, Object> queryData) {
        StringBuilder builder = new StringBuilder(200);
        builder.append("select ");
        Joiner.on(", ").skipNulls().appendTo(builder, columns);
        builder.append(" from ").append(table.getName()).append(" (nolock) where ");
        int idx = 0, length = queryData.size();
        for (String queryColumn : queryData.keySet()) {
            if (idx != 0) {
                builder.append(" and ");
            }
            builder.append(queryColumn).append(" = ? ");
            idx++;
        }
        return builder.toString();
    }
}
