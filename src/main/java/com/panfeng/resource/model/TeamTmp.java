package com.panfeng.resource.model;
/**
 * team审核零时表
 * 用于供应商修改信息后信息暂存
 *2016-10-17 12:06:39
 */
public class TeamTmp {

	private Integer id;
	private Long teamId;
	private String teamName;
	private String linkMan;
	private String webchat;
	private String qq;
	private String email;
	private String address;
	private String teamProvince;
	private String teamCity;
	private Integer priceRange;
	private Integer infoResource;
	private String business;
	private String teamDescription;
	private String scale;
	private String demand;
	private String createTime;
	private Boolean status;
	private Integer checkStatus;
	private String checkDetails;
	private String establishDate;
	private String officialSite;
	private String businessDescription;
	
	
	private String teamProvinceName;
	private String teamCityName;
	
	public String getTeamProvinceName() {
		return teamProvinceName;
	}
	public void setTeamProvinceName(String teamProvinceName) {
		this.teamProvinceName = teamProvinceName;
	}
	public String getTeamCityName() {
		return teamCityName;
	}
	public void setTeamCityName(String teamCityName) {
		this.teamCityName = teamCityName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getTeamId() {
		return teamId;
	}
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getWebchat() {
		return webchat;
	}
	public void setWebchat(String webchat) {
		this.webchat = webchat;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTeamProvince() {
		return teamProvince;
	}
	public void setTeamProvince(String teamProvince) {
		this.teamProvince = teamProvince;
	}
	public String getTeamCity() {
		return teamCity;
	}
	public void setTeamCity(String teamCity) {
		this.teamCity = teamCity;
	}
	public Integer getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(Integer priceRange) {
		this.priceRange = priceRange;
	}
	public Integer getInfoResource() {
		return infoResource;
	}
	public void setInfoResource(Integer infoResource) {
		this.infoResource = infoResource;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getTeamDescription() {
		return teamDescription;
	}
	public void setTeamDescription(String teamDescription) {
		this.teamDescription = teamDescription;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getDemand() {
		return demand;
	}
	public void setDemand(String demand) {
		this.demand = demand;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getCheckDetails() {
		return checkDetails;
	}
	public void setCheckDetails(String checkDetails) {
		this.checkDetails = checkDetails;
	}
	public String getEstablishDate() {
		return establishDate;
	}
	public void setEstablishDate(String establishDate) {
		this.establishDate = establishDate;
	}
	public String getOfficialSite() {
		return officialSite;
	}
	public void setOfficialSite(String officialSite) {
		this.officialSite = officialSite;
	}
	public String getBusinessDescription() {
		return businessDescription;
	}
	public void setBusinessDescription(String businessDescription) {
		this.businessDescription = businessDescription;
	}
}
