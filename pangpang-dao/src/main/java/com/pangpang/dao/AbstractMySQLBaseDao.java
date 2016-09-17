package com.pangpang.dao;

import com.pangpang.dao.util.ColumnInfo;
import com.pangpang.dao.util.ParseAnnotation;
import com.pangpang.dao.util.TableInfo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangjg on 2016/6/20.
 */
public abstract class AbstractMySQLBaseDao<TEntity, ID extends Serializable> extends AbstractBaseDao<TEntity, ID> {
    String tableName;
    String insertSql;
    String updateSql;
    String deleteSql;
    String queyBasicSql;
    String colNames;
    String queryByIdSql;
    String queryPageSql;

    TableInfo tableInfo;

    public AbstractMySQLBaseDao() {
        tableInfo = ParseAnnotation.parseEntity(getEntityClass());
        tableName = tableInfo.getTableName();

        StringBuilder sbCols = new StringBuilder();
        StringBuilder sbPara = new StringBuilder();

        insertSql = "";
        int count = 0;
        for (ColumnInfo cInfo : tableInfo.getColumnInfos()) {
            if("dataChange_LastTime".equalsIgnoreCase(cInfo.getColumnName()))
                continue;

            if (cInfo.isIdentity() == false) {
                if (count > 0) {
                    sbCols.append(",");
                    sbPara.append(",");
                }
                sbCols.append(cInfo.getColumnName());
                sbPara.append(":" + cInfo.getFieldName());
                count++;
            }
        }
        insertSql = String.format("insert into %s (%s) values (%s)", tableName, sbCols.toString(), sbPara.toString());

        sbCols = new StringBuilder();
        sbPara = new StringBuilder();
        StringBuilder sbCondition = new StringBuilder();
        int conditionCount = 0;
        count = 0;
        StringBuilder sbConditionById = new StringBuilder();
        for (ColumnInfo cInfo : tableInfo.getColumnInfos()) {
            if("dataChange_LastTime".equalsIgnoreCase(cInfo.getColumnName()))
                continue;

            if (cInfo.isPk()) {
                if (conditionCount > 0) {
                    sbCondition.append(" and ");
                    sbConditionById.append(" and ");
                }
                sbCondition.append(cInfo.getColumnName() + "=:" + cInfo.getFieldName());
                sbConditionById.append(cInfo.getColumnName() + " = ?");
                conditionCount++;
            } else if (cInfo.isIdentity() == false) {
                if (count > 0) {
                    sbCols.append(",");
                }
                sbCols.append(cInfo.getColumnName() + " = IFNULL(:" + cInfo.getFieldName() + "," + cInfo.getColumnName() + ")");
                count++;
            }
        }
        updateSql = String.format("update %s set datachange_lasttime = now(),%s where %s", tableName, sbCols.toString(), sbCondition.toString());
        // org.springframework.jdbc.core.ColumnMapRowMapper c;
        // org.springframework.jdbc.core.SingleColumnRowMapper<T>
        // columnRowMapper;
        // JdbcUtils
        deleteSql = String.format("delete from %s where %s", tableName, sbCondition.toString());


        sbCols = new StringBuilder();
        count = 0;
        for (ColumnInfo cInfo : tableInfo.getColumnInfos()) {

            if (count > 0) {
                sbCols.append(",");
            }
            sbCols.append(cInfo.getColumnName() + " as " + cInfo.getFieldName());
            count++;
        }

        queyBasicSql = String.format("SELECT %s FROM %s ", sbCols.toString(), getTABLE_NAME());
        queryByIdSql = String.format("%s where %s", queyBasicSql, sbConditionById.toString());
        colNames = sbCols.toString();
    }

    @Override
    protected String getTABLE_NAME() {
        return tableName;
    }

    @Override
    protected String getCOUNT_SQL_PATTERN() {
        return String.format("SELECT count(1) from %s", getTABLE_NAME());
    }

    @Override
    protected String getALL_SQL_PATTERN() {
        return queyBasicSql;
    }

    @Override
    protected String getPAGE_SQL_PATTERN() {
        return queyBasicSql + " %s LIMIT %s, %s";
    }

    protected String getInsertSql() {
        return insertSql;
    }

