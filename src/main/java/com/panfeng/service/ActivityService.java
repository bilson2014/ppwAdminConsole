package com.panfeng.service;

import java.text.ParseException;
import java.util.List;

import org.quartz.SchedulerException;

import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.Activity;
import com.panfeng.resource.view.ActivityView;

public interface ActivityService {
	List<Activity> getAll() throws Exception;

	BaseMsg saveActivity(Activity activity) throws SchedulerException, ParseException;

	BaseMsg updateActivity(Activity activity) throws ParseException, SchedulerException;

	BaseMsg deleteActivities(Long[] ids) throws SchedulerException;

	Activity getActivityById(Long activityId) throws Exception;
	
	List<Activity> listWithPagination(final ActivityView view);

	long maxSize(ActivityView view);

}

