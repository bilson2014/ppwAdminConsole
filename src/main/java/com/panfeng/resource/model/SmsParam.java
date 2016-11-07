package com.panfeng.resource.model;

import java.io.Serializable;

public class SmsParam implements Serializable {

	private static final long serialVersionUID = -1260327667384125424L;

	// 短信内容
	private String[] content = null; 
	
	// 电话号码
	private String telephone = null; 

	public String[] getContent() {
		return content;
	}

	public void setContent(String[] content) {
		this.content = content;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
