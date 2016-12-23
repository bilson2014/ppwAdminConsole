package com.panfeng.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.panfeng.domain.BaseJob;

public interface QuartzService {
	public void addOrUpdateJob(BaseJob baseJob) throws SchedulerException;

	public void removeJob(BaseJob baseJob) throws SchedulerException;

	public void pasueOneJob(BaseJob baseJob) throws SchedulerException;

	public void resumeOneJob(BaseJob baseJob) throws SchedulerException;
	
	public List<BaseJob> getTriggersInfo();
}
