package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.domain.BaseMsg;
import com.panfeng.persist.EmployeeMapper;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.model.Role;
import com.panfeng.resource.model.Team;
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
		if (ValidateUtil.isValid(roleIds)) {
			// 排除没有权限的弊端
			if (roleIds.size() == 1 && roleIds.get(0) == 0) {

			} else {

				mapper.saveRelativity(employee);
			}
		}
	}

	@Transactional
	public long delete(final long[] ids) {

		if (ids != null && ids.length > 0) {
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

	public Employee doLogin(final String loginName, final String password) {

		final Employee employee = mapper.doLogin(loginName, password);
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
		return mapper.findEmployeeByRealName(employeeRealName);
	}

	@Override
	public List<Employee> getEmployeeList() {

		final List<Employee> list = mapper.getEmployeeList();
		return list;
	}

	@Override
	public Map<Long, Employee> getEmployeeMap() {

		final Map<Long, Employee> map = mapper.getEmployeeMap();
		return map;
	}

	@Override
	public List<Employee> findEmployeeByRealNameByReffer(final String employeeRealName) {

		final List<Employee> list = mapper.findEmployeeByRealNameByReffer(employeeRealName);
		return list;
	}

	@Override
	public List<Employee> findEmployeeToSynergy() {

		final List<Employee> list = mapper.findEmployeeToSynergy();
		return list;
	}

	@Override
	public List<Employee> getEmployeesWithVersionManager(final String phoneNumber) {

		final List<Employee> list = mapper.getEmployeesWithVersionManager(phoneNumber);
		return list;
	}

	@Override
	public List<Employee> verificationEmployeeExist(Employee employee) {
		return mapper.verificationEmployeeExist(employee);
	}

	@Override
	public BaseMsg bind(Employee employee) {
		BaseMsg baseMsg = null;
		List<Employee> employees = mapper.verificationEmployeeExist(employee);
		if (ValidateUtil.isValid(employees)) {
			// 不存在情况 --->只存在重复绑定
			for (Employee empl : employees) {
				if (employee.equals(Employee.LTYPE_QQ)) {
					if (empl.getQqUnique().equals(employee.getQqUnique())) {
						return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
					}
				} else if (employee.equals(Employee.LTYPE_WECHAT)) {
					if (empl.getWechatUnique().equals(employee.getWechatUnique())) {
						return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
					}
				} else if (employee.equals(Employee.LTYPE_WEIBO)) {
					if (empl.getWbUnique().equals(employee.getWbUnique())) {
						return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
					}
				}
			}
		} else {
			// 新建
			String phoneNumber = employee.getPhoneNumber();
			List<Employee> empls = mapper.checkEmployee(phoneNumber);
			if (empls != null && empls.size() > 0) {
				if (empls.size() == 1) {
					Employee empl = empls.get(0);
					switch (employee.getThirdLoginType()) {
					case Team.LTYPE_QQ:
						if (!ValidateUtil.isValid(empl.getQqUnique())) {
							// 绑定
							empl.setQqUnique(employee.getQqUnique());
							mapper.updateUniqueId(empl);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else if (empl.getQqUnique().equals(employee.getQqUnique())) {
							// 重复绑定
							return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else {
							// 手机号已经绑定过非当前QQ了
							baseMsg = new BaseMsg(BaseMsg.ERROR, "该手机号已经被注册", null);
						}
						break;
					case Team.LTYPE_WECHAT:
						if (!ValidateUtil.isValid(empl.getWechatUnique())) {
							// 绑定
							empl.setWechatUnique(employee.getWechatUnique());
							mapper.updateUniqueId(empl);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else if (empl.getWechatUnique().equals(employee.getWechatUnique())) {
							// 重复绑定
							return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else {
							// 手机号已经绑定过非当前QQ了
							baseMsg = new BaseMsg(BaseMsg.ERROR, "该手机号已经被注册", null);
						}
						break;
					case Team.LTYPE_WEIBO:
						if (!ValidateUtil.isValid(empl.getWbUnique())) {
							// 绑定
							empl.setWbUnique(employee.getWbUnique());
							mapper.updateUniqueId(empl);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else if (empl.getWbUnique().equals(employee.getWbUnique())) {
							// 重复绑定
							return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else {
							// 手机号已经绑定过非当前QQ了
							baseMsg = new BaseMsg(BaseMsg.ERROR, "该手机号已经被注册", null);
						}
						break;
					}

				} else {
					baseMsg = new BaseMsg(BaseMsg.ERROR, "手机号码重复注册，请联系人事。", null);
				}
			} else {
				// 不存在 ，必须先注册
				baseMsg = new BaseMsg(BaseMsg.ERROR, "手机号必须先注册，请联系人事注册手机号。", null);
			}
		}
		return baseMsg;
	}

}
