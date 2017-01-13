package com.panfeng.resource.view;

public class IndentView extends Pagination{

	private static final long serialVersionUID = -6378756993311609394L;

	private Long serviceId = null; // 关联服务
	
	private Long userId = null; // 关联用户
	
	private String indentNum = null; // 订单编号
	
	private String indent_tele = null; //  联系方式
	
	private String indentId = null; // 订单ID
	
	private Integer indentType = null; // 订单状态
	
	private String salesmanUniqueId = null; // 分销人唯一编号

	public String getIndent_tele() {
		return indent_tele;
	}

	public void setIndent_tele(String indent_tele) {
		this.indent_tele = indent_tele;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIndentNum() {
		return indentNum;
	}

	public void setIndentNum(String indentNum) {
		this.indentNum = indentNum;
	}
	public String getIndentId() {
		return indentId;
	}

	public void setIndentId(String indentId) {
		this.indentId = indentId;
	}

	public Integer getIndentType() {
		return indentType;
	}

	public void setIndentType(Integer indentType) {
		this.indentType = indentType;
	}

	public String getSalesmanUniqueId() {
		return salesmanUniqueId;
	}

	public void setSalesmanUniqueId(String salesmanUniqueId) {
		this.salesmanUniqueId = salesmanUniqueId;
	}

}
