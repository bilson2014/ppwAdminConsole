package com.panfeng.resource.view;

public class IndentView extends Pagination {

	private static final long serialVersionUID = -6378756993311609394L;

	private Long serviceId = null; // 关联服务

	private Long userId = null; // 关联用户

	private String indentNum = null; // 订单编号

	private String indent_tele = null; // 联系方式

	private String id = null; // 订单ID

	private Integer indentType = null; // 订单状态

	private String salesmanUniqueId = null; // 分销人唯一编号

	private String beginTime = null;// 上传时间搜索 起始时间

	private String endTime = null;// 上传时间搜索 结束时间

	/**
	 * 订单来源
	 */
	private Integer indentSource;

	/**
	 * 客服ID
	 */
	private Long employeeId;

	public Integer getIndentSource() {
		return indentSource;
	}

	public void setIndentSource(Integer indentSource) {
		this.indentSource = indentSource;
	}

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

}
