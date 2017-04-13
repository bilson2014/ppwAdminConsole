package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;

public class ThirdBind extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private Integer code;//0||1 第三方账户状态,不存在或者存在却无手机号
	private String type;//qq wechat wb 第三方来源
	private String phoneStatus;//手机号状态  register or noregister 未注册过或已注册过
	private Integer thirdStatus;//当前输入手机号绑定第三方 状态  0未绑定第三方 1已绑定第三方
	private Integer userId;
	private String userName;
	private String imgUrl;
	private String telephone;
	private String verification_code;
	private String unique;//第三方唯一标识
	
	public String getUnique() {
		return unique;
	}
	public void setUnique(String unique) {
		this.unique = unique;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhoneStatus() {
		return phoneStatus;
	}
	public void setPhoneStatus(String phoneStatus) {
		this.phoneStatus = phoneStatus;
	}
	public Integer getThirdStatus() {
		return thirdStatus;
	}
	public void setThirdStatus(Integer thirdStatus) {
		this.thirdStatus = thirdStatus;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getVerification_code() {
		return verification_code;
	}
	public void setVerification_code(String verification_code) {
		this.verification_code = verification_code;
	}
	
}
