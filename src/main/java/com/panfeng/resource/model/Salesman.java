package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

/**
 * 分销人
 * @author Jack
 */
public class Salesman extends BaseObject {

	private static final long serialVersionUID = -8360251575797625256L;

	private long salesmanId = 0l;
	
	private String salesmanName = null; // 分销人名称
	
	private String salesmanDescription = null; // 分销备注
	
	private String salesmanDepartment = null; // 备用字段
	
	private String uniqueId = null; // 唯一标识
	
	private String updateDate = null; // 更新时间
	
	private String createDate = null; // 创建时间

	public long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getSalesmanDescription() {
		return salesmanDescription;
	}

	public void setSalesmanDescription(String salesmanDescription) {
		this.salesmanDescription = salesmanDescription;
	}

	public String getSalesmanDepartment() {
		return salesmanDepartment;
	}

	public void setSalesmanDepartment(String salesmanDepartment) {
		this.salesmanDepartment = salesmanDepartment;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}
