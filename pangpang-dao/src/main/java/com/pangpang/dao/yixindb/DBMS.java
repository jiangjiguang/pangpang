package com.pangpang.dao.yixindb;

/**
 * Created by yxjiang on 2016/2/25.
 */
public enum DBMS {
    SQL_SERVER("Microsoft SQL Server"), MYSQL("MySQL");

    String productName;
    DBMS(String productName) {
        this.productName = productName;
    }

    public static DBMS byProductName(String productName) {
        for(DBMS dbms :DBMS.values()) {
            if (dbms.productName.equals(productName)) {
                return dbms;
            }
        }
        return null;
    }
}
