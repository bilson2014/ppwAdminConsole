package com.panfeng.resource.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;
import com.paipianwang.pat.facade.user.entity.PmsUser;
import com.paipianwang.pat.facade.user.service.PmsUserFacade;
import com.panfeng.domain.SessionInfo;
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
	private final PmsTeamFacade pmsTeamFacade = null;
	@Autowired
	private final PmsUserFacade pmsUserFacade = null;
	
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
				//User user = userService.findUserById(sessionInfo.getReqiureId());
				PmsUser user = pmsUserFacade.findUserById(sessionInfo.getReqiureId());
				if(ValidateUtil.isValid(user.getLoginName())){
					return true;
				}return false;
			case "role_provider":
				//Team team = teamService.findTeamById(sessionInfo.getReqiureId());
				PmsTeam team = pmsTeamFacade.findTeamById(sessionInfo.getReqiureId());
				if(ValidateUtil.isValid(team.getLoginName())){
					return true;
				}return false;
			}
		}
		return true;
	}
}
