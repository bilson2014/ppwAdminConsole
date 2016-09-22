package com.panfeng.dao;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SessionInfoDao {

	public boolean addSession(final HttpServletRequest request,Map<String,String> map);
	
	/**
	 * 获取该session的所有字段值
	 */
	public Map<String,String> getSessionWithAllFields(final HttpServletRequest request);
	
	/**
	 * 获取指定字段的值
	 */
	public String getSessionWithField(final HttpServletRequest request,final String field);
	
	public void removeSession(final HttpServletRequest request);
	
	/**
	 * 更新session字段内容
	 */
	public void updateSession(final HttpServletRequest request,final String filed,final String value);

	/**
	 * 判断session是否存在
	 */
	public boolean exitSession(final HttpServletRequest request);

	public boolean addSessionSeveralTime(HttpServletRequest request, Map<String, String> destMap, int time);

	public String getSessionWithToken(HttpServletRequest request, String token);

	public void removeSessionByToken(HttpServletRequest request, String token);
}
