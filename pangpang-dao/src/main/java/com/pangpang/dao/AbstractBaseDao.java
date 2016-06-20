package com.pangpang.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.io.Serializable;

/**
 * Created by jiangjg on 2016/6/20.
 */
public abstract class AbstractBaseDao<TEntity, ID extends Serializable> implements BaseDao<TEntity, ID>{

    public abstract DataSource getDataSource();
    public abstract void setDataSource(DataSource dataSource);

    private SimpleJdbcCall simpleJdbcCall;
    protected SimpleJdbcCall getSimpleJdbcCall(){
        if(simpleJdbcCall == null){
            simpleJdbcCall = new SimpleJdbcCall(getDataSource());
        }
        return simpleJdbcCall;
    }

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    /**
     * NamedParameterJdbcTemplate
     *
     * @return
     */
    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        if (namedParameterJdbcTemplate == null){
            namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
        }
        return namedParameterJdbcTemplate;
    }

    private JdbcTemplate jdbcTemplate;
    protected JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null){
            jdbcTemplate = new JdbcTemplate(getDataSource());
        }
        return jdbcTemplate;
    }

    public AbstractBaseDao() {
        entityClass = (Class<TEntity>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        idClass = (Class<ID>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    Class<TEntity> entityClass;
    /**
     * 获取实体类类型
     *
     * @return
     */
    protected Class<TEntity> getEntityClass() {
        return entityClass;
    }

    Class<ID> idClass;

    /**
     * 获取主键类型
     *
     * @return
     */
    protected Class<ID> getTKeyClass() {
        return idClass;
    }

    /**
     * 表名
     *
     * @return
     */
    protected abstract String getTABLE_NAME();

    /**
     * 全表数据数量sql
     *
     * @return
     */
    protected abstract String getCOUNT_SQL_PATTERN();

    /**
     * 全表数据sql
     *
     * @return
     */
    protected abstract String getALL_SQL_PATTERN();

    /**
     * 分页sql
     *
     * @return
     */
    protected abstract String getPAGE_SQL_PATTERN();

    /**
     * 获取select时需要的col名称
     * @return
     */
    protected abstract String getColumnNames();
}
