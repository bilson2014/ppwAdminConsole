package com.panfeng.resource.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.model.User;
import com.panfeng.resource.view.CurrentCustomer;
import com.panfeng.service.SessionInfoService;
import com.panfeng.service.TeamService;
import com.panfeng.service.UserService;
import com.panfeng.util.ValidateUtil;

/**
 * 公共controller
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class CommonController extends BaseController {

	@Autowired
	private final SessionInfoService service = null;
	@Autowired
	private final UserService userService = null;
	@Autowired
	private final TeamService teamService = null;
	
	/**
	 * 获取当前登录者
	 */
	@RequestMapping("/common/loadCurrentUser")
	public Object currentUser(final HttpServletRequest request,@RequestBody final CurrentCustomer current){
		final Object obj = service.getSessionWithField(request, current.getField());
		return obj;
	}
	
	/**
	 * 登出当前登录者
	 */
	@RequestMapping("/common/loginout")
	public boolean loginout(final HttpServletRequest request){
		service.removeSession(request);
		return true;
	}
	
	/**
	 * 更新当前session内容
	 */
	@RequestMapping("/common/updateSession")
	public void updateSession(final HttpServletRequest request,@RequestBody final CurrentCustomer current){
		
		service.updateSession(request, current.getField(), current.getValue());
	}
	
	/**
	 * 验证登录者是否完善登录名,密码
	 * 	 ROLE_EMPLOYEE = "role_employee"; // 用户身份 -- 内部员工
	 *	 ROLE_CUSTOMER = "role_customer"; // 用户身份 -- 客户
     *   ROLE_PROVIDER = "role_provider"; // 用户身份 -- 供应商
	 */
	@RequestMapping("/loginName/validate")
	public boolean loginNameValidate(final HttpServletRequest request){
		SessionInfo sessionInfo = getCurrentInfo(request);
		if(null!=sessionInfo){
			String type = sessionInfo.getSessionType();
			switch (type) {
			case "role_customer":
				User user = userService.findUserById(sessionInfo.getReqiureId());
				if(ValidateUtil.isValid(user.getLoginName())){
					return true;
				}
				break;
			case "role_provider":
				Team team = teamService.findTeamById(sessionInfo.getReqiureId());
				if(ValidateUtil.isValid(team.getLoginName())){
					return true;
				}
				break;
			}
		}
		return false;
	}
}
