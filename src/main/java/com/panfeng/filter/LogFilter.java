package com.panfeng.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.MDC;

import com.panfeng.resource.model.User;


public class LogFilter implements Filter{

	private static final String REQUEST_KEY = "request";
	
	private static final String SESSION_KEY = "session";
	
	private static final String USER_KEY = "username";
	
	private static final String CLIENT_KEY = "client";
	
	@Override
	public void destroy() {
		// empty
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		final User user = (User) session.getAttribute(USER_KEY);
		String username = user == null ? null : user.getUserName();
		if (username == null || username.trim().isEmpty()) {
			username = "缺省用户";
		}
		final String clientIP = getRemoteAddress(req);
		MDC.put(REQUEST_KEY, req.getRequestURI());
		MDC.put(SESSION_KEY, session.getId().substring(0, 5));
		MDC.put(USER_KEY, username);
		MDC.put(CLIENT_KEY, clientIP);
		try {
			chain.doFilter(request, response);
		} finally {
			MDC.remove(REQUEST_KEY);
			MDC.remove(SESSION_KEY);
			MDC.remove(USER_KEY);
			MDC.remove(CLIENT_KEY);
		}
	}

	private String getRemoteAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.trim().isEmpty() || ip.equalsIgnoreCase("unknown")){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.trim().isEmpty() || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.trim().isEmpty() || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// empty
		
	}

}
