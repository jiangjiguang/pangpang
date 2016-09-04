package com.pangpang.dao.yidb;

import com.google.common.collect.Maps;
import com.pangpang.dao.pangpangdb.dao.StudentDao;
import com.pangpang.dao.pangpangdb.entity.Student;
import com.pangpang.dao.yixindb.DBMeta;
import com.pangpang.dao.yixindb.SqlTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by jiangjg on 2016/8/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jdbc/jdbc-beans-jdbc.xml")
public class DBMetaTest {
    public static void main(String[] args) throws Exception {
        //DataSource dataSource =
        DBMeta meta = new DBMeta(null);
        meta.loadMeta();

        SqlTable table = meta.getSqlTable("student");
        Map<String, Object> update = Maps.newHashMap();
        update.put("cp", "CP001");
        Map<String, Object> insert = meta.getSqlOperation().insert(table, update);
    }

    @Autowired
    @Qualifier("pangpangdbDataSource")
    private DataSource dataSource;


    @Test
    public void countTest() throws Exception {
        //DataSource dataSource =
        DBMeta meta = new DBMeta(dataSource);
        meta.loadMeta();

        SqlTable table = meta.getSqlTable("student");
        Map<String, Object> update = Maps.newHashMap();
        update.put("cp", "CP001");
        Map<String, Object> insert = meta.getSqlOperation().insert(table, update);
    }
}
