package com.pangpang.dao.codegenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;

/**
 * Created by root on 16-6-26.
 */
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

    
}
