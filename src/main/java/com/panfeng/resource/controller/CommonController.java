package com.panfeng.resource.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.resource.view.CurrentCustomer;
import com.panfeng.service.SessionInfoService;

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
}
