package com.pangpang.dao.codegenerator;

import com.pangpang.util.FileUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by root on 16-6-26.
 */
@Service
public class PangpangCodeGenerator {
    Logger logger = Logger.getLogger(PangpangCodeGenerator.class);
    private DataSource dataSource;

    @Autowired
    @Qualifier("pangpangCodeGeneratorDataSource")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private static final String RT_1 = "\r\n";
    private static final String RT_2 = RT_1+RT_1;
    private static final String BLANK_1 =" ";
    private static final String BLANK_4 ="    ";
    private static final String BLANK_8 =BLANK_4 + BLANK_4;


    public void generate(String tableName, String pathDir){
        if(!StringUtils.isBlank(pathDir) && !pathDir.endsWith("/")){
            pathDir += "/";
        }
        String entityPath = pathDir + "/" + tableName + ".java";
        String daoPath = pathDir + "/" + tableName + "Dao.java";
        FileUtil.createFile(entityPath, "r");
        FileUtil.createFile(daoPath, "r");

        List<SqlColumn> sqlColumnList =  getSqlColumnsInfo(tableName);

        logger.info("123456");



    }


    //获取表的主键信息
    private List<String> getPrimaryKeys(String tableName) {
        List<String> primaryKeyList = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        try{
            connection = dataSource.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            resultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                primaryKeyList.add(columnName);
            }
        }catch (Exception ex){
            logger.error(ExceptionUtils.getFullStackTrace(ex));
        }finally {
            try{
                if(resultSet != null && !resultSet.isClosed() ){
                    resultSet.close();
                }
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            }catch (Exception ex){
                logger.error(ExceptionUtils.getFullStackTrace(ex));
            }
        }
        return primaryKeyList;
    }

    //获取表的列信息
    public  List<SqlColumn> getSqlColumnsInfo(String tableName){
        List<SqlColumn> sqlColumnList = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        try{
            connection = dataSource.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            resultSet = databaseMetaData.getColumns(null, null, tableName, null);
            while (resultSet.next()) {
                SqlColumn sqlColumn = new SqlColumn();
                //int jdbcType = resultSet.getInt("DATA_TYPE");
                //String TYPE_NAME = resultSet.getString("TYPE_NAME");
                String columnName = resultSet.getString("COLUMN_NAME");
                String isAutoincrement = resultSet.getString("IS_AUTOINCREMENT");
                sqlColumn.setName(columnName);
                boolean autoincrement = false;
                if(!StringUtils.isBlank((isAutoincrement)) && isAutoincrement.equals("YES")){
                    autoincrement = true;
                }
                sqlColumn.setAutoIncrement(autoincrement);
                sqlColumnList.add(sqlColumn);
            }
        }catch (Exception ex){
            logger.error(ExceptionUtils.getFullStackTrace(ex));
        }finally {
            try {
                if(resultSet != null && !resultSet.isClosed() ){
                    resultSet.close();
                }
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            }catch (Exception ex){
                logger.error(ExceptionUtils.getFullStackTrace(ex));
            }
        }

        List<String> primaryKeyList = getPrimaryKeys(tableName);
        for(SqlColumn sqlColumn : sqlColumnList){
            if(primaryKeyList.contains(sqlColumn.getName())){
                sqlColumn.setPrimaryKey(true);
            }else{
                sqlColumn.setPrimaryKey(false);
            }
        }
        return sqlColumnList;
    }

    
}
