package com.panfeng.test.wang;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.jdbcjobstore.JobStoreSupport;
import org.springframework.stereotype.Component;
@Component
public class TestJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("123123qweqweqweqweqweqeweqwe");
	}
}
