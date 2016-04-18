package com.panfeng.resource.model;

import java.util.List;

import com.panfeng.domain.BaseObject;

public class Role extends BaseObject {

	private static final long serialVersionUID = -8854764223286938909L;
	
	private long roleId = 0l;
	
	private String roleName = null;
	
	private String roleValue = null;
	
	private String roleDescription = null;
	
	private String createDate = null; // 创建时间
	
	private String updateDate = null; // 更新时间
	
	private List<Employee> employees = null; // 职工集合
	
	private List<Right> rights = null; // 权限集合

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
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

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Right> getRights() {
		return rights;
	}

	public void setRights(List<Right> rights) {
		this.rights = rights;
	}

	public String getRoleValue() {
		return roleValue;
	}

	public void setRoleValue(String roleValue) {
		this.roleValue = roleValue;
	}

}
