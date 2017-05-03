package com.panfeng.domain;

import com.paipianwang.pat.common.entity.BaseEntity;

public class Result extends BaseEntity {

	private static final long serialVersionUID = 8198446999141773515L;

	private boolean ret = false;
	
	private String message = null;

	public boolean isRet() {
		return ret;
	}

	public void setRet(boolean ret) {
		this.ret = ret;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
