package com.panfeng.resource.model;

/**
 * 对象差异中间类
 *2016-10-18 15:57:42
 */
public class DIffBean {

	private String property;//属性字段
	private String propertyName;//属性描述
	private String oldValue;//旧值
	private String newValue;//新值
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
}
