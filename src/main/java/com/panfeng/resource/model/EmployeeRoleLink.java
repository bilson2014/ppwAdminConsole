package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class EmployeeRoleLink extends BaseObject {

	private static final long serialVersionUID = -6534688236658464234L;
	
	private Employee employee = null;
	
	private Role role = null;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
