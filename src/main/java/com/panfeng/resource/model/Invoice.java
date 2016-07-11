package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

/**
 * 
 * 发票信息实体
 * @author Jack
 */
public class Invoice extends BaseObject {

	private static final long serialVersionUID = -6519482800672773697L;

	private long invoiceId = 0l;
	
	private String invoiceCode = null; // 发票编号
	
	private String invoiceName = null; // 发票名称
	
	private double invoicePrice = 0.0d; // 发票金额
	
	private float invoiceRadio = 0.0f; // 税率
	
	private String invoiceNotice = null; // 备注
	
	private Long invoiceProjectId = 0l; // 项目ID
	
	private String projectName = null; // 冗余字段，显示信息
	
	private Long invoiceUserId = 0l; // 客户ID
	
	private String userName = null; // 冗余字段，显示信息
	
	private Long invoiceProviderId = 0l; // 供应商ID
	
	private String providerName = null; // 冗余字段，显示信息
	
	private int invoiceType = 0; // 0:客户发票 1:供应商发票
	
	private int invoiceFlag = 0; // 0:视频管家未领 1:视频管家已领
	
	private int invoiceDraw = 0; // 0:发票领取方未领取 1:发票领取方已领取
	
	private String payTime = null; // 交易时间
	
	private String createDate = null; // 创建时间
	
	private String updateDate = null; // 更新时间

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

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public double getInvoicePrice() {
		return invoicePrice;
	}

	public void setInvoicePrice(double invoicePrice) {
		this.invoicePrice = invoicePrice;
	}

	public float getInvoiceRadio() {
		return invoiceRadio;
	}

	public void setInvoiceRadio(float invoiceRadio) {
		this.invoiceRadio = invoiceRadio;
	}

	public String getInvoiceNotice() {
		return invoiceNotice;
	}

	public void setInvoiceNotice(String invoiceNotice) {
		this.invoiceNotice = invoiceNotice;
	}

	public long getInvoiceProjectId() {
		return invoiceProjectId;
	}

	public void setInvoiceProjectId(long invoiceProjectId) {
		this.invoiceProjectId = invoiceProjectId;
	}

	public long getInvoiceUserId() {
		return invoiceUserId;
	}

	public void setInvoiceUserId(long invoiceUserId) {
		this.invoiceUserId = invoiceUserId;
	}

	public long getInvoiceProviderId() {
		return invoiceProviderId;
	}

	public void setInvoiceProviderId(long invoiceProviderId) {
		this.invoiceProviderId = invoiceProviderId;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	public int getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(int invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public int getInvoiceDraw() {
		return invoiceDraw;
	}

	public void setInvoiceDraw(int invoiceDraw) {
		this.invoiceDraw = invoiceDraw;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
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

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
}
