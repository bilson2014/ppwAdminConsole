package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.panfeng.domain.Result;
import com.panfeng.util.AESSecurityUtil;
import com.panfeng.util.DataUtil;

/**
 * 登陆相关
 * @author Jack
 *
 */

@RestController
@RequestMapping("/portal")
public class LoginController extends BaseController {
	
	@Autowired
	private final PmsEmployeeFacade pmsEmployeeFacade = null;
	
	@Autowired
	private final PmsRoleFacade pmsRoleFacade = null;
	
	@Autowired
	private final PmsRightFacade pmsRightFacade = null;
	
	@RequestMapping("/doLogin")
	public Result doLogin(final PmsEmployee employee,final HttpServletRequest request,
			final HttpServletResponse response){
		
		final String loginName = employee.getEmployeeLoginName();
		String password = employee.getEmployeePassword();
		
		if(ValidateUtil.isValid(password)){
			try {
				final String ps = AESSecurityUtil.Decrypt(password, PmsConstant.UNIQUE_KEY);
				password = DataUtil.md5(ps);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		final PmsEmployee e = pmsEmployeeFacade.doLogin(loginName,password);
		final Result result = new Result();
		if(e != null){
			// 登陆成功
			result.setRet(true);
			
			final SessionInfo info = new SessionInfo();
			info.setSessionType(PmsConstant.ROLE_EMPLOYEE);
			info.setReqiureId(e.getEmployeeId());
			info.setLoginName(e.getEmployeeLoginName());
			info.setRealName(e.getEmployeeRealName());
			info.setTelephone(e.getPhoneNumber());
			
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
			final long[] rightSum = new long[(int) (maxPos+ 1)];
			
			e.setRightSum(rightSum);
			e.calculateRightSum();
			long[] sum = e.getRightSum();
			info.setSum(sum);
			info.setSuperAdmin(e.isSuperAdmin()); // 判断是否是超级管理员
			
			final Map<String,Object> map = new HashMap<String, Object>();
			map.put(PmsConstant.SESSION_INFO, info);
			final HttpSession session = request.getSession();
			
			boolean ret = true;
			if(session.getAttribute(PmsConstant.SESSION_INFO) != null) {
				// 如果已经登录，那么提示登出后在登录
				ret = false;
			} else {
				session.setAttribute(PmsConstant.SESSION_INFO, info);
			}
			
			result.setRet(ret);
			result.setMessage("您当前已登录，如要切换账号，请登出后再进行登陆操作!");
			return result;
		}
		
		result.setRet(false);
		result.setMessage("用户名或密码错误!");
		return result;
	}
	
	@RequestMapping("/logout")
	public boolean logout(final HttpServletRequest request,final HttpServletResponse response){
		
		// logOutCookie(request,response);
		// 删除
		request.getSession().removeAttribute(PmsConstant.SESSION_INFO);
		return true;
	}
}
