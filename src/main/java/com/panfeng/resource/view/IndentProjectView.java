package com.panfeng.resource.view;

public class IndentProjectView extends Pagination {

	private static final long serialVersionUID = -4905862614173543861L;

	private Long projectId = null;
	
	private String projectName = null;
	
	private Long userId = 0l; // 客户ID
	
	private String userName = null; // // 客户公司名称
	
	private String userContact = null; // 客户名称
	
	private String userPhone = null;
	
	private String teamName = null; // 供应商名称
	
	private String teamPhone = null; // 供应商联系方式
	
	private Integer state = null; // 0 正常 1为取消 2为已完成
	
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getUserContact() {
		return userContact;
	}

	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getTeamPhone() {
		return teamPhone;
	}

	public void setTeamPhone(String teamPhone) {
		this.teamPhone = teamPhone;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
}
