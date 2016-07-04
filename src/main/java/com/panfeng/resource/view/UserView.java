package com.panfeng.resource.view;

public class UserView extends Pagination{

	private static final long serialVersionUID = -8386964921821452730L;
	
	private String userName;
	
	private Integer clientLevel = -1; // 客户分级

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
