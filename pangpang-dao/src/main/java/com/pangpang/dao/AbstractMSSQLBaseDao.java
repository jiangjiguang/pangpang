package com.pangpang.dao;

import com.pangpang.dao.util.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangjg on 2016/6/20.
 */
public abstract class AbstractMSSQLBaseDao<TEntity , ID extends Serializable> extends AbstractBaseDao<TEntity, ID>{

    protected String primaryKeysField;  //类主键名
    protected String primaryKeysColumn; //表主键名
    protected List<String> arrIdentityKey = new ArrayList<>(); //自增主键名
    protected String tableName;        //表名
    protected String queyBasicSql;
    protected String colNames;
    protected String queryByIdSql;
    protected TableInfo tableInfo;
    protected Map<String, String> columnName2FieldName = new HashMap<>();
    protected Map<String, String> fieldName2ColumnName = new HashMap<>();

    public AbstractMSSQLBaseDao(){
        tableInfo = ParseAnnotation.parseEntity(getEntityClass());
        tableName = tableInfo.getTableName();

        int conditionCount = 0;
        int count = 0;
        StringBuilder sbPrimaryKeysColumn = new StringBuilder();
        StringBuilder sbPrimaryKeysField = new StringBuilder();

        StringBuilder sbConditionById = new StringBuilder();
        for (ColumnInfo cInfo : tableInfo.getColumnInfos()) {
            if (cInfo.isPk()) {
                if(cInfo.isIdentity()){
                    arrIdentityKey.add(cInfo.getColumnName() + "," + cInfo.getFieldName());
                }
                if (conditionCount > 0) {
                    sbConditionById.append(" and ");
                    sbPrimaryKeysColumn.append(" , ");
                    sbPrimaryKeysField.append(" , ");
                }

                sbConditionById.append(cInfo.getColumnName() + " = ?");
                sbPrimaryKeysColumn.append(cInfo.getColumnName());
                sbPrimaryKeysField.append(cInfo.getFieldName());
                conditionCount++;
            }
        }
        primaryKeysColumn = sbPrimaryKeysColumn.toString();
        primaryKeysField = sbPrimaryKeysField.toString();

        StringBuilder sbCols = new StringBuilder();
        count = 0;
        for (ColumnInfo cInfo : tableInfo.getColumnInfos()) {
            if (count > 0) {
                sbCols.append(",");
            }
            columnName2FieldName.put(cInfo.getColumnName(), cInfo.getFieldName());
            fieldName2ColumnName.put(cInfo.getFieldName(), cInfo.getColumnName());
            sbCols.append(cInfo.getColumnName() + " as " + cInfo.getFieldName());
            count++;
        }
        queyBasicSql = String.format("SELECT %s FROM %s (nolock)", sbCols.toString(), getTABLE_NAME());
        queryByIdSql = String.format("%s where %s", queyBasicSql, sbConditionById.toString());
        colNames = sbCols.toString();
    }

    @Override
    protected String getTABLE_NAME() {
        return tableName;
    }

    @Override
    protected String getCOUNT_SQL_PATTERN() {
        return String.format("SELECT count(1) from %s (nolock)", getTABLE_NAME());
    }

    @Override
    protected String getALL_SQL_PATTERN() {
        return queyBasicSql;
    }

    @Override
    protected String getPAGE_SQL_PATTERN() {
        StringBuilder sbPageSql = new StringBuilder();
        sbPageSql.append("WITH CTE AS ");
        sbPageSql.append(String.format(" (SELECT tmpTable.* ,ROW_NUMBER() OVER ( ORDER BY %s ) as RowNumber FROM ", primaryKeysField));
        sbPageSql.append(String.format(" ( %s ) AS tmpTable ", queyBasicSql));
        sbPageSql.append(" ) SELECT * FROM CTE  with (nolock)  WHERE RowNumber between %s and %s ");
        return sbPageSql.toString();
    }

