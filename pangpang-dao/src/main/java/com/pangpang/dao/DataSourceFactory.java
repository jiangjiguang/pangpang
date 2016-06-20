package com.pangpang.dao;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by jiangjg on 2016/6/20.
 */
public class DataSourceFactory {
    private static Map<String, DataSource> dataSources;

    public DataSourceFactory() {
        dataSources = new java.util.concurrent.ConcurrentHashMap<>();
    }

    public DataSource getDataSource(String dbKeyName) {
        return null;
    }
    public void destroy() {
        if (dataSources != null) {
            for (DataSource dataSource : dataSources.values())
                try {
                    Connection connection = dataSource.getConnection();
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

}
