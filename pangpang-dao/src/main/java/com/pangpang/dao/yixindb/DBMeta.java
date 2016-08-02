package com.pangpang.dao.yixindb;

import com.google.common.collect.Maps;
import com.pangpang.dao.yixindb.operation.MysqlOperation;
import com.pangpang.dao.yixindb.operation.SqlOperation;
import com.pangpang.dao.yixindb.operation.SqlServerOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yxjiang on 2016/2/25.
 */
public class DBMeta {
    private static final long EXPIRE_MILLIS = DateUtils.MILLIS_PER_MINUTE;
    private DataSource dataSource;
    private SqlOperation sqlOperation;
    List<String> tables;
    private Map<String, CacheNode> tableMap = new ConcurrentHashMap<>();

    public DBMeta(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void loadMeta() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String databaseProductName = connection.getMetaData().getDatabaseProductName();
            DBMS dbms = DBMS.byProductName(databaseProductName);
            Assert.notNull(dbms, "未能找到数据库产品名" + databaseProductName + "对应的DBMS枚举。");
            switch (dbms) {
                case SQL_SERVER:
                    sqlOperation = new SqlServerOperation(dataSource);
                    break;
                case MYSQL:
                    sqlOperation = new MysqlOperation(dataSource);
                    break;
            }
            Assert.notNull(sqlOperation, databaseProductName + "的sqlOperation没有实现。");
        }
        tables = sqlOperation.getTables(null);
    }

    public SqlTable getSqlTable(String tableName) {
        CacheNode sqlTableNode = tableMap.get(tableName);
        if (sqlTableNode == null) {
            try {
                sqlTableNode = new CacheNode(buildSqlTable(tableName), System.currentTimeMillis());
                tableMap.put(tableName, sqlTableNode);
            } catch (Exception e) {
            }
        } else if (sqlTableNode.getTimestamp() + EXPIRE_MILLIS < System.currentTimeMillis()) {
            sqlTableNode.refresh();
        }
        return sqlTableNode == null ? null : sqlTableNode.getNode();
    }

    public SqlOperation getSqlOperation() {
        return sqlOperation;
    }

    private SqlTable buildSqlTable(String table) throws Exception {
        List<SqlColumn> columns = sqlOperation.getColumns(null, table);
        List<String> primaryKeyNames = sqlOperation.getPrimaryKeys(null, table);
        List<String> indexedColumns = sqlOperation.getIndexInfo(null, table);
        Map<String, SqlColumn> primaryKeysMap = Maps.newLinkedHashMap();
        Map<String, SqlColumn> columnsMap = Maps.newLinkedHashMap();
        Map<String, SqlColumn> indexMap = Maps.newLinkedHashMap();
        for (SqlColumn column : columns) {
            String columnName = column.getName();
            columnsMap.put(columnName, column);
            if (primaryKeyNames.contains(columnName)) {
                primaryKeysMap.put(columnName, column);
            }
            if (indexedColumns.contains(columnName)) {
                indexMap.put(columnName, column);
            }
        }
        return new SqlTable(table, primaryKeysMap, columnsMap, indexMap);
    }

    class CacheNode {
        private SqlTable node;
        private long timestamp;
        private volatile boolean refreshing = false;

        public CacheNode(SqlTable node, long timestamp) {
            this.node = node;
            this.timestamp = timestamp;
        }

        public SqlTable getNode() {
            return node;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void refresh() {
            if (node != null && !refreshing) {
                synchronized (this) {
                    if (!refreshing && timestamp + EXPIRE_MILLIS < System.currentTimeMillis()) {
                        refreshing = true;
                        final String tableName = node.getName();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    node = buildSqlTable(tableName);
                                    timestamp = System.currentTimeMillis();
                                } catch (Exception e) {
                                }
                                refreshing = false;
                            }
                        }).start();
                    }
                }
            }
        }
    }
}