    protected String getUpdateSql() {
        // "update t_actor set first_name = :firstName, last_name = :lastName
        // where id = :id"
        return updateSql;
    }

    protected String getDeleteSql() {

        return deleteSql;
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
        // queryByIdSql
        TEntity entity = null;
        try {
            List<TEntity> list = getJdbcTemplate().query(queryByIdSql, new Object[] { key }, new BeanPropertyRowMapper<TEntity>(getEntityClass()));
            // List<TEntity> list = getJdbcTemplate().queryForList(queryByIdSql,
            // new Object[] { key }, getEntityClass());
            if (list != null && !list.isEmpty())
                return list.get(0);
        } catch (Exception exception) {
            throw exception;
        }
        return entity;
    }


    /**
     *
     * @param pageSize 每页数量，必须大于0
     * @param pageNo 从1开始
     * @return
     * @throws SQLException
     */
    @Override
    public List<TEntity> queryByPage(int pageSize, int pageNo) throws SQLException {
        return queryByPage(pageSize,pageNo," DataChange_LastTime desc ");
    }

    /**
     *
     * @param pageSize
     * @param pageNo
     * @param orderByCondition
     * @return
     * @throws SQLException
     */
    @Override
    public List<TEntity> queryByPage(int pageSize, int pageNo,String orderByCondition)  throws SQLException{
        if (pageNo < 1 || pageSize < 1)
            throw new SQLException("Illigal pagesize or pageNo, pls check");
        String sql = "";
        sql = String.format(getPAGE_SQL_PATTERN()," order by " + orderByCondition,(pageNo - 1) * pageSize, pageSize);

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
        List<TEntity> daoPojos = new ArrayList<>();
        daoPojos.add(daoPojo);
        return insert(daoPojos)[0];
    }

    /**
     * SQL insert Note: there must be one non-null field in daoPojo
     **/
    @Override
    public int[] insert(List<TEntity> daoPojos) throws SQLException {
		/*
		 * // Retrieving auto-generated keys KeyHolder keyHolder = new
		 * GeneratedKeyHolder(); getJdbcTemplate().update(new
		 * PreparedStatementCreator() { public PreparedStatement
		 * createPreparedStatement(Connection connection) throws SQLException {
		 * PreparedStatement ps = connection.prepareStatement("", new String[] {
		 * "id" }); ps.setString(1, name); return ps; } }, keyHolder);
		 */
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(daoPojos.toArray());
        // "update t_actor set first_name = :firstName, last_name = :lastName
        // where id = :id"
        int[] updateCounts = getNamedParameterJdbcTemplate().batchUpdate(getInsertSql(), batch);
        return updateCounts;
    }

    /**
     * SQL delete Note: there must be one non-null field in daoPojo
     **/
    @Override
    public int delete(TEntity daoPojo) throws SQLException {
        List<TEntity> daoPojos = new ArrayList<>();
        daoPojos.add(daoPojo);
        return delete(daoPojos)[0];
    }

    /**
     * SQL delete Note: there must be one non-null field in daoPojo
     **/
    @Override
    public int[] delete(List<TEntity> daoPojos) throws SQLException {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(daoPojos.toArray());
        // "update t_actor set first_name = :firstName, last_name = :lastName
        // where id = :id"
        int[] updateCounts = getNamedParameterJdbcTemplate().batchUpdate(getDeleteSql(), batch);
        return updateCounts;
    }

    /**
     * SQL update Note: there must be one non-null field in daoPojo
     **/
    @Override
    public int update(TEntity daoPojo) throws SQLException {
        List<TEntity> daoPojos = new ArrayList<>();
        daoPojos.add(daoPojo);
        return update(daoPojos)[0];
    }

    /**
     * SQL update Note: there must be one non-null field in daoPojo
     **/
    @Override
    public int[] update(final List<TEntity> daoPojos) throws SQLException {

        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(daoPojos.toArray());
        // "update t_actor set first_name = :firstName, last_name = :lastName
        // where id = :id"
        int[] updateCounts = getNamedParameterJdbcTemplate().batchUpdate(getUpdateSql(), batch);
        return updateCounts;

    }
}
