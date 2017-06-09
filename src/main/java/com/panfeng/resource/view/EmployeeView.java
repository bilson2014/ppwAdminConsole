package com.panfeng.resource.view;

public class EmployeeView extends Pagination {

	private static final long serialVersionUID = -1359502684224461677L;

	private String employeeRealName = null; // 职工姓名

	private String phoneNumber = null; // 电话号码

	private String hireBeginDate = null; // 入职开始时间

	private String hireEndDate = null; // 入职结束时间

	private Long roleId = null; // 角色ID

	private Integer dimissionStatus = null; // 在职状态

	public String getEmployeeRealName() {
		return employeeRealName;
	}

	public void setEmployeeRealName(String employeeRealName) {
		this.employeeRealName = employeeRealName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getHireBeginDate() {
		return hireBeginDate;
	}

	public void setHireBeginDate(String hireBeginDate) {
		this.hireBeginDate = hireBeginDate;
	}

	public String getHireEndDate() {
		return hireEndDate;
	}

	public void setHireEndDate(String hireEndDate) {
		this.hireEndDate = hireEndDate;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Integer getDimissionStatus() {
		return dimissionStatus;
	}

	public void setDimissionStatus(Integer dimissionStatus) {
		this.dimissionStatus = dimissionStatus;
	}

}