    @Override
    protected String getColumnNames(){
        return colNames;
    }
    /**
     * Get the records count
     **/
    @Override
    public int count() throws SQLException {
        Integer count = getNamedParameterJdbcTemplate().queryForObject(getCOUNT_SQL_PATTERN(), new HashMap<String, String>(), Integer.class);
        return count;
    }
    /**
     * Query TagdatatypeGen by simple primary key
     **/
    @Override
    public TEntity queryByPk(ID key) throws SQLException {
        TEntity entity = null;
        try {
            List<TEntity> list = getJdbcTemplate().query(queryByIdSql, new Object[] { key }, new BeanPropertyRowMapper<TEntity>(getEntityClass()));
            if (list != null && !list.isEmpty()){
                entity = list.get(0);
            }
        } catch (Exception exception) {
            throw exception;
        }
        return entity;
    }

    /**
     * Query TagdatatypeGen with paging function The pageSize and pageNo must be
     * greater than zero.
     **/
    @Override
    public List<TEntity> queryByPage(int pageSize, int pageNo) throws SQLException {
        if (pageNo < 1 || pageSize < 1) {
            throw new SQLException("Illigal pagesize or pageNo, pls check");
        }
        int fromRownum = (pageNo - 1) * pageSize + 1;
        int endRownum = pageSize * pageNo;
        String sql = String.format(getPAGE_SQL_PATTERN(), fromRownum, endRownum);
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<TEntity>(getEntityClass()));
    }

    @Override
    public List<TEntity> queryByPage(int pageSize, int pageNo,String orderByCondition) throws SQLException {
        if (pageNo < 1 || pageSize < 1) {
            throw new SQLException("Illigal pagesize or pageNo, pls check");
        }
        int fromRownum = (pageNo - 1) * pageSize + 1;
        int endRownum = pageSize * pageNo;

        StringBuilder sbPageSql = new StringBuilder();
        sbPageSql.append("WITH CTE AS ");
        sbPageSql.append(String.format(" (SELECT tmpTable.* ,ROW_NUMBER() OVER ( ORDER BY %s ) as RowNumber FROM ", orderByCondition));
        sbPageSql.append(String.format(" ( %s ) AS tmpTable ", queyBasicSql));
        sbPageSql.append(" ) SELECT * FROM CTE  with (nolock)  WHERE RowNumber between %s and %s ");


        String sql = String.format(sbPageSql.toString(), fromRownum, endRownum);
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<TEntity>(getEntityClass()));
    }


    /**
     * Get all records in the whole table
     **/
    @Override
    public List<TEntity> getAll() throws SQLException {
        List<TEntity> list = getJdbcTemplate().query(getALL_SQL_PATTERN(), new BeanPropertyRowMapper<TEntity>(getEntityClass()));
        return list;
    }

    /**
     * SQL insert Note: there must be one non-null field in daoPojo
     **/
    @Override
    public int insert(TEntity daoPojo) throws SQLException {
        DbUtil dbUtil = new DbUtil();
        int ret = 0;
        String spName = String.format("spA_%s_i", tableName);
        List<ParameterMetaData> parameterMetaDatas = DBMeta.getInstance().getParameterMetaDataList(spName, this.getDataSource().getConnection());
        String spSQL = dbUtil.getSpSql(spName, parameterMetaDatas);
        Map<Integer, Object> map = dbUtil.getEntityValue(daoPojo, parameterMetaDatas, columnName2FieldName);
        CallableStatement cstmt =  this.getDataSource().getConnection().prepareCall(spSQL);
        for(int item: map.keySet()){
            cstmt.setObject(item, map.get(item));
        }

        for(int i=0; i < parameterMetaDatas.size(); i++) {
            if (parameterMetaDatas.get(i).getParameterType() == 2 || parameterMetaDatas.get(i).getParameterType() == 3) {
                cstmt.registerOutParameter(i, parameterMetaDatas.get(i).getSqlType());
            }
        }
        boolean flag = cstmt.execute();
        dbUtil.setEntityValue(daoPojo, cstmt, parameterMetaDatas, columnName2FieldName);
        return ret;
    }

    /**
     * SQL insert Note: there must be one non-null field in daoPojo
     **/

    @Override
    public int[] insert(List<TEntity> daoPojos) throws SQLException {
        int[] ret = new int[daoPojos.size()];
        for(int i=0; i<daoPojos.size(); i++){
            ret[i] = insert(daoPojos.get(i));
        }
        return ret;
    }

    /**
     * SQL delete Note: there must be one non-null field in daoPojo
     **/
    @Override
    public int delete(TEntity daoPojo) throws SQLException {
        DbUtil dbUtil = new DbUtil();
        int ret = 0;
        String spName = String.format("spA_%s_d", tableName);
        List<ParameterMetaData> parameterMetaDatas = DBMeta.getInstance().getParameterMetaDataList(spName, this.getDataSource().getConnection());
        String spSQL = dbUtil.getSpSql(spName, parameterMetaDatas);
        Map<Integer, Object> map = dbUtil.getEntityValue(daoPojo, parameterMetaDatas, columnName2FieldName);
        CallableStatement cstmt =  this.getDataSource().getConnection().prepareCall(spSQL);
        for(int item: map.keySet()){
            cstmt.setObject(item, map.get(item));
        }

        for(int i=0; i < parameterMetaDatas.size(); i++) {
            if (parameterMetaDatas.get(i).getParameterType() == 2 || parameterMetaDatas.get(i).getParameterType() == 3) {
                cstmt.registerOutParameter(i, parameterMetaDatas.get(i).getSqlType());
            }
        }
        boolean flag = cstmt.execute();
        dbUtil.setEntityValue(daoPojo, cstmt, parameterMetaDatas, columnName2FieldName);
        return ret;
    }

    /**
     * SQL delete Note: there must be one non-null field in daoPojo
     **/

    @Override
    public int[] delete(List<TEntity> daoPojos) throws SQLException {
        int[] ret = new int[daoPojos.size()];
        for(int i=0; i<daoPojos.size(); i++){
            ret[i] = delete(daoPojos.get(i));
        }
        return ret;
    }
    /**
     * SQL update Note: there must be one non-null field in daoPojo
     **/
    @Override
    public int update(TEntity daoPojo) throws SQLException {
        DbUtil dbUtil = new DbUtil();
        int ret = 0;
        String spName = String.format("spA_%s_u", tableName);
        List<ParameterMetaData> parameterMetaDatas = DBMeta.getInstance().getParameterMetaDataList(spName, this.getDataSource().getConnection());
        String spSQL = dbUtil.getSpSql(spName, parameterMetaDatas);
        Map<Integer, Object> map = dbUtil.getEntityValue(daoPojo, parameterMetaDatas, columnName2FieldName);
        CallableStatement cstmt =  this.getDataSource().getConnection().prepareCall(spSQL);
        for(int item: map.keySet()){
            cstmt.setObject(item, map.get(item));
        }

        for(int i=0; i < parameterMetaDatas.size(); i++) {
            if (parameterMetaDatas.get(i).getParameterType() == 2 || parameterMetaDatas.get(i).getParameterType() == 3) {
                cstmt.registerOutParameter(i, parameterMetaDatas.get(i).getSqlType());
            }
        }
        boolean flag = cstmt.execute();
        dbUtil.setEntityValue(daoPojo, cstmt, parameterMetaDatas, columnName2FieldName);
        return ret;
    }

    /**
     * SQL update Note: there must be one non-null field in daoPojo
     **/

    @Override
    public int[] update(final List<TEntity> daoPojos) throws SQLException {
        int[] ret = new int[daoPojos.size()];
        for(int i=0; i<daoPojos.size(); i++){
            ret[i] = update(daoPojos.get(i));
        }
        return ret;
    }
}
