package com.panfeng.test.wang.pay;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.domain.BaseJob;
import com.panfeng.service.QuartzService;
import com.panfeng.service.impl.QuartzServiceImpl;
import com.panfeng.test.wang.TestJob;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class QuartzTest {
	@Autowired
	QuartzService quartzService;

	@Test
	public void testGetAll() {
		List<BaseJob> triggersInfo = quartzService.getTriggersInfo();
		for (BaseJob baseJob : triggersInfo) {
			System.out.println(baseJob.getJobName());
		}
	}

	@Test
	public void testAddJob() throws SchedulerException {
		BaseJob baseJob = new BaseJob();
		baseJob.setJobName("qwe");
		baseJob.setJobGroup("qwe");
		baseJob.setJobClass(TestJob.class);
		baseJob.setCronExpression(QuartzServiceImpl.getCron(new Date()));
		quartzService.addJob(baseJob);
	}
}
