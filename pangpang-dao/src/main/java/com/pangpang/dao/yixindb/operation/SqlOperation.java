package com.pangpang.dao.yixindb.operation;


import com.pangpang.dao.yixindb.SqlColumn;
import com.pangpang.dao.yixindb.SqlTable;

import java.util.List;
import java.util.Map;

/**
 * Created by yxjiang on 2016/2/25.
 */
public interface SqlOperation {
    List<String> getTables(String schema) throws Exception;

    List<String> getPrimaryKeys(String schema, String table) throws Exception;

    List<SqlColumn> getColumns(String schema, String table) throws Exception;

    Map<String, Object> insert(SqlTable table, Map<String, Object> data) throws Exception;

    List<String> getIndexInfo(String schema, String table) throws Exception;

    int update(SqlTable table, Map<String, Object> updateData, Map<String, Object> queryData) throws Exception;

    List<Map<String, Object>> select(SqlTable table, List<String> columns, Map<String, Object> queryData) throws Exception;
}
