package com.panfeng.resource.model;

import java.util.Map;

import com.panfeng.domain.BaseObject;

public class DealLog extends BaseObject {

	private static final long serialVersionUID = 8541137142577987948L;

	private Long dealId = null; // id
	private String billNo = null; // 订单编号
	private String payChannel = null; // 支付通道
	private String createTime = null;
	private String updateTime = null;
	private Integer dealStatus = null; // 订单状态，0：进行中 1：支付成功 2：支付关闭 3：支付失败
	private String userType = null;// 用户角色
	private Long userId = null;
	private Long projectId = null; // 项目ID
	private Integer dealLogSource = null; // 交易记录来源， 0 线上支付，1线下支付
	private Integer logType = null;// 0 为入账，1为出账
	private Double payPrice = null;// 支付价格
	private String projectName = null;
	private String userName = null;
	private String description = null;// 描述
	private String unOrderId = null; // 银联支付成功流水号
	private String payTime = null;

	// 冗余字段
	private String title = null; // 支付页标题
	private String returnUrl = null;// 支付后返回url
	private Map<String, Object> optional = null;
	private Sign sign = null;
	private String orderTimeOut = null; // 订单超时
	private String proceedsSide = "北京拍片乐科技有限公司";// 现在是定死的
	private Boolean urlEffective = null;
	private String token = null;

	public final static int DEAL_STATUS_ONGOING = 0;// 支付中
	public final static int DEAL_STATUS_SUCCEED = 1;// 支付成功
	public final static int DEAL_STATUS_OFF = 2;// 支付关闭
	public final static int DEAL_STATUS_FAIL = 3;// 支付失败

	public final static int DEALLOG_SOURCE_ONLINE = 0;// 在线支付
	public final static int DEALLOG_SOURCE_OFFLINE = 1;// 离线支付

	public final static int LOG_TYPE_INCOME = 0;// 收入
	public final static int LOG_TYPE_EXPENDITURE = 1;// 支出

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("dealId:");
		stringBuffer.append(dealId);
		stringBuffer.append("\nbillNo:");
		stringBuffer.append(billNo);
		stringBuffer.append("\npayChannel:");
		stringBuffer.append(payChannel);
		stringBuffer.append("\ncreateTime");
		stringBuffer.append(createTime);
		stringBuffer.append("\nupdateTime:");
		stringBuffer.append(updateTime);
		stringBuffer.append("\ndealStatus:");
		stringBuffer.append(dealStatus);
		stringBuffer.append("\nuserType:");
		stringBuffer.append(userType);
		stringBuffer.append("\nuserId:");
		stringBuffer.append(userId);
		stringBuffer.append("\nprojectId:");
		stringBuffer.append(projectId);
		stringBuffer.append("\ndealLogSource:");
		stringBuffer.append(dealLogSource);
		stringBuffer.append("\nlogType:");
		stringBuffer.append(logType);
		stringBuffer.append("\npayPrice:");
		stringBuffer.append(payPrice);
		stringBuffer.append("\nprojectName:");
		stringBuffer.append(projectName);
		stringBuffer.append("\nuserName:");
		stringBuffer.append(userName);
		stringBuffer.append("\ndescription:");
		stringBuffer.append(description);
		stringBuffer.append("\nunOrderId:");
		stringBuffer.append(unOrderId);
		stringBuffer.append("\ntitle:");
		stringBuffer.append(title);
		stringBuffer.append("\nreturnUrl:");
		stringBuffer.append(returnUrl);
		stringBuffer.append("\noptional:");
		stringBuffer.append(optional);
		return stringBuffer.toString();
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public Long getDealId() {
		return dealId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOrderTimeOut() {
		return orderTimeOut;
	}

	public String getProceedsSide() {
		return proceedsSide;
	}

	public void setProceedsSide(String proceedsSide) {
		this.proceedsSide = proceedsSide;
	}

	public void setOrderTimeOut(String orderTimeOut) {
		this.orderTimeOut = orderTimeOut;
	}

	public Boolean getUrlEffective() {
		return urlEffective;
	}

	public void setUrlEffective(Boolean urlEffective) {
		this.urlEffective = urlEffective;
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

	public Map<String, Object> getOptional() {
		return optional;
	}

	public void setOptional(Map<String, Object> optional) {
		this.optional = optional;
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

	public Integer getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(Integer dealStatus) {
		this.dealStatus = dealStatus;
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

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Integer getDealLogSource() {
		return dealLogSource;
	}

	public void setDealLogSource(Integer dealLogSource) {
		this.dealLogSource = dealLogSource;
	}

	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public Double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public String getUnOrderId() {
		return unOrderId;
	}

	public void setUnOrderId(String unOrderId) {
		this.unOrderId = unOrderId;
	}

}
