package com.panfeng.resource.model;

import java.io.Serializable;

/**
 * 项目角色
 */
public class ActivitiGroup implements Serializable{
	private static final long serialVersionUID = -2731311175591109829L;
	private String id;
	private String rev;
	private String name;
	private String type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRev() {
		return rev;
	}
	public void setRev(String rev) {
		this.rev = rev;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
