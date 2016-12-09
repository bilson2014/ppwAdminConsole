package com.panfeng.test.wang.pay;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.domain.BaseJob;
import com.panfeng.service.QuartzService;
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
	public void testAddJob() {
		BaseJob baseJob = new BaseJob();
		baseJob.setJobName("6_trigger");
		baseJob.setJobGroup(BaseJob.ACTIVITY);
		baseJob.setJobClass(TestJob.class);
		try {
			baseJob.setCronExpression("10 01 12 09 12 ? 2016");
			quartzService.addOrUpdateJob(baseJob);
		} catch (SchedulerException e1) {
			e1.printStackTrace();
		}
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRemoveJob() {
		BaseJob baseJob = new BaseJob();
		baseJob.setJobName("qwe");
		baseJob.setJobGroup(BaseJob.ACTIVITY);
		try {
			quartzService.removeJob(baseJob);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPasueTrigger() {
		BaseJob baseJob = new BaseJob();
		baseJob.setJobName("qwe");
		baseJob.setJobGroup(BaseJob.ACTIVITY);
		try {
			quartzService.pasueOneJob(baseJob);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testResumeTrigger() {
		BaseJob baseJob = new BaseJob();
		baseJob.setJobName("qwe");
		baseJob.setJobGroup(BaseJob.ACTIVITY);
		try {
			quartzService.resumeOneJob(baseJob);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
