package com.pangpang.dao.util;

public class ColumnInfo {
	boolean isPk = false;
	boolean isIdentity = false;
	String columnName;
	String fieldName;
	Class<?> fieldType;
	public boolean isPk() {
		return isPk;
	}
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}
	public boolean isIdentity() {
		return isIdentity;
	}
	public void setIdentity(boolean isIdentity) {
		this.isIdentity = isIdentity;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Class<?> getFieldType() {
		return fieldType;
	}
	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}
	
}
