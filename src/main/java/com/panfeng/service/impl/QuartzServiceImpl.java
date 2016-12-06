package com.panfeng.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.panfeng.domain.BaseJob;
import com.panfeng.service.QuartzService;

/**
 * Quartz 封装
 * 
 * @author wang
 *
 */
@Service
public class QuartzServiceImpl implements QuartzService {
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	public void addJob(BaseJob baseJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// 唯一主键
		String jobName = baseJob.getJobName();
		String jobGroup = baseJob.getJobGroup();
		TriggerKey triggerKey = baseJob.getTriggerKey();
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (null == cronTrigger) {
			JobDetail jobDetail = JobBuilder.newJob(baseJob.getJobClass()).withIdentity(jobName, jobGroup).build();
			cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
					.withSchedule(CronScheduleBuilder.cronSchedule(baseJob.getCronExpression())).build();
			if (baseJob.getParam() != null)
				cronTrigger.getJobDataMap().put(BaseJob.PARAMKEY, baseJob.getParam());
			scheduler.scheduleJob(jobDetail, cronTrigger);
			// 启动一个定时器
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}
		} else {
			cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey)
					.withSchedule(CronScheduleBuilder.cronSchedule(baseJob.getCronExpression())).build();
			scheduler.rescheduleJob(triggerKey, cronTrigger);
		}
	}

	public void removeJob(BaseJob baseJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = baseJob.getTriggerKey();
		Trigger trigger = scheduler.getTrigger(triggerKey);
		if (null == trigger) {
			return;
		}
		JobKey jobKey = trigger.getJobKey();
		scheduler.pauseTrigger(triggerKey);// 停止触发器
		scheduler.unscheduleJob(triggerKey);// 移除触发器
		scheduler.deleteJob(jobKey);// 删除任务
	}

	public void modifyJobTime(BaseJob baseJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = baseJob.getTriggerKey();
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(baseJob.getCronExpression());
		cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder)
				.build();
		scheduler.rescheduleJob(triggerKey, cronTrigger);
	}

	public void pasueOneJob(BaseJob baseJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = baseJob.getTriggerKey();
		Trigger trigger = scheduler.getTrigger(triggerKey);
		if (null == trigger) {
			return;
		}
		JobKey jobKey = trigger.getJobKey();
		scheduler.pauseJob(jobKey);
	}

	public void resOneJob(BaseJob baseJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = baseJob.getTriggerKey();
		Trigger trigger = scheduler.getTrigger(triggerKey);
		if (null == trigger) {
			return;
		}
		scheduler.rescheduleJob(triggerKey, trigger);
	}
	
    //获取所有的触发器  
    public List<BaseJob> getTriggersInfo(){  
        try {
        	Scheduler scheduler = schedulerFactoryBean.getScheduler();
            GroupMatcher<TriggerKey> matcher = GroupMatcher.anyTriggerGroup();  
            Set<TriggerKey> Keys = scheduler.getTriggerKeys(matcher);  
            List<BaseJob> triggers = new ArrayList<BaseJob>();  
              
            for (TriggerKey key : Keys) {  
                Trigger trigger = scheduler.getTrigger(key);  
                BaseJob pageTrigger = new BaseJob();  
                pageTrigger.setJobName(trigger.getJobKey().getName());  
                pageTrigger.setJobGroup(trigger.getJobKey().getGroup());  
                pageTrigger.setTriggerState(scheduler.getTriggerState(key));  
                if (trigger instanceof SimpleTrigger) {
                    SimpleTrigger simple = (SimpleTrigger) trigger;  
                    //pageTrigger.setExpression("重复次数:"+ (simple.getRepeatCount() == -1 ?   
                     //       "无限" : simple.getRepeatCount()) +",重复间隔:"+(simple.getRepeatInterval()/1000L));  
                    //pageTrigger.setDesc(simple.getDescription());  
                }  
                if (trigger instanceof CronTrigger) {  
                    CronTrigger cron = (CronTrigger) trigger;  
                    pageTrigger.setCronExpression(cron.getCronExpression());  
                }  
                triggers.add(pageTrigger);  
            }  
            return triggers;  
        } catch (SchedulerException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }

	/////////////////////////////////////////////////////////////////////
	private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

	public static String getCron(final Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
		String formatTimeStr = "";
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}
}
