package com.panfeng.resource.view;

public class UserView extends Pagination {

	private static final long serialVersionUID = -8386964921821452730L;

	private String userName;
	private Integer clientLevel = -1; // 客户分级
	private String beginTime = null;
	private String endTime = null;
	private String telephone = null;
	/** 客户公司 **/
	private String userCompany = null;

	/** 真实姓名 **/
	private String realName = null;

	/**
	 * 推荐人ID
	 */
	private Long referrerId;
	/**
	 * 网址
	 */
	private String officialSite;
	/**
	 * 职位
	 */
	private Integer position;
	/**
	 * 客户类型
	 */
	private Integer customerType;
	/**
	 * 购买频次
	 */
	private Integer purchaseFrequency;
	/**
	 * 购买价格
	 */
	private Integer purchasePrice;
	/**
	 * 客户规模
	 */
	private Integer customerSize;
	/**
	 * 高层背书
	 */
	private Integer endorse;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

	public Long getReferrerId() {
		return referrerId;
	}

	public void setReferrerId(Long referrerId) {
		this.referrerId = referrerId;
	}

	public String getOfficialSite() {
		return officialSite;
	}

	public void setOfficialSite(String officialSite) {
		this.officialSite = officialSite;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public Integer getPurchaseFrequency() {
		return purchaseFrequency;
	}

	public void setPurchaseFrequency(Integer purchaseFrequency) {
		this.purchaseFrequency = purchaseFrequency;
	}

	public Integer getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Integer purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Integer getCustomerSize() {
		return customerSize;
	}

	public void setCustomerSize(Integer customerSize) {
		this.customerSize = customerSize;
	}

	public Integer getEndorse() {
		return endorse;
	}

	public void setEndorse(Integer endorse) {
		this.endorse = endorse;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserCompany() {
		return userCompany;
	}

	public void setUserCompany(String userCompany) {
		this.userCompany = userCompany;
	}
	
	

}
