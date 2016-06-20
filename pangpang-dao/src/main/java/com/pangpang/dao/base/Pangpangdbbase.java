package com.pangpang.dao.base;

import com.pangpang.dao.AbstractMSSQLBaseDao;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Serializable;

/**
 * Created by jiangjg on 2016/6/20.
 */
public class PangpangdbBase <TEntity, ID extends Serializable> extends AbstractMSSQLBaseDao<TEntity, ID> {
    private DataSource dataSource;

    @Resource(name = "pangpangdbDataSource")
    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
