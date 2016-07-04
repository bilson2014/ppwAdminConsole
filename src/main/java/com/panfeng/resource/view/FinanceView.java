package com.panfeng.resource.view;

public class FinanceView extends Pagination{

	private static final long serialVersionUID = 461244803027514701L;
	
	private Integer logType = null; // 0 为入账，1为出账
	
	private Integer dealLogSource = null; // 交易记录来源， 0 线上支付，1线下支付

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
