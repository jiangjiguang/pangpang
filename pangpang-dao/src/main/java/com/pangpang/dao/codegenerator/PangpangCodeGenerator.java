package com.pangpang.dao.codegenerator;

import com.pangpang.util.FileUtil;
import com.pangpang.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;


/**
 * Created by root on 16-6-26.
 */
@Service
public class PangpangCodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PangpangCodeGenerator.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("jdbc/jdbc-beans-jdbc.xml");

        PangpangCodeGenerator generator = applicationContext.getBean(PangpangCodeGenerator.class);
        generator.generate("student", "/usr/local/generate");
    }

    //所有的类型
    private static Map<Integer, String> typeMap  = null;
    static{
        if(typeMap == null){
            typeMap = new HashMap<>();

            typeMap.put(Types.INTEGER, "Integer");
            typeMap.put(Types.BIGINT, "Long");
            typeMap.put(Types.BIT, "Boolean");
            typeMap.put(Types.CHAR, "String");
            typeMap.put(Types.DATE, "Timestamp");
            typeMap.put(Types.DECIMAL, "BigDecimal");
            typeMap.put(Types.DOUBLE, "BigDecimal");
            typeMap.put(Types.FLOAT, "BigDecimal");
            typeMap.put(Types.NCHAR, "String");
            typeMap.put(Types.NVARCHAR, "String");
            typeMap.put(Types.VARCHAR, "String");
            typeMap.put(Types.TIME, "Timestamp");
            typeMap.put(Types.TIMESTAMP, "Timestamp");
            typeMap.put(Types.TINYINT, "Byte");
        }
    }


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
        List<String> contentList = new ArrayList<>();
        contentList.add(String.format("%s%s", "import javax.persistence.*;", RT_1));

        contentList.add("@Entity");
        contentList.add(String.format("@Table(name=\"%s\")", tableName));
        contentList.add(String.format("public class %s {", tableName));

        /*
            @Column(name="name")
            private String name;
         */

        //shuxing
        if(sqlColumnList != null && sqlColumnList.size() > 0){
            for(SqlColumn item : sqlColumnList){
                if(item.isPrimaryKey() || item.isAutoIncrement()){
                    String generationType = "GenerationType.IDENTITY";
                    if(!item.isAutoIncrement()){
                        generationType = "GenerationType.AUTO";
                    }
                    contentList.add(String.format("%s@Id%s" +
                                    "%s@GeneratedValue(strategy = %s)%s" +
                                    "%s@Column(name=\"%s\")%s" +
                                    "%sprivate %s %s;%s",
                            BLANK_4,
                            RT_1, BLANK_4, generationType, RT_1,
                            BLANK_4, item.getName(), RT_1,
                            BLANK_4, MapUtils.getString(typeMap, item.getType(), "String"),  StringUtil.lowerName(item.getName()), RT_1));
                }else{
                    contentList.add(String.format("%s@Column(name=\"%s\")%s" +
                                    "%sprivate %s %s;%s",
                            BLANK_4, item.getName(), RT_1,
                            BLANK_4, MapUtils.getString(typeMap, item.getType(), "String"),  StringUtil.lowerName(item.getName()), RT_1));
                }
            }
        }

        //getter and setter
        if(sqlColumnList != null && sqlColumnList.size() > 0){
            for(SqlColumn item : sqlColumnList){
                contentList.add(String.format("%s%spublic%s%s%s%s() {%s" +
                                "%sreturn%s%s;%s" +
                                "%s}",
                        RT_1, BLANK_4, BLANK_1, MapUtils.getString(typeMap, item.getType(), "String"), BLANK_1,
                        "get" + StringUtil.captureName(item.getName()), RT_1,
                        BLANK_8, BLANK_1, StringUtil.lowerName(item.getName()), RT_1,
                        BLANK_4));

                contentList.add(String.format("%s%spublic %s %s(%s %s) {%s" +
                                "%s%s = %s;%s" +
                                "%s}",
                        RT_1, BLANK_4, "void", "set" + StringUtil.captureName(item.getName()), MapUtils.getString(typeMap, item.getType(), "String"), StringUtil.lowerName(item.getName()), RT_1,
                        BLANK_8, "this." + StringUtil.lowerName(item.getName()), StringUtil.lowerName(item.getName()),RT_1,
                        BLANK_4));
            }
        }

        /*
     public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
         */


        contentList.add("}");


        try {
            FileUtils.writeLines(new File(entityPath), contentList, true);
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getFullStackTrace(ex));
        }
        System.out.println(123456);


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
                int jdbcType = resultSet.getInt("DATA_TYPE");
                String columnName = resultSet.getString("COLUMN_NAME");
                String isAutoincrement = resultSet.getString("IS_AUTOINCREMENT");
                boolean autoincrement = false;
                if(!StringUtils.isBlank((isAutoincrement)) && isAutoincrement.equals("YES")){
                    autoincrement = true;
                }

                sqlColumn.setType(jdbcType);
                sqlColumn.setName(columnName);
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
