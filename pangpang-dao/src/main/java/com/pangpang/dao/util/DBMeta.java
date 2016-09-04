package com.pangpang.dao.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiangjg on 2016/6/16.
 */
public class DBMeta {
    //private static Logger logger = LoggerFactory.getLogger(DBMeta.class);
    private static DBMeta dbMeta = null;
    private DBMeta(){

    }

    public static DBMeta getInstance(){
        if(dbMeta == null){
            dbMeta = new DBMeta();
        }
        return dbMeta;
    }

    private Map<String, List<ParameterMetaData>> parameterMetaDataListMap = new ConcurrentHashMap<>();

    private String getFullName(String spName, Connection connection){
        ResultSet resultSet = null;
        List<String> fullSPName = new ArrayList();
        try{
            resultSet = connection.getMetaData().getProcedures(null, null, spName);
            System.out.println(resultSet.getRow());
            while(resultSet.next()) {
                fullSPName.add(resultSet.getString("PROCEDURE_CAT") + "." + resultSet.getString("PROCEDURE_SCHEM") + "." + resultSet.getString("PROCEDURE_NAME"));
            }
            if(fullSPName.size() < 1){
                //logger.error("存储过程不存在：" + spName);
            }
            if(fullSPName.size() > 1){
                //logger.error("发现多个同名存储过程：" + spName);
            }
        }catch (Exception ex){
            //logger.error("获取存储过程全名异常" + ExceptionUtils.getFullStackTrace(ex));
        }finally {
            try{
                if(resultSet != null && !resultSet.isClosed()){
                    resultSet.close();
                }
            }catch (Exception ex){
               // logger.warn("关闭结果集异常：" + ExceptionUtils.getFullStackTrace(ex));
            }

        }
        return fullSPName.get(0);
    }

    public List<ParameterMetaData> getParameterMetaDataList(String spName, Connection connection){
        String fullName = getFullName(spName, connection);
        ResultSet resultSet = null;
        try{
            if(!parameterMetaDataListMap.containsKey(fullName)){
                List<ParameterMetaData> parameterMetaDataList = new ArrayList<>();
                resultSet =  connection.getMetaData().getProcedureColumns(null, null, spName, null);
                System.out.println(resultSet.getRow());
                while(resultSet.next()) {
                    String columnName = resultSet.getString("COLUMN_NAME");
                    int columnType = resultSet.getInt("COLUMN_TYPE");
                    if(columnName == null && (columnType == 1 || columnType == 2 || columnType == 4)) {
                       // logger.warn("Skipping metadata for: " + columnType + " " + resultSet.getInt("DATA_TYPE") + " " + resultSet.getString("TYPE_NAME") + " " + resultSet.getBoolean("NULLABLE") + " (probably a member of a collection)");
                    } else {
                        ParameterMetaData meta = new ParameterMetaData(columnName.substring(1), columnType, resultSet.getInt("DATA_TYPE"), resultSet.getString("TYPE_NAME"), resultSet.getBoolean("NULLABLE"));
                        parameterMetaDataList.add(meta);
                    }
                }
                parameterMetaDataListMap.put(fullName, parameterMetaDataList);
            }
        }catch (Exception ex){
           // logger.error("获取存储过程参数异常：" + ExceptionUtils.getFullStackTrace(ex));
        }finally {
            try{
                if(resultSet != null && !resultSet.isClosed()){
                    resultSet.close();
                }
            }catch (Exception ex){
               // logger.warn("关闭结果集异常：" + ExceptionUtils.getFullStackTrace(ex));
            }
        }
        return parameterMetaDataListMap.get(fullName);
    }

}
