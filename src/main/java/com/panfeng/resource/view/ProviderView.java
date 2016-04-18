package com.panfeng.resource.view;

public class ProviderView extends Pagination {

	private static final long serialVersionUID = -3075917061946999097L;
	
	private long providerId = 0l;
	
	private String providerName = null; // 供应商名称
	
	private String phoneNumber = null; // 供应商 电话号码
	
	private String email = null; // 供应商邮件
	
	private String flag = null; // 状态位

	public long getProviderId() {
		return providerId;
	}

	public void setProviderId(long providerId) {
		this.providerId = providerId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
