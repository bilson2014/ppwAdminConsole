package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

/**
 * 服务业务类
 * @author GY
 */
public class Service extends BaseObject {

	private static final long serialVersionUID = 3119615375596278034L;

	private long serviceId = 0l;
	
	private String serviceName = null; // 服务名称
	
	private String serviceDescription = null; // 服务描述
	
	private double servicePrice = 0.0d; // 服务金额
	
	private double serviceRealPrice = 0.0d; // 折扣后的金额
	
	private double serviceDiscount = 0.0d; // 折扣
	
	private int serviceOd = 0; // 排序字段
	
	private long mcoms = 0l; // 影片时长
	
	private Long productId = 0l; // 作品关联
	
	private String productName = null; // 冗余字段，用于保存项目名称
	
	private Product product = null; // 作品关联

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public double getServiceRealPrice() {
		return serviceRealPrice;
	}

	public void setServiceRealPrice(double serviceRealPrice) {
		this.serviceRealPrice = serviceRealPrice;
	}

	public double getServiceDiscount() {
		return serviceDiscount;
	}

	public void setServiceDiscount(double serviceDiscount) {
		this.serviceDiscount = serviceDiscount;
	}

	public int getServiceOd() {
		return serviceOd;
	}

	public void setServiceOd(int serviceOd) {
		this.serviceOd = serviceOd;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public long getMcoms() {
		return mcoms;
	}

	public void setMcoms(long mcoms) {
		this.mcoms = mcoms;
	}
	
}
