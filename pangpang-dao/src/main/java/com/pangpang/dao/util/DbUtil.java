package com.pangpang.dao.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangjg on 2016/6/17.
 */
public class DbUtil {
    private static Logger logger = LoggerFactory.getLogger(DbUtil.class);

    public <TEntity> void setEntityValue(TEntity daoPojo, CallableStatement cstmt, List<ParameterMetaData> parameterMetaDatas, Map<String, String> columnName2FieldName){
        for(int i=0; i < parameterMetaDatas.size(); i++) {
            if (parameterMetaDatas.get(i).getParameterType() == 2 || parameterMetaDatas.get(i).getParameterType() == 3) {
                try{
                    Object object = cstmt.getObject(i);
                    String columnName = parameterMetaDatas.get(i).getParameterName();
                    String fieldName = columnName2FieldName.get(columnName);
                    Field field = daoPojo.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(daoPojo, object);
                }catch (Exception ex){
                    logger.warn("设置属性值异常：" +  ExceptionUtils.getFullStackTrace(ex));
                }
            }
        }
    }

    public <TEntity> Map getEntityValue(TEntity daoPojo, List<ParameterMetaData> parameterMetaDatas, Map<String, String> columnName2FieldName){
        Map<Integer, Object> map = new HashMap<>();
        for(int i=0; i < parameterMetaDatas.size(); i++){
            if(parameterMetaDatas.get(i).getParameterType() == 1 || parameterMetaDatas.get(i).getParameterType() == 2){
                String columnName = parameterMetaDatas.get(i).getParameterName();
                String fieldName = columnName2FieldName.get(columnName);
                Object object = null;
                try{
                    Field field = daoPojo.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    object = field.get(daoPojo);
                }catch (Exception ex){
                    logger.warn("类属性不存在：" +  ExceptionUtils.getFullStackTrace(ex));
                }
                map.put(i, object);
            }

        }
        return map;
    }

    public String getSpSql(String spName, List<ParameterMetaData> parameterMetaDatas){
        StringBuilder sb = new StringBuilder();
        try{
            sb.append(String.format(" {call %s(", spName));
            int indexCount = 0;
            for(int i=0; i < parameterMetaDatas.size(); i++){
                if(parameterMetaDatas.get(i).getParameterType() == 1 || parameterMetaDatas.get(i).getParameterType() == 2){
                    String columnName = parameterMetaDatas.get(i).getParameterName();
                    if(indexCount > 0){
                        sb.append(",");
                    }
                    sb.append("?");
                    indexCount++;
                }

            }
            sb.append(")}");
        }catch (Exception ex){
            logger.error("获取存储过程SQL异常：" + ExceptionUtils.getFullStackTrace(ex));
            return null;
        }
        return sb.toString();
    }



}
