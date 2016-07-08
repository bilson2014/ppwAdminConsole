package com.panfeng.resource.view;

public class ServiceView extends Pagination {

	private static final long serialVersionUID = 3207613996765386713L;

	private Long serviceId = null;
	
	private Long productId = null;
	
	//add by wanglc 2016-7-4 15:15:13所属项目修改为模糊查询 bigin
	private String productName = null;
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	//add by wanglc 2016-7-4 15:15:13所属项目修改为模糊查询 bigin
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
}
