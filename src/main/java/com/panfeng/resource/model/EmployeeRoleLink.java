package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.entity.PmsRole;

public class EmployeeRoleLink extends BaseEntity {

	private static final long serialVersionUID = -6534688236658464234L;
	
	private PmsEmployee employee = null;
	
	private PmsRole role = null;

	public PmsEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(PmsEmployee employee) {
		this.employee = employee;
	}

	public PmsRole getRole() {
		return role;
	}

	public void setRole(PmsRole role) {
		this.role = role;
	}

}
