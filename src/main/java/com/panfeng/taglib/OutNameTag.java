package com.panfeng.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.util.ValidateUtil;

public class OutNameTag extends TagSupport {

	private static final long serialVersionUID = -2010299046537359253L;

	public int doStartTag() throws JspException {
		final SessionInfo info = (SessionInfo) pageContext.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		
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
