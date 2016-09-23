package com.panfeng.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panfeng.domain.SessionInfo;

public class Log {
	private static Logger logger = LoggerFactory.getLogger("error");

	public static void info(String msg, SessionInfo sessionInfo) {
		if (sessionInfo != null) {
			logger.info("用户：" + sessionInfo.getLoginName() + '\\' + sessionInfo.getRealName() + ",角色类型："+sessionInfo.getSessionType()+ ",   msg:" + msg);
		} else {
			logger.info(msg);
		}
	}

	public static void info(String msg, SessionInfo sessionInfo, Exception e) {
		if (sessionInfo != null) {
			logger.info("用户：" + sessionInfo.getLoginName() + '\\' + sessionInfo.getRealName() + ",角色类型："+sessionInfo.getSessionType()+ ",   msg:" + msg, e);
		} else {
			logger.info(msg,e);
		}
	}

	public static void error(String msg, SessionInfo sessionInfo) {
		if (sessionInfo != null) {
			logger.error("用户：" + sessionInfo.getLoginName() + '\\' + sessionInfo.getRealName() + ",角色类型："+sessionInfo.getSessionType()+ ",   msg:" + msg);
		} else {
			logger.error(msg);
		}
	}

	public static void error(String msg, SessionInfo sessionInfo, Exception e) {
		if (sessionInfo != null) {
			logger.error("用户：" + sessionInfo.getLoginName() + '\\' + sessionInfo.getRealName() + ",角色类型："+sessionInfo.getSessionType()+",   msg:" + msg, e);
		} else {
			logger.error(msg,e);
		}
	}
}
