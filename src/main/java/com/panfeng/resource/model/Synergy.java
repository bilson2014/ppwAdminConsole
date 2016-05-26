package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class Synergy extends BaseObject {

	private static final long serialVersionUID = 7186705977664367497L;

	private Long synergyId = null;
	private Long userId = null;
	private Long projectId = null;
	private String createTime = null;
	private String updateTime = null;
	private Double ratio = null; // 项目占有比例

	private String userName = null; //冗余字段  用户名

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getSynergyId() {
		return synergyId;
	}

	public void setSynergyId(Long synergyId) {
		this.synergyId = synergyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

}
