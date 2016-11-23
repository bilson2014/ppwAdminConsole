package com.panfeng.flow.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.model.Role;
import com.panfeng.service.EmployeeService;
import com.panfeng.service.RoleService;

/**
 * 认证当前用户是否有权利完成当前节点</br>
 * 每当有步骤节点切换的时候检测 </br>
 * 流程图中认证字段： “assignee”
 * 
 * @author wang
 *
 */
@Component
public class Auth {
	@Autowired
	private final RoleService roleService = null;

	@Autowired
	private final EmployeeService employeeService = null;

	/**
	 * 认证当前用户是否有权限执行当前步骤
	 * 
	 * @param request
	 * @param activityImpl
	 * @return
	 */
	public boolean auth(SessionInfo sessionInfo, String assignees) {
		/**
		 * 1.分离节点内部允许执行角色集合 </br>
		 * 2.分离当前用户权限集合 </br>
		 * 3.认证用所拥有的角色是否被当前节点承认
		 */

		String[] assignee = assignees.split("\\,");

		final List<Role> roles = new ArrayList<>();

		switch (sessionInfo.getSessionType()) {
		case GlobalConstant.ROLE_CUSTOMER:
			Role role = roleService.findRoleById(3l);
			roles.add(role);
			break;
		case GlobalConstant.ROLE_PROVIDER:
			role = roleService.findRoleById(2l);
			roles.add(role);
			break;
		case GlobalConstant.ROLE_EMPLOYEE:
			Employee employee = employeeService.findEmployerById(sessionInfo.getReqiureId());
			for (final Role r : employee.getRoles()) {
				role = roleService.findRoleById(r.getRoleId());
				roles.add(role);
			}
			break;
		}

		for (int i = 0; i < assignee.length; i++) {
			for (Role role : roles) {
				if (Long.parseLong(assignee[i]) == role.getRoleId()) {
					return true;
				}
			}
		}
		// 无权利进入下一个节点
		return false;
	}
}
