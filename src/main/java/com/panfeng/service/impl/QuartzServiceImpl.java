package com.panfeng.service.impl;

import java.util.ArrayList;
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
	String PARAMKEY = "paramkey";

	public void addOrUpdateJob(BaseJob baseJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// 唯一主键
		String jobName = baseJob.getJobName();
		String jobGroup = baseJob.getJobGroup();
		TriggerKey triggerKey = baseJob.getTriggerKey();
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (null == cronTrigger) {
			JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName + "_Job", jobGroup));
			// 一个job类可以执行多种相同性质但是参数不同的任务
			// 通过JobDetail 进行关联，一个job对应着多个JobDetail。
			if (jobDetail == null)
				jobDetail = JobBuilder.newJob(baseJob.getJobClass()).withIdentity(jobName + "_Job", jobGroup).requestRecovery().build();

			cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName + "_trigger", jobGroup)
					.withSchedule(CronScheduleBuilder.cronSchedule(baseJob.getCronExpression())).build();
			
			if (baseJob.getParam() != null)
				jobDetail.getJobDataMap().put(PARAMKEY, baseJob.getParam().toString());
			scheduler.scheduleJob(jobDetail, cronTrigger);
			// 启动一个定时器
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}
		} else {
			removeJob(baseJob);
			addOrUpdateJob(baseJob);
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

	public void pasueOneJob(BaseJob baseJob) throws SchedulerException {
		// 暂停触发器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = baseJob.getTriggerKey();
		Trigger trigger = scheduler.getTrigger(triggerKey);
		if (null == trigger) {
			return;
		}
		JobKey jobKey = trigger.getJobKey();
		scheduler.pauseJob(jobKey);
	}

	public void resumeOneJob(BaseJob baseJob) throws SchedulerException {
		// 恢复触发器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = baseJob.getTriggerKey();
		Trigger trigger = scheduler.getTrigger(triggerKey);
		if (null == trigger) {
			return;
		}
		JobKey jobKey = trigger.getJobKey();
		scheduler.resumeJob(jobKey);  
		scheduler.rescheduleJob(triggerKey, trigger);
	}

	// 获取所有的触发器
	public List<BaseJob> getTriggersInfo() {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			GroupMatcher<TriggerKey> matcher = GroupMatcher.anyTriggerGroup(); // 获取所有分组
			Set<TriggerKey> Keys = scheduler.getTriggerKeys(matcher); // 获取所有key
			List<BaseJob> triggers = new ArrayList<BaseJob>();
			for (TriggerKey key : Keys) {
				Trigger trigger = scheduler.getTrigger(key);
				BaseJob pageTrigger = new BaseJob();
				pageTrigger.setJobName(trigger.getJobKey().getName());
				pageTrigger.setJobGroup(trigger.getJobKey().getGroup());
				pageTrigger.setTriggerState(scheduler.getTriggerState(key));
				if (trigger instanceof SimpleTrigger) {
					// SimpleTrigger simple = (SimpleTrigger) trigger;
					// pageTrigger.setExpression("重复次数:"+
					// (simple.getRepeatCount() == -1 ?
					// "无限" : simple.getRepeatCount())
					// +",重复间隔:"+(simple.getRepeatInterval()/1000L));
					// pageTrigger.setDesc(simple.getDescription());
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

}
