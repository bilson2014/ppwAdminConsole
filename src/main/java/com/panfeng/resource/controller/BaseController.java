package com.panfeng.resource.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.paipianwang.pat.common.util.Constants;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.User;
import com.panfeng.util.Log;

/**
 * 资源基类
 * 专用于继承
 * @author GY
 *
 */
public abstract class BaseController {

	//private static Logger logger = LoggerFactory.getLogger("error");
	// get current user
	protected User getUser (final HttpServletRequest request){
		
		User user = null;
		try {
			HttpSession session = request.getSession();
			user = (User) session.getAttribute("username");
		} catch (Exception e) {
			Log.error("Retrieve username error ...",null,e);
			e.printStackTrace();
		}
		if(user == null){
			user = new User();
			user.setUserName("缺省用户");
		}
		return user;
	}
	
	
	protected SessionInfo getCurrentInfo(final HttpServletRequest request){
		//final SessionInfo info = (SessionInfo) sessionService.getSessionWithField(request, GlobalConstant.SESSION_INFO);
		final SessionInfo info = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		return info;
	}
	protected void addCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookieUsername = new Cookie("token", request.getSession().getId());
		cookieUsername.setPath("/");
		cookieUsername.setDomain(Constants.COOKIES_SCOPE);
		cookieUsername.setMaxAge(60 * 60 * 24 * 7); /* 设置cookie的有效期为 7 天 */
		response.addCookie(cookieUsername);
	}
	//退出时删除redis 删除cookie
		protected void logOutCookie(HttpServletRequest request,HttpServletResponse response) {
			Cookie[] cookie = request.getCookies();
			if(cookie!=null){
				if(cookie.length>0){
					for (Cookie c : cookie) {
						if ("token".equals(c.getName())) {
							request.getSession().removeAttribute(c.getValue());
							//sessionService.removeSessionByToken(request, c.getValue());
							Cookie cookieUsername = new Cookie("token", null);
							cookieUsername.setPath("/");
							cookieUsername.setDomain(Constants.COOKIES_SCOPE);
							cookieUsername.setMaxAge(0);
							response.addCookie(cookieUsername);
							
						}
					}
				}
			}
		}
}
