package com.panfeng.test.wang;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class SynergyTest {
	/*@Autowired
	StdSchedulerFactory schedulerFactory;
	@Test
	public void test1() throws SchedulerException, InterruptedException {
		// 获取调度器
		Scheduler scheduler = schedulerFactory.getScheduler();
		Trigger trigger = newTrigger().withIdentity("testTrigger", "testTriggergroup")
				.withSchedule(cronSchedule("0/5 * * * * ?")).build();
		
		// 布置一个任务 实现类/表达式
		JobDetail jobDetail = getJobDetail();
		jobDetail.getJobDataMap().put("testkey", "k123123k");
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
		MethodInvokingJobDetailFactoryBean k;
		Thread.sleep(60 * 1000);
		scheduler.shutdown();
	}*/
}
