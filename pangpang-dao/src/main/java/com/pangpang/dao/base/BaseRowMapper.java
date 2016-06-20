package com.pangpang.dao.base;
import com.pangpang.dao.util.ColumnInfo;
import com.pangpang.dao.util.TableInfo;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseRowMapper<T> implements RowMapper<T> {
	TableInfo tableInfo;

	public BaseRowMapper(TableInfo tableInfo) {
		// TODO Auto-generated constructor stub
		this.tableInfo = tableInfo;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		
		try {
			Object rtn = tableInfo.getClassType().newInstance();
			for(ColumnInfo cInfo:tableInfo.getColumnInfos()){
				//Object colValue = rs.getObject();
				Object colValue = getResultSetValue(rs, cInfo.getColumnName(),cInfo.getFieldType());
			}
		} catch (InstantiationException | IllegalAccessException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return null;
	}

	public static Object getResultSetValue(ResultSet rs, String colName, Class<?> requiredType) throws SQLException {
		if (requiredType == null) {
			return rs.getObject(colName);
		}

		Object value;

		// Explicitly extract typed value, as far as possible.
		if (String.class == requiredType) {
			return rs.getString(colName);
		} else if (boolean.class == requiredType || Boolean.class == requiredType) {
			value = rs.getBoolean(colName);
		} else if (byte.class == requiredType || Byte.class == requiredType) {
			value = rs.getByte(colName);
		} else if (short.class == requiredType || Short.class == requiredType) {
			value = rs.getShort(colName);
		} else if (int.class == requiredType || Integer.class == requiredType) {
			value = rs.getInt(colName);
		} else if (long.class == requiredType || Long.class == requiredType) {
			value = rs.getLong(colName);
		} else if (float.class == requiredType || Float.class == requiredType) {
			value = rs.getFloat(colName);
		} else if (double.class == requiredType || Double.class == requiredType || Number.class == requiredType) {
			value = rs.getDouble(colName);
		} else if (BigDecimal.class == requiredType) {
			return rs.getBigDecimal(colName);
		} else if (java.sql.Date.class == requiredType) {
			return rs.getDate(colName);
		} else if (java.sql.Time.class == requiredType) {
			return rs.getTime(colName);
		} else if (java.sql.Timestamp.class == requiredType || java.util.Date.class == requiredType) {
			return rs.getTimestamp(colName);
		} else if (byte[].class == requiredType) {
			return rs.getBytes(colName);
		} else if (Blob.class == requiredType) {
			return rs.getBlob(colName);
		} else if (Clob.class == requiredType) {
			return rs.getClob(colName);
		} else {
			return rs.getObject(colName);
		}

		// Perform was-null check if necessary (for results that the JDBC driver
		// returns as primitives).
		return (rs.wasNull() ? null : value);
	}
}
