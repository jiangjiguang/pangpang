package com.pangpang.dao.yidb;

import com.google.common.collect.Maps;
import com.pangpang.dao.yixindb.DBMeta;
import com.pangpang.dao.yixindb.SqlTable;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by jiangjg on 2016/8/2.
 */
public class DBMetaTest {
    public static void main(String[] args) throws Exception {
        //DataSource dataSource =
        DBMeta meta = new DBMeta(null);
        meta.loadMeta();

        SqlTable table = meta.getSqlTable("RISK_DEAL_INFO");
        Map<String, Object> update = Maps.newHashMap();
        update.put("cp", "CP001");
        Map<String, Object> insert = meta.getSqlOperation().insert(table, update);
    }
}
