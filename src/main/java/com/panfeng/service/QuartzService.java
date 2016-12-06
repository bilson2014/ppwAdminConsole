package com.panfeng.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.panfeng.domain.BaseJob;

public interface QuartzService {
	public void addJob(BaseJob baseJob) throws SchedulerException;

	public void removeJob(BaseJob baseJob) throws SchedulerException;

	public void modifyJobTime(BaseJob baseJob) throws SchedulerException;

	public void pasueOneJob(BaseJob baseJob) throws SchedulerException;

	public void resOneJob(BaseJob baseJob) throws SchedulerException;
	
	public List<BaseJob> getTriggersInfo();
}
