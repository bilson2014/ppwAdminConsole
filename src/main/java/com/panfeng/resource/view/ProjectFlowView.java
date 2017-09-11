package com.panfeng.resource.view;

public class ProjectFlowView extends Pagination {
	private static final long serialVersionUID = -4031506373939561037L;
	
	private String projectId;
	
	private String projectName;//项目名称	
	private String projectStatus;//项目状态
	private String principalName;//项目负责人
	
	private String schemeName;//策划名称
	private String superviseName;//监制名称
	
	private String userName;//客户名称
	private String teamName;//供应商名称
	
	private String projectSource;//项目来源
	private String projectStage;//项目阶段
	private Integer productId;//产品线
	private Integer productConfigLevelId;//等级
	private String beginTime = null;//创建时间 起始时间	
	private String endTime = null;//创建时间 结束时间
	

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getSuperviseName() {
		return superviseName;
	}

	public void setSuperviseName(String superviseName) {
		this.superviseName = superviseName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getProjectSource() {
		return projectSource;
	}

	public void setProjectSource(String projectSource) {
		this.projectSource = projectSource;
	}

	public String getProjectStage() {
		return projectStage;
	}

	public void setProjectStage(String projectStage) {
		this.projectStage = projectStage;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductConfigLevelId() {
		return productConfigLevelId;
	}

	public void setProductConfigLevelId(Integer productConfigLevelId) {
		this.productConfigLevelId = productConfigLevelId;
	}

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
	
}
