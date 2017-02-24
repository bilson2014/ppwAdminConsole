package com.panfeng.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.panfeng.dao.SessionInfoDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;

@Component
@Aspect
public class CallMonitor {
	
	private final Logger logger = LoggerFactory.getLogger(CallMonitor.class);
	
	@Autowired
	private final SessionInfoDao dao = null;
	//private static Logger logger = LoggerFactory.getLogger("service");
	
	@Around("execution(* *..service..*.*(..))")
	private Object invoke(final ProceedingJoinPoint joinPoint) throws Throwable{
		/*RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
		HttpSession session = request.getSession();*/
		final StopWatch sw = new StopWatch(joinPoint.toShortString());
		final String key = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "(...)";
		logger.info(key + " processing...");
		sw.start("invoke");
		try {
			return joinPoint.proceed();
		} finally {
			sw.stop();
			logger.info(key + " execution time: " + sw.getTotalTimeMillis() + " ms");
		}
	}
	
	protected SessionInfo getCurrentInfo(final HttpSession session){
		// final String str = dao.getSessionWithSessionId(sessionId, GlobalConstant.SESSION_INFO);
		final SessionInfo info = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		/*final String str = ra.getAttribute(GlobalConstant.SESSION_INFO, SessionScope);
		if (ValidateUtil.isValid(str)) {
			final SessionInfo info = RedisUtils.fromJson(str, SessionInfo.class);
			return info;
		}*/
		return info;
	}
	
}
