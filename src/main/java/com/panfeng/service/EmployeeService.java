package com.panfeng.service;

import java.util.List;
import java.util.Map;

import com.panfeng.resource.model.Employee;
import com.panfeng.resource.view.EmployeeView;

public interface EmployeeService {

	public List<Employee> listWithPagination(final EmployeeView view);

	public long update(final Employee employee);
	
	public void updateWidthRelation(final Employee employee);
	
	public long delete(final long[] ids);
	
	public long save(final Employee employee);
	
	public Employee findEmployerById(final long eId);

	public long maxSize(final EmployeeView view);

	public Employee doLogin(final String loginName, final String password);

	public long updateImagePath(final Employee e);

	public long editPassword(final Employee employee);
	
	// 检测手机的唯一性
	public long checkPhoneNumber(final String phoneNumber);
	
	public List<Employee> searchEmployee(final String employeeRealName);

	/**
	 * 获取内部员工（除admin、测试账号外）
	 * @return list
	 */
	public List<Employee> getEmployeeList();
	
	/**
	 * 获取内部员工（除admin、测试账号外）
	 * @return map
	 */
	public Map<Long,Employee> getEmployeeMap();
	
	public List<Employee> findEmployeeByRealNameByReffer(final String employeeRealName);

	public long editPasswordById(final Employee employee);
}
