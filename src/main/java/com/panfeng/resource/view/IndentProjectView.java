package com.panfeng.resource.view;

public class IndentProjectView extends Pagination {

	private static final long serialVersionUID = -4905862614173543861L;

	private Long projectId = null;
	
	private Long userId = 0l; // 视频管家ID
	
	private Integer state = null; // 0 正常 1为取消 2为已完成
	
	private Long teamId = null; // 供应商ID
	
	private String source = null; // 项目来源
	
	private String payment = "no_user"; // 付款金额，此字段作为冗余字段，没有作用
	
	//add by wanglc 2016-6-28 18:49:18 视频管家是否是协同人begin
	private Integer isSynergy = 0;
	public Integer getIsSynergy() {
		return isSynergy;
	}

	public void setIsSynergy(Integer isSynergy) {
		this.isSynergy = isSynergy;
	}
	//add by wanglc 2016-6-28 18:49:18 视频管家是否是协同人end
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	
}
