package com.pangpang.dao.util;

import java.sql.Connection;

/**
 * Created by root on 16-6-19.
 */
public class DBHelperTest {
    public static void main(String[] args) {
        Connection connection = DBHelper.getConnection();
        System.out.println(connection);
    }
}
