package com.panfeng.resource.view;


public class TeamView extends Pagination{

	private static final long serialVersionUID = 7598528321076177988L;
	
	private Long teamId = null; // 团队唯一编号
	
	private String flag = null; // 供应商审核状态
	
	private String phoneNumber = null; // 电话号码
	
	private String loginName = null; // 登录名
	
	private String priceRange = null; // 价格区间
	
	private String[] business = null; // 业务区间
	
	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}

	public String[] getBusiness() {
		return business;
	}

	public void setBusiness(String[] business) {
		this.business = business;
	}

}
