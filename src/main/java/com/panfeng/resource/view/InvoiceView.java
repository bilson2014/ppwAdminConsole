package com.panfeng.resource.view;

public class InvoiceView extends Pagination {

	private static final long serialVersionUID = 1739408407063119953L;

	private String invoiceName = null; // 发票名称
	
	private Long invoiceProjectId = null; // 项目ID
	
	private Long invoiceProviderId = null; // 供应商ID
	
	private Long invoiceUserId = null; // 客户ID
	
	private Integer invoiceType = null; // 0:客户发票 1:供应商发票
	
	private Integer invoiceFlag = null; // 0:视频管家未领 1:视频管家已领
	
	private Integer invoiceDraw = null; // 0:发票领取方未领取 1:发票领取方已领取

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public Long getInvoiceProjectId() {
		return invoiceProjectId;
	}

	public void setInvoiceProjectId(Long invoiceProjectId) {
		this.invoiceProjectId = invoiceProjectId;
	}
	
	public Long getInvoiceUserId() {
		return invoiceUserId;
	}

	public void setInvoiceUserId(Long invoiceUserId) {
		this.invoiceUserId = invoiceUserId;
	}

	public Long getInvoiceProviderId() {
		return invoiceProviderId;
	}

	public void setInvoiceProviderId(Long invoiceProviderId) {
		this.invoiceProviderId = invoiceProviderId;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Integer getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Integer invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public Integer getInvoiceDraw() {
		return invoiceDraw;
	}

	public void setInvoiceDraw(Integer invoiceDraw) {
		this.invoiceDraw = invoiceDraw;
	}

}
