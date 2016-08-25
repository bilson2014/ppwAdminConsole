package com.panfeng.resource.view;

public class FinanceView extends Pagination{

	private static final long serialVersionUID = 461244803027514701L;
	
	private Integer logType = null; // 0 为入账，1为出账
	
	private Integer dealLogSource = null; // 交易记录来源， 0 线上支付，1线下支付
	
	private String beginTime = null;//搜索起始时间
	
	private String endTime = null;//搜索结束时间
	
	
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

	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public Integer getDealLogSource() {
		return dealLogSource;
	}

	public void setDealLogSource(Integer dealLogSource) {
		this.dealLogSource = dealLogSource;
	}
	
}
