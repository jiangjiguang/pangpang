package com.pangpang.dao.util;

import java.util.List;

public class TableInfo {
	
	Class<?> classType;
	String tableName;
	List<ColumnInfo> columnInfos;
	
	
	
	public Class<?> getClassType() {
		return classType;
	}
	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<ColumnInfo> getColumnInfos() {
		return columnInfos;
	}
	public void setColumnInfos(List<ColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}
	
}
