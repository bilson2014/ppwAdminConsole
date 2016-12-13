package com.panfeng.resource.model;
/**
 * Product审核零时表
 * 用于供应商修改作品信息后信息暂存
 * 2016-12-13 18:57:56
 */
public class ProductTmp {
	private Long id;
	private Long productId;
	private String videoUrl;
	private String productName;
	private String picHDUrl;
	private Long teamId;
	private Integer checkStatus;
	private String checkDetails;
	private Boolean status;
	private String pTime;
	private String createTime;
	private Integer display;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPicHDUrl() {
		return picHDUrl;
	}
	public void setPicHDUrl(String picHDUrl) {
		this.picHDUrl = picHDUrl;
	}
	public Long getTeamId() {
		return teamId;
	}
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
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
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getpTime() {
		return pTime;
	}
	public void setpTime(String pTime) {
		this.pTime = pTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getDisplay() {
		return display;
	}
	public void setDisplay(Integer display) {
		this.display = display;
	}
}
