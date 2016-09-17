package com.pangpang.dao.codegenerator;

import com.pangpang.dao.yixindb.SqlColumn;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * Created by root on 16-7-3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jdbc/jdbc-beans-jdbc.xml")
public class PangpangCodeGeneratorTest {
    @Autowired
    private PangpangCodeGenerator pangpangCodeGenerator;

    @Test
    public void testGenerate(){
        pangpangCodeGenerator.generate("student", "/usr/local/generate");
    }

    @Test
    public void testGetSqlColumnsInfo(){
        List list = pangpangCodeGenerator.getSqlColumnsInfo("student");
        System.out.println(list);
    }

    @Test
     public void countTest() throws SQLException {
        DatabaseMetaData databaseMetaData = pangpangCodeGenerator.getDataSource().getConnection().getMetaData();

        ResultSet resultSet = databaseMetaData.getColumns(null, null, "student", null);
        while (resultSet.next()) {
            int jdbcType = resultSet.getInt("DATA_TYPE");
            String columnName = resultSet.getString("COLUMN_NAME");
            String isAutoincrement = resultSet.getString("IS_AUTOINCREMENT");
            final SqlColumn col = new SqlColumn(columnName, jdbcType, StringUtils.equals(isAutoincrement, "YES"));
            System.out.println("----------------");
            System.out.println(jdbcType);
            System.out.println(columnName);
            System.out.println(isAutoincrement);
            System.out.println(col);
        }
        //ResultSet resultSet2 = databaseMetaData.getPrimaryKeys(null, null, "student");



        //System.out.println(resultSet.getRow());
        //System.out.println(resultSet2.getRow());
        //System.out.println(resultSet);
    }
}
