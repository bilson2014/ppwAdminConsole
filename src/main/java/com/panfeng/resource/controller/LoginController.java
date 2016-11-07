package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.Result;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.model.Role;
import com.panfeng.service.EmployeeService;
import com.panfeng.service.RightService;
import com.panfeng.service.RoleService;
import com.panfeng.service.SessionInfoService;
import com.panfeng.util.AESUtil;
import com.panfeng.util.DataUtil;
import com.panfeng.util.ValidateUtil;

/**
 * 登陆相关
 * @author Jack
 *
 */

@RestController
@RequestMapping("/portal")
public class LoginController extends BaseController {
	
	@Autowired
	private final EmployeeService eService = null;
	
	@Autowired
	private final RoleService rService = null;
	
	@Autowired
	private final RightService rightService = null;
	
	@Autowired
	private final SessionInfoService sessionService = null;
	
	@RequestMapping("/doLogin")
	public Result doLogin(final Employee employee,final HttpServletRequest request,
			final HttpServletResponse response){
		
		final String loginName = employee.getEmployeeLoginName();
		String password = employee.getEmployeePassword();
		
		if(ValidateUtil.isValid(password)){
			try {
				final String ps = AESUtil.Decrypt(password, GlobalConstant.UNIQUE_KEY);
				password = DataUtil.md5(ps);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		final Employee e = eService.doLogin(loginName,password);
		final Result result = new Result();
		if(e != null){
			// 登陆成功
			result.setRet(true);
			
			//final HttpSession session = request.getSession();
			final SessionInfo info = new SessionInfo();
			info.setSessionType(GlobalConstant.ROLE_EMPLOYEE);
			info.setReqiureId(e.getEmployeeId());
			info.setLoginName(e.getEmployeeLoginName());
			info.setRealName(e.getEmployeeRealName());
			
			// 计算权限码
			// 替换带有权限的角色
			final List<Role> roles = new ArrayList<Role>();
			for (final Role r : e.getRoles()) {
				
				final Role role = rService.findRoleById(r.getRoleId());
				roles.add(role);
			}
			e.setRoles(roles);

			// 计算权限码总和
			final long maxPos = rightService.getMaxPos();
			final long[] rightSum = new long[(int) (maxPos+ 1)];
			
			e.setRightSum(rightSum);
			e.calculateRightSum();
			long[] sum = e.getRightSum();
			info.setSum(sum);
			info.setSuperAdmin(e.isSuperAdmin()); // 判断是否是超级管理员
			
			//session.setAttribute(GlobalConstant.SESSION_INFO, info);
			final Map<String,Object> map = new HashMap<String, Object>();
			map.put(GlobalConstant.SESSION_INFO, info);
			final boolean ret = sessionService.addSession(request, map);
			
			addCookies(request,response);
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
		
		//HttpSession session = request.getSession();
		//session.setAttribute(GlobalConstant.SESSION_INFO, null);
		logOutCookie(request,response);
		// 删除
		sessionService.removeSession(request);
		return true;
	}
}
