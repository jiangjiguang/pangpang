package com.pangpang.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jiangjg on 2016/6/20.
 */
public interface BaseDao<TEntity,ID extends Serializable> {
    public int[] delete(List<TEntity> daoPojos) throws SQLException;
    public int delete(TEntity daoPojo) throws SQLException;

    public int[] insert(List<TEntity> daoPojos) throws SQLException;
    public int insert(TEntity daoPojo) throws SQLException;

    public int update(TEntity daoPojo) throws SQLException;
    public int[] update(List<TEntity> daoPojos) throws SQLException;

    public int count() throws SQLException;

    public List<TEntity> getAll() throws SQLException;

    public TEntity queryByPk(ID id)	throws SQLException;

    /**
     * 分页查询，默认根据DataChange_Lasttime倒序  order by DataChange_Lasttime desc
     * @param pageSize 每页数量，必须大于0
     * @param pageNo 从1开始
     * @return
     * @throws SQLException
     */
    public List<TEntity> queryByPage(int pageSize, int pageNo)  throws SQLException;
    /**
     * 分页查询，默认根据DataChange_Lasttime倒序  order by DataChange_Lasttime desc
     * @param pageSize 每页数量，必须大于0
     * @param pageNo 从1开始
     * @param orderByCondition 排序条件 如 DataChange_Lasttime desc
     * @return
     * @throws SQLException
     */
    public List<TEntity> queryByPage(int pageSize, int pageNo,String orderByCondition)  throws SQLException;
}