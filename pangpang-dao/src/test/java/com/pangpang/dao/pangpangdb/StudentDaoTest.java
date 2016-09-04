package com.pangpang.dao.pangpangdb;

import com.pangpang.dao.pangpangdb.dao.StudentDao;
import com.pangpang.dao.pangpangdb.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

/**
 * Created by root on 16-6-26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jdbc/jdbc-beans-jdbc.xml")
public class StudentDaoTest {
    @Autowired
    private StudentDao studentDao;

    @Test
    public void countTest() throws SQLException {
        System.out.println(studentDao.count());
        Student student = studentDao.queryByPk(1);
        studentDao.insert(student);

    }

}
