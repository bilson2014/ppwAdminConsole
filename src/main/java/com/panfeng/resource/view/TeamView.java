package com.panfeng.resource.view;

public class TeamView extends Pagination {

	private static final long serialVersionUID = 7598528321076177988L;

	private Long teamId = null; // 团队唯一编号

	private String flag = null; // 供应商审核状态

	private String phoneNumber = null; // 电话号码

	private String loginName = null; // 登录名

	private String priceRange = null; // 价格区间

	private String[] business = null; // 业务区间

	private String teamName = null;// 团队名称like查询

	private boolean recommend = false;// 是否是主页推荐

	private String linkman = null; // 联系人

	private String provinceID = null; // 省ID

	private String cityID = null; // 市ID

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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

}
