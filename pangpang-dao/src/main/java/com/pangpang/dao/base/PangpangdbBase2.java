package com.pangpang.dao.base;

import com.pangpang.dao.AbstractMySQLBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Serializable;

/**
 * Created by jiangjg on 2016/6/20.
 */
public class PangpangdbBase2<TEntity, ID extends Serializable> extends AbstractMySQLBaseDao<TEntity, ID> {

    private DataSource dataSource;

    @Autowired
    @Qualifier("pangpangdb2DataSource")
    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

}
