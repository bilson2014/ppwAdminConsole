package com.panfeng.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.panfeng.dao.SessionInfoDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;

@Component
@Aspect
public class CallMonitor {
	@Autowired
	private final SessionInfoDao dao = null;
	//private static Logger logger = LoggerFactory.getLogger("service");
	
	@Around("execution(* *..service..*.*(..))")
	private Object invoke(final ProceedingJoinPoint joinPoint) throws Throwable{
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		final StopWatch sw = new StopWatch(joinPoint.toShortString());
		final String key = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "(...)";
		if(ra!=null){
			Log.error(key + " processing...",getCurrentInfo(ra.getSessionId()));
		}else{
			Log.error(key + " processing...",null);
		}
		sw.start("invoke");
		try {
			return joinPoint.proceed();
		} finally {
			sw.stop();
			if(ra!=null){
				Log.error(key + " execution time: " + sw.getTotalTimeMillis() + " ms",getCurrentInfo(ra.getSessionId()));
			}else{
				Log.error(key + " execution time: " + sw.getTotalTimeMillis() + " ms", null);
			}
		}
	}
	
	protected SessionInfo getCurrentInfo(final String sessionId){
		final String str = dao.getSessionWithSessionId(sessionId, GlobalConstant.SESSION_INFO);
		if (ValidateUtil.isValid(str)) {
			final SessionInfo info = RedisUtils.fromJson(str, SessionInfo.class);
			return info;
		}
		return null;
	}
}
