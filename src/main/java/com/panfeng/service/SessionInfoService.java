package com.panfeng.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SessionInfoService {

	public boolean addSession(final HttpServletRequest request,Map<String,Object> map);
	
	public Map<String,Object> getSessionWithAllFields(final HttpServletRequest request);
	
	public Object getSessionWithField(final HttpServletRequest request,final String field);
	
	public void removeSession(final HttpServletRequest request);
	
	public void updateSession(final HttpServletRequest request,final String filed,final String value);
}
