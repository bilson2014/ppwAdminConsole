package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class DealLog extends BaseObject {

	private static final long serialVersionUID = 8541137142577987948L;

	private Long dealId = null; // id
	private String billNo = null; // 订单编号
	private String payChannel = null; // 支付通道
	private String createTime = null;
	private String updateTime = null;
	private Integer state = null; // 订单状态，0：进行中 1：支付成功 2：支付取消 3：支付失败
	private String userType = null;// 用户角色
	private Long userId = null;

	public final static int DEAL_ONGOING = 0;
	public final static int DEAL_SUCCEED = 1;
	public final static int DEAL_CANCEL = 2;
	public final static int DEAL_FAIL = 3;

	public Long getDealId() {
		return dealId;
	}

	public void setDealId(Long dealId) {
		this.dealId = dealId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
