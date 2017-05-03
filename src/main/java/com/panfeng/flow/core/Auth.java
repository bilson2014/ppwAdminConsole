package com.panfeng.flow.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.facade.right.service.PmsRoleFacade;

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
	private final PmsRoleFacade pmsRoleFacade = null;

	@Autowired
	private final PmsEmployeeFacade pmsEmployeeFacade = null;

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

		final List<PmsRole> roles = new ArrayList<>();

		switch (sessionInfo.getSessionType()) {
		case PmsConstant.ROLE_CUSTOMER:
			PmsRole role = pmsRoleFacade.findRoleById(3l);
			roles.add(role);
			break;
		case PmsConstant.ROLE_PROVIDER:
			role = pmsRoleFacade.findRoleById(2l);
			roles.add(role);
			break;
		case PmsConstant.ROLE_EMPLOYEE:
			PmsEmployee employee = pmsEmployeeFacade.findEmployeeById(sessionInfo.getReqiureId());
			final List<PmsRole> roleList = employee.getRoles();
			if(ValidateUtil.isValid(roleList)) {
				for (final PmsRole r : roleList) {
					role = pmsRoleFacade.findRoleById(r.getRoleId());
					roles.add(role);
				}
			}
			break;
		}

		for (int i = 0; i < assignee.length; i++) {
			for (PmsRole role : roles) {
				if (Long.parseLong(assignee[i]) == role.getRoleId()) {
					return true;
				}
			}
		}
		// 无权利进入下一个节点
		return false;
	}
}
