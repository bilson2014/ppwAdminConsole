package com.panfeng.service.impl.job;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.panfeng.mq.service.MailMQService;
import com.panfeng.resource.model.Activity;
import com.panfeng.service.ActivityService;
import com.panfeng.service.JobParamParser;
import com.panfeng.util.Log;

public class SendMailJob implements Job {

	@Autowired
	private JobParamParser jobParamParser;

	@Autowired
	private MailMQService mailMQService;

	@Autowired
	private ActivityService activityService;
	String PARAMKEY = "paramkey";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobExecutionContext context2 = context;
		Object object = context2.getJobDetail().getJobDataMap().get(PARAMKEY);
		if (object != null && object instanceof String) {
			Long activityId = Long.valueOf(object.toString());
			try {
				Activity activity = activityService.getActivityById(activityId);
				Map<String, String[]> parser = jobParamParser.parser(activityId);
				mailMQService.sendMailsByType(activity.getActicityTempleteId(), parser);
			} catch (Exception e) {
				e.printStackTrace();
				Log.error("send mail fail ...", null, e);
			}
		}
	}

}
