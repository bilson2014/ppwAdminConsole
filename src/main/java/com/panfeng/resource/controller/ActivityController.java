package com.panfeng.resource.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Activity;
import com.panfeng.resource.view.ActivityView;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.ActivityService;
import com.panfeng.util.Log;

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
	
	@RequestMapping(value = "/activitie/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public DataGrid<Activity> list(final ActivityView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		DataGrid<Activity> dataGrid = new DataGrid<Activity>();
		List<Activity> list;
		try {
			list = activityService.listWithPagination(view);
			dataGrid.setRows(list);
			final long total = activityService.maxSize(view);
			dataGrid.setTotal(total);
			return dataGrid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@RequestMapping("/post/activity")
	public BaseMsg saveActivity(@RequestBody Activity activity, HttpServletRequest request) {

		try {
			BaseMsg res = activityService.saveActivity(activity);
			return res;
		} catch (SchedulerException | ParseException e) {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("create activity failed", sessionInfo, e);
			return new BaseMsg(BaseMsg.ERROR, "创建失败", "");
		}
	}

	@RequestMapping("/put/activity")
	public BaseMsg updateActivity(@RequestBody Activity activity, HttpServletRequest request) {
		try {
			return activityService.updateActivity(activity);
		} catch (ParseException | SchedulerException e) {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("activity update failed", sessionInfo, e);
			return new BaseMsg(BaseMsg.ERROR, "更新失败", "");
		}
	}

	@RequestMapping("/delete/activity")
	public BaseMsg deleteActivities(String eventIds, HttpServletRequest request) {
		String[] ids = eventIds.split("\\,|，");
		Long[] lIds = new Long[ids.length];
		SessionInfo sessionInfo = getCurrentInfo(request);
		for (int i = 0; i < ids.length; i++) {
			lIds[i] = Long.valueOf(ids[i]);
		}
		try {
			activityService.deleteActivities(lIds);
		} catch (SchedulerException e) {
			Log.error("delete activity failed", sessionInfo, e);
		}
		return new BaseMsg(BaseMsg.NORMAL, "", true);
	}

}
