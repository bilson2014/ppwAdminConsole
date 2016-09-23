package com.panfeng.resource.view;

public class UserView extends Pagination{

	private static final long serialVersionUID = -8386964921821452730L;
	
	private String userName;
	private Integer clientLevel = -1; // 客户分级
	private String beginTime = null;
	private String endTime = null;
	
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getClientLevel() {
		return clientLevel;
	}

	public void setClientLevel(Integer clientLevel) {
		this.clientLevel = clientLevel;
	}
	
}
