package com.pangpang.dao.codegenerator;

import com.pangpang.dao.yixindb.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-6-26.
 */
@Service
public class PangpangCodeGenerator {

    private DataSource dataSource;
//http://www.cnblogs.com/hongten/archive/2013/02/24/hongten_code_create.html

    @Autowired
    @Qualifier("pangpangCodeGeneratorDataSource")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private void getPrimaryKeys(String tableName) throws Exception {
        List<SqlColumn> sqlColumnList = new ArrayList<>();

        DatabaseMetaData databaseMetaData = dataSource.getConnection().getMetaData();
        ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            System.out.println(columnName);
        }
    }

    public  void getSqlColumn(String tableName) throws Exception {
        List<SqlColumn> sqlColumnList = new ArrayList<>();
        try{
            Connection connection = dataSource.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, null);
            while (resultSet.next()) {
                int jdbcType = resultSet.getInt("DATA_TYPE");
                String TYPE_NAME = resultSet.getString("TYPE_NAME");
                String columnName = resultSet.getString("COLUMN_NAME");
                String isAutoincrement = resultSet.getString("IS_AUTOINCREMENT");
                final com.pangpang.dao.yixindb.SqlColumn col = new com.pangpang.dao.yixindb.SqlColumn(columnName, jdbcType, StringUtils.equals(isAutoincrement, "YES"));
                System.out.println("----------------");
                System.out.println(TYPE_NAME);
                System.out.println(jdbcType);
                System.out.println(columnName);
                System.out.println(isAutoincrement);
                System.out.println(col);
            }
        }catch (Exception ex){

        }finally {

        }

    }

    
}
