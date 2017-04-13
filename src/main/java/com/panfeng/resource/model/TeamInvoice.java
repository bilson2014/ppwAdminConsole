package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;

/**
 * 供应商发票信息实体
 */
public class TeamInvoice extends BaseEntity {

	private static final long serialVersionUID = -6519482800672773697L;

	private long[] ids;//id集合
	
	private long invoiceId = 0l;
	
	private String invoiceCode = null; // 发票编号
	
	private int invoiceType ; // 1.增值税专用发票 2.增值税普通发票
	
	private String invoiceContent;//发票内容
	
	private String invoiceTotal;//价税合计
	
	private String invoiceStampTime = null; //开票日期
	
	private String invoiceTeamTime = null; // 提供发票日期
	
	private float invoiceRadio = 0.0f; // 税率
	
	private String invoiceNotice = null; // 备注
	
	private Long invoiceProjectId = 0l; // 项目ID
	
	private Long invoiceEmployeeId = 0l; // 领取人
	
	private Long invoiceUserId = 0l; // 客户ID
	
	private Long invoiceTeamId = 0l; // 供应商ID
	
	private Integer invoiceStatus = 0; // 发票状态
	
	private String reason = null; // 未通过原因
	
	private String projectName = null;//项目名
	
	private String teamName = null;//供应商名称
	
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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getInvoiceTeamTime() {
		return invoiceTeamTime;
	}

	public void setInvoiceTeamTime(String invoiceTeamTime) {
		this.invoiceTeamTime = invoiceTeamTime;
	}

	public Long getInvoiceTeamId() {
		return invoiceTeamId;
	}

	public void setInvoiceTeamId(Long invoiceTeamId) {
		this.invoiceTeamId = invoiceTeamId;
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
