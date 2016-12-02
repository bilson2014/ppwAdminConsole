package com.panfeng.service;

import java.util.List;

import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.Activity;

public interface ActivityService {
	List<Activity> getAll();

	BaseMsg saveActivity(Activity activity);

	BaseMsg updateActivity(Activity activity);

	BaseMsg deleteActivities(Long[] ids);
}
