package com.panfeng.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class CallMonitor {
	
	private static Logger logger = LoggerFactory.getLogger("service");
	
	@Around("execution(* *..service..*.*(..))")
	private Object invoke(final ProceedingJoinPoint joinPoint) throws Throwable{
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
}
