package com.panfeng.resource.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panfeng.resource.model.User;

/**
 * 资源基类
 * 专用于继承
 * @author GY
 *
 */
public abstract class BaseController {

	private static Logger logger = LoggerFactory.getLogger("error");
	
	// get current user
	protected User getUser (final HttpServletRequest request){
		
		User user = null;
		try {
			HttpSession session = request.getSession();
			user = (User) session.getAttribute("username");
		} catch (Exception e) {
			logger.error("Retrieve username error ...",e);
			e.printStackTrace();
		}
		if(user == null){
			user = new User();
			user.setUserName("缺省用户");
		}
		return user;
	}
	
}
