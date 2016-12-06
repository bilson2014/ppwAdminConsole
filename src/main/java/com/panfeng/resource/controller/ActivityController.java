package com.panfeng.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.Activity;
import com.panfeng.service.ActivityService;

/**
 * 活动
 * 
 * @author wang
 *
 */
@RestController
@RequestMapping("/portal")
public class ActivityController extends BaseController {

	@Autowired
	private ActivityService activityService;

	@RequestMapping("/activity-list")
	public ModelAndView activityView() {
		return new ModelAndView("/activity-list");
	}

	@RequestMapping("/get/activities")
	public List<Activity> getAll() {
		try {
			return activityService.getAll();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping("/post/activity")
	public BaseMsg saveActivity(@RequestBody Activity activity) {
		return activityService.saveActivity(activity);
	}

	@RequestMapping("/put/activity")
	public BaseMsg updateActivity(@RequestBody Activity activity) {
		return activityService.updateActivity(activity);
	}
	
	@RequestMapping("/delete/activity")
	public BaseMsg deleteActivities(){
		return null;
	}
	
}
