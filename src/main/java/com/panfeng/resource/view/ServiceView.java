package com.panfeng.resource.view;

public class ServiceView extends Pagination {

	private static final long serialVersionUID = 3207613996765386713L;

	private Long serviceId = null;
	
	private Long productId = null;

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
