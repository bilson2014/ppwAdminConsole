package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

/**
 * 数据库元数据
 * @author Jack
 *
 */
public class MetaDataColumn extends BaseObject {

	private static final long serialVersionUID = 3845010065273927089L;

	private String name; // 列名
	
	private String text; // 别名
	
	private String dataType; // 数据类型
	
	private long width; // 长度
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public MetaDataColumn(String name, String text, String dataType) {
		super();
		this.name = name;
		this.text = text;
		this.dataType = dataType;
	}

	public MetaDataColumn() {
		super();
	}
	
}
