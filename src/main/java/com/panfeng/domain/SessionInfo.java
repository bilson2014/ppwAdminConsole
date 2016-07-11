package com.panfeng.domain;

import com.panfeng.resource.model.Right;

public class SessionInfo extends BaseObject {

	private static final long serialVersionUID = -8244829888233273132L;

	private String token = null; // 令牌
	
	private String sessionType = null; // 用户类型
	
	private long[] sum;
	
	private boolean superAdmin = false; // 超级管理员
	
	private String description = null; // 描述
	
	private String loginName = null; // 登录名
	
	private String realName = null; // 真实姓名
	
	private Long reqiureId = null; // 唯一ID
	
	private String email = null; // 邮箱
	
	private String address = null; // 地址
	
	private String photo = null; // 头像
	
	private Integer clientLevel = null; // 客户级别
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	public long[] getSum() {
		return sum;
	}

	public void setSum(long[] sum) {
		this.sum = sum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}
	
	public boolean hasRight(final Right right){
		int pos = right.getPos();
		long code = right.getCode();
		long ret = sum[pos] & code;
		return !(ret == 0);
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getReqiureId() {
		return reqiureId;
	}

	public void setReqiureId(Long reqiureId) {
		this.reqiureId = reqiureId;
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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getClientLevel() {
		return clientLevel;
	}

	public void setClientLevel(Integer clientLevel) {
		this.clientLevel = clientLevel;
	}
	
}
