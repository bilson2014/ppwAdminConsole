package com.panfeng.taglib;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.service.SessionInfoService;
import com.panfeng.util.ValidateUtil;

public class OutNameTag extends TagSupport {

	private static final long serialVersionUID = -2010299046537359253L;

	public int doStartTag() throws JspException {
		final ServletContext sc = pageContext.getServletContext();
		WebApplicationContext  wc = WebApplicationContextUtils.findWebApplicationContext(sc);
		final SessionInfoService sessionService = (SessionInfoService) wc.getBean("sessionInfoService");
		
		final SessionInfo info = (SessionInfo) sessionService.getSessionWithField((HttpServletRequest)pageContext.getRequest(), GlobalConstant.SESSION_INFO);
		if(info != null){
			final String realName = info.getRealName();
			if(ValidateUtil.isValid(realName)){
				try {
					pageContext.getOut().print(realName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				try {
					pageContext.getOut().print(info.getLoginName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return EVAL_BODY_INCLUDE;
	}
}
