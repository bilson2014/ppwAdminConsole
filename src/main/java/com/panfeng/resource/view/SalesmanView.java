package com.panfeng.resource.view;

public class SalesmanView extends Pagination {

	private static final long serialVersionUID = 6110097106317678268L;

	private String uniqueId = null; // 唯一标识
	
	private String salesmanName = null; // 分销人名称

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

}
