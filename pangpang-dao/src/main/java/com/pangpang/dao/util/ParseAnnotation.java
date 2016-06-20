package com.pangpang.dao.util;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ParseAnnotation {
	/**
	 * 解析方法注解
	 * 
	 * @param <T>
	 * @param clazz
	 */
	public static <T> TableInfo parseEntity(Class<T> clazz) {
		TableInfo info = new TableInfo();
		try {
			info.setClassType(clazz);
			Table tableAnnotation = clazz.getAnnotation(Table.class);
			if (tableAnnotation == null) {
				String tString = clazz.getName();
				tString = tString.substring(tString.lastIndexOf(".")+1);
				info.setTableName(tString);//Ipipapidata

			} else
				info.setTableName(tableAnnotation.name());
			
			List<ColumnInfo> columnInfos = new ArrayList<>();
			info.setColumnInfos(columnInfos);
			
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				for (Field f : fields) {
					ColumnInfo columnInfo = new ColumnInfo();
					Id id = f.getAnnotation(Id.class);
					if (id != null) {
						columnInfo.setPk(true);
					}

					GeneratedValue generatedValue = f.getAnnotation(GeneratedValue.class);
					if (generatedValue != null && generatedValue.strategy() == GenerationType.IDENTITY) {
						columnInfo.setIdentity(true);
					}
					// (strategy = GenerationType.IDENTITY)
					// @Column(name="startAddr")
					Column column = f.getAnnotation(Column.class);
					if(column!=null && StringUtils.isNotBlank(column.name())){
						columnInfo.setColumnName(column.name());
						columnInfo.setFieldName(f.getName());
						columnInfo.setFieldType(f.getType());
						columnInfos.add(columnInfo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
}
