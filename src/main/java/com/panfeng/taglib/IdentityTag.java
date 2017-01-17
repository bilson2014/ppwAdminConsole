package com.panfeng.taglib;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.util.ValidateUtil;

/**
 * 该自定义标签用于判断当前登录角色
 * 符合当前登录角色，输出;反之则不输出
 * @author Jack
 *
 */
public class IdentityTag extends TagSupport{

	private static final long serialVersionUID = 2294078545765614257L;
	
	private String role = null;

	public int doStartTag() throws JspException {
		if(ValidateUtil.isValid(role)){
			final ServletContext sc = pageContext.getServletContext();
			// WebApplicationContext  wc = WebApplicationContextUtils.findWebApplicationContext(sc);
			// final SessionInfoService sessionService = (SessionInfoService) wc.getBean("sessionInfoService");
			
			// final SessionInfo info = (SessionInfo) sessionService.getSessionWithField((HttpServletRequest)pageContext.getRequest(), GlobalConstant.SESSION_INFO);
			final SessionInfo info = (SessionInfo) pageContext.getSession().getAttribute(GlobalConstant.SESSION_INFO);
			if(info != null){
				final String currentRole = info.getSessionType();
				// 已经登陆,判断角色
				if(currentRole.indexOf(role) > -1){
					return EVAL_BODY_INCLUDE;
				}
			}
		}
		return SKIP_BODY;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
