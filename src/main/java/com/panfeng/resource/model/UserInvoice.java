package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

/**
 * 
 * 客户发票信息实体
 * @author Jack
 */
public class UserInvoice extends BaseObject {

	private static final long serialVersionUID = -6519482800672773697L;

	private long[] ids;
	
	private long invoiceId = 0l;
	
	private String invoiceCode = null; // 发票编号
	
	private int invoiceType ; // 1.增值税专用发票 2.增值税普通发票
	
	private String invoiceContent;//发票内容
	
	private String invoiceTotal;//价税合计
	
	private float invoiceRadio = 0.0f; // 税率
	
	private String invoiceStampTime = null; //提供发票的日期
	
	private String invoiceReceiveTime = null; // 视频管家领取日期
	
	private Long invoiceUserId = 0l; // 客户ID
	
	private Long invoiceProjectId = 0l; // 项目ID
	
	
	private Long invoiceEmployeeId = 0l; // 领取人
	
	private String invoiceNotice = null; // 备注
	
	private Integer invoiceStatus = 0; // 发票状态
	
	private String reason = null; // 未通过原因
	
	private String projectName = null;//项目名
	
	private String userName = null;//用户名
	
	private String invoiceEmployeeName = null;//用户名
	

	public long[] getIds() {
		return ids;
	}

	public void setIds(long[] ids) {
		this.ids = ids;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getInvoiceEmployeeName() {
		return invoiceEmployeeName;
	}

	public void setInvoiceEmployeeName(String invoiceEmployeeName) {
		this.invoiceEmployeeName = invoiceEmployeeName;
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

	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public String getInvoiceTotal() {
		return invoiceTotal;
	}

	public void setInvoiceTotal(String invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	public float getInvoiceRadio() {
		return invoiceRadio;
	}

	public void setInvoiceRadio(float invoiceRadio) {
		this.invoiceRadio = invoiceRadio;
	}

	public String getInvoiceStampTime() {
		return invoiceStampTime;
	}

	public void setInvoiceStampTime(String invoiceStampTime) {
		this.invoiceStampTime = invoiceStampTime;
	}

	public String getInvoiceReceiveTime() {
		return invoiceReceiveTime;
	}

	public void setInvoiceReceiveTime(String invoiceReceiveTime) {
		this.invoiceReceiveTime = invoiceReceiveTime;
	}

	public Long getInvoiceUserId() {
		return invoiceUserId;
	}

	public void setInvoiceUserId(Long invoiceUserId) {
		this.invoiceUserId = invoiceUserId;
	}

	public Long getInvoiceProjectId() {
		return invoiceProjectId;
	}

	public void setInvoiceProjectId(Long invoiceProjectId) {
		this.invoiceProjectId = invoiceProjectId;
	}

	public Long getInvoiceEmployeeId() {
		return invoiceEmployeeId;
	}

	public void setInvoiceEmployeeId(Long invoiceEmployeeId) {
		this.invoiceEmployeeId = invoiceEmployeeId;
	}

	public String getInvoiceNotice() {
		return invoiceNotice;
	}

	public void setInvoiceNotice(String invoiceNotice) {
		this.invoiceNotice = invoiceNotice;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	
}
