package com.panfeng.taglib;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.panfeng.dao.RightDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Right;
import com.panfeng.service.SessionInfoService;
import com.panfeng.util.UrlResourceUtils;
import com.panfeng.util.ValidateUtil;

/**
 * 判断两个URL，至少有一个满足条件
 * @author Jack
 *
 */
public class SecurityTagMultipleParam extends TagSupport {

	private static final long serialVersionUID = 7037089506680059434L;

	private String uri;
	
	private String uri2;
	
	public int doStartTag() throws JspException {
		
		boolean flag = false;
		final ServletContext sc = pageContext.getServletContext();
		WebApplicationContext  wc = WebApplicationContextUtils.findWebApplicationContext(sc);
		final RightDao dao = (RightDao) wc.getBean("rightDao");
		final SessionInfoService sessionService = (SessionInfoService) wc.getBean("sessionInfoService");
		
		final SessionInfo info = (SessionInfo) sessionService.getSessionWithField((HttpServletRequest)pageContext.getRequest(), GlobalConstant.SESSION_INFO);
		
		final String contextPath = sc.getContextPath();
		
		if(info.isSuperAdmin()){
			 // 超级管理员
			return EVAL_BODY_INCLUDE;
		}else {
			
			flag = hasRight(info, uri, contextPath,dao);
			if(flag){
				return EVAL_BODY_INCLUDE;
			}else {
				// 判断另一个URL
				flag = hasRight(info, uri2, contextPath,dao);
				if(flag){
					return EVAL_BODY_INCLUDE;
				}else {
					return SKIP_BODY;
				}
			}
		}
		
	}

	public void setUri(String uri) {
		this.uri = uri;
	}


	public void setUri2(String uri2) {
		this.uri2 = uri2;
	}
	
	public boolean hasRight(final SessionInfo info,final String url,final String contextPath,final RightDao dao){
		if(ValidateUtil.isValid(uri)){
			final String requestUrl = UrlResourceUtils.URLResolver(url, contextPath);
			final Right right = dao.getRightFromRedis(requestUrl);
			if(right != null){
				if(info.hasRight(right)){
					return true;
				}
			}else {
				return false;
			}
		}else {
			return false;
		}
	
		return false;
	}
}