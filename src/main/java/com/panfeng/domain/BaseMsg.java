package com.panfeng.domain;

public class BaseMsg {

	private Integer errorCode = null;
	private String errorMsg = null;
	private Object result = null;

	public static int NORMAL = 200; // 正常
	public static int ERROR = 500; // 错误
	public static int UNKNOWN_ERROR = 600; // 异常
	public static int URL_FAILURE = 601; // 失效

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public BaseMsg(Integer errorCode, String errorMsg, Object result) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.result = result;
	}

	public BaseMsg() {
		super();
	}
	
	

}
