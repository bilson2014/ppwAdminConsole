package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.EmployeeMapper;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.model.Role;
import com.panfeng.resource.view.EmployeeView;
import com.panfeng.service.EmployeeService;
import com.panfeng.util.ValidateUtil;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	final EmployeeMapper mapper = null;
	
	public List<Employee> listWithPagination(final EmployeeView view) {
		
		final List<Employee> lists = mapper.listWithPagination(view);
		for (final Employee employee : lists) {
			final List<Role> roles = employee.getRoles();
			final List<Long> roleIds = new ArrayList<Long>();
			for (final Role role : roles) {
				final long roleId = role.getRoleId();
				roleIds.add(roleId);
			}
			employee.setRoleIds(roleIds);
		}
		return lists;
	}

	public long update(final Employee employee) {
		final long ret = mapper.update(employee);
		return ret;
	}
	
	@Transactional
	public void updateWidthRelation(final Employee employee) {
		
		final long id = employee.getEmployeeId();
		mapper.deleteEmployeeRoleLink(id);
		mapper.update(employee);
		
		final List<Long> roleIds = employee.getRoleIds();
		if(ValidateUtil.isValid(roleIds)){
			// 排除没有权限的弊端
			if(roleIds.size() == 1 && roleIds.get(0) == 0){
				
			}else {
				
				mapper.saveRelativity(employee);
			}
		}
	}

	@Transactional
	public long delete(final long[] ids) {
		
		if(ids != null && ids.length > 0){
			for (final long id : ids) {
				// 删除ID员工时，同时删除关联表中的数据
				mapper.deleteEmployeeRoleLink(id);
				mapper.delete(id);
			}
			return 1l;
		}
		return 0l;
	}

	@Transactional
	public long save(final Employee employee) {
		
		final long ret = mapper.save(employee);
		mapper.saveRelativity(employee);
		return ret;
	}

	public Employee findEmployerById(final long employeeId) {
		
		final Employee employee = mapper.findEmployeeById(employeeId);
		return employee;
	}

	public long maxSize(final EmployeeView view) {
		
		final long max = mapper.maxSize(view);
		return max;
	}

	public Employee doLogin(final String loginName,final String password) {
		
		final Employee employee = mapper.doLogin(loginName,password);
		return employee;
	}

	public long updateImagePath(final Employee e) {
		
		final long ret = mapper.updateImagePath(e);
		return ret;
	}

	public long editPassword(final Employee employee) {
		
		final long ret = mapper.editPassword(employee);
		return ret;
	}
	
	public long editPasswordById(final Employee employee) {
		
		final long ret = mapper.editPasswordById(employee);
		return ret;
	}

	public long checkPhoneNumber(String phoneNumber) {
		
		final long ret = mapper.checkPhoneNumber(phoneNumber);
		return ret;
	}

	@Override
	public List<Employee> searchEmployee(String employeeRealName) {
	 return	mapper.findEmployeeByRealName(employeeRealName);
	}

	@Override
	public List<Employee> getEmployeeList() {
		
		final List<Employee> list = mapper.getEmployeeList();
		return list;
	}

	@Override
	public Map<Long, Employee> getEmployeeMap() {
		
		final Map<Long,Employee> map = mapper.getEmployeeMap();
		return map;
	}

	@Override
	public List<Employee> findEmployeeByRealNameByReffer(final String employeeRealName) {
		
		final List<Employee> list = mapper.findEmployeeByRealNameByReffer(employeeRealName);
		return list;
	}
}
