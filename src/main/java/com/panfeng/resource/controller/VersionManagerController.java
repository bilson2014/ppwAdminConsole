package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.facade.right.service.PmsRightFacade;
import com.paipianwang.pat.facade.right.service.PmsRoleFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.service.EmployeeService;
import com.panfeng.util.DataUtil;

/**
 * 视频管家管理
 * 
 * @author GY
 *
 */

@RestController
@RequestMapping("/portal")
public class VersionManagerController extends BaseController {

	@Autowired
	private final PmsEmployeeFacade pmsEmployeeFacade = null;
	
	@Autowired
	private final EmployeeService service = null;

	@Autowired
	private final PmsRoleFacade pmsRoleFacade = null;

	@Autowired
	private final PmsRightFacade pmsRightFacade = null;
	
	// -------------------- 前端方法 ----------------

	/**
	 * 三方登录验证
	 * 
	 * @param employee
	 * @param request
	 * @return
	 */
	@RequestMapping("/manager/thirdLogin/isExist")
	public boolean verificationTeamExist(@RequestBody final PmsEmployee employee, final HttpServletRequest request) {

		final List<PmsEmployee> list = pmsEmployeeFacade.verificationEmployeeExist(employee);
		if (ValidateUtil.isValid(list)) {
			if (list.size() == 1) {
				if (ValidateUtil.isValid(list.get(0).getPhoneNumber())) {
					// 绑定账户
					// 清除当前session
					// infoService.removeSession(request);
					request.getSession().removeAttribute(PmsConstant.SESSION_INFO);
					final PmsEmployee empl = list.get(0);
					// 存入session中
					return initSessionInfo(empl, request);
				}
			}
		}
		return false;
	}

	@RequestMapping("/manager/static/checkNumber/{phoneNumber}")
	public long isNumberExist(@PathVariable("phoneNumber") final String phoneNumber, final HttpServletRequest request) {

		if (ValidateUtil.isValid(phoneNumber)) {
			final long count = pmsEmployeeFacade.checkPhoneNumber(phoneNumber);
			return count;
		}

		return 0l;
	}

	@RequestMapping("/manager/thirdLogin/bind")
	public BaseMsg bind(@RequestBody final PmsEmployee employee, final HttpServletRequest request) {
		final BaseMsg baseMsg = service.bind(employee);
		if (baseMsg.getErrorCode().equals(BaseMsg.NORMAL) || baseMsg.getErrorCode().equals(BaseMsg.WARNING)) {
			boolean login = initSessionInfo((PmsEmployee) baseMsg.getResult(), request);
			if (!login) {
				return new BaseMsg(BaseMsg.ERROR, "绑定成功，登录失败", baseMsg.getResult());
			}
		}
		return baseMsg;
	}

	/**
	 * 初始化 sessionInfo 信息
	 */
	public boolean initSessionInfo(final PmsEmployee e, final HttpServletRequest request) {
		// 存入session中
		final String sessionId = request.getSession().getId();
		final SessionInfo info = new SessionInfo();
		info.setLoginName(e.getEmployeeLoginName());
		info.setRealName(e.getEmployeeRealName());
		info.setSessionType(PmsConstant.ROLE_EMPLOYEE);
		info.setToken(DataUtil.md5(sessionId));
		info.setReqiureId(e.getEmployeeId());
		info.setPhoto(e.getEmployeeImg());

		// 计算权限码
		// 替换带有权限的角色
		final List<PmsRole> roles = new ArrayList<PmsRole>();
		for (final PmsRole r : e.getRoles()) {
			final PmsRole role = pmsRoleFacade.findRoleById(r.getRoleId());
			roles.add(role);
		}
		e.setRoles(roles);

		// 计算权限码总和
		final long maxPos = pmsRightFacade.getMaxPos();
		final long[] rightSum = new long[(int) (maxPos + 1)];

		e.setRightSum(rightSum);
		e.calculateRightSum();
		long[] sum = e.getRightSum();
		info.setSum(sum);
		info.setSuperAdmin(e.isSuperAdmin()); // 判断是否是超级管理员
		request.getSession().setAttribute(PmsConstant.SESSION_INFO, info);
		return true;
	}
	
}
