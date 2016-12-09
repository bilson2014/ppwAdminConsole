package com.panfeng.service.impl.job;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.panfeng.mq.service.MailMQService;
import com.panfeng.mq.service.SmsMQService;
import com.panfeng.resource.model.Activity;
import com.panfeng.resource.model.Sms;
import com.panfeng.service.ActivityService;
import com.panfeng.service.JobParamParser;
import com.panfeng.service.SMSTemplateService;
import com.panfeng.util.Log;

public class SendSmsJob implements Job {

	@Autowired
	private JobParamParser jobParamParser;

	@Autowired
	private SmsMQService smsMQService;

	@Autowired
	private ActivityService activityService;
	String PARAMKEY = "paramkey";

	@Autowired
	private SMSTemplateService smsTemplateService;
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobExecutionContext context2 = context;
		Object object = context2.getJobDetail().getJobDataMap().get(PARAMKEY);
		if (object != null && object instanceof String) {
			Long activityId = Long.valueOf(object.toString());
			try {
				Activity activity = activityService.getActivityById(activityId);
				Sms findSmsById = smsTemplateService.findSmsById(Long.parseLong(activity.getActicityTempleteId()));
				String content = findSmsById.getTempContent();
				
				Map<String, String[]> parser = jobParamParser.parser(activityId);
				Set<String> key = parser.keySet();
				for (String keyString : key) {
					String[] value = parser.get(keyString);
					String[] sortParam = new String[value.length];
					// 针对模板参数序列重排
					if (content != null) {
						Pattern pattern = Pattern.compile("\\{(.*?)\\}");
						Matcher matcher = pattern.matcher(content);
						int i = 0;
						while (matcher.find()) {
							int index = Integer.parseInt(matcher.group(1)) - 1;
							sortParam[i] = value[index];
							i++;
						}
						smsMQService.sendMessage(activity.getActicityTempleteId(), keyString, sortParam);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.error("send sms fail ...", null, e);
			}
		}
	}

}
