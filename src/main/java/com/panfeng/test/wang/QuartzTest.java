package com.panfeng.test.wang;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class QuartzTest {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Test
	public void addJob() {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			
			// 唯一主键
			String jobName = "wang1";
			String jobGroup = "wang1";
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null == cronTrigger) {
				JobDetail jobDetail = JobBuilder.newJob(TestJob.class).withIdentity(jobName, jobGroup).build();

				cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
						.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
				// cronTrigger.getJobDataMap().put("jobEntity", myJob);

				scheduler.scheduleJob(jobDetail, cronTrigger);
				// 启动一个定时器
				if (!scheduler.isShutdown()) {
					scheduler.start();
				}
			} else {
				cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey)
						.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
				scheduler.rescheduleJob(triggerKey, cronTrigger);
			}
			// 等待启动完成
			Thread.sleep(1000000000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public  void pasueOneJob() {  
        try {  
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            // 唯一主键  
            String jobName = "";  
            String jobGroup = "";  
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);  
            Trigger trigger = scheduler.getTrigger(triggerKey);  
//          if(null==trigger){  
//              log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:removeJob trigger is NULL ");  
//              return;  
//          }  
            JobKey jobKey = trigger.getJobKey();  
            scheduler.pauseJob(jobKey);  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
    public  void resOneJob() {  
        try {  
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            // 唯一主键  
            String jobName = "";  
            String jobGroup = "";  
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);  
            Trigger trigger = scheduler.getTrigger(triggerKey);  
//          if(null==trigger){  
//              log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:removeJob trigger is NULL ");  
//              return;  
//          }  
            scheduler.rescheduleJob(triggerKey, trigger);  
              
//          Thread.sleep(TimeUnit.MINUTES.toMillis(10));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    } 
    
    public  void modifyJobTime( String time) {  
        try {  
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            // 唯一主键  
            String jobName = "";  
            String jobGroup = "";  
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);  
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);  
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("");  
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder)  
                    .build();  
            scheduler.rescheduleJob(triggerKey, cronTrigger);  
            
//          Thread.sleep(TimeUnit.MINUTES.toMillis(60));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
    public  void removeJob() {  
        try {  
            Scheduler scheduler = schedulerFactoryBean.getScheduler();  
            // 唯一主键  
            String jobName = "";  
            String jobGroup = "";  
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);  
            Trigger trigger = scheduler.getTrigger(triggerKey);  
            if(null==trigger){  
                return;  
            }  
            JobKey jobKey = trigger.getJobKey();  
            scheduler.pauseTrigger(triggerKey);// 停止触发器  
            scheduler.unscheduleJob(triggerKey);// 移除触发器  
            scheduler.deleteJob(jobKey);// 删除任务  
              
            //Thread.sleep(TimeUnit.MINUTES.toMillis(10));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
}
