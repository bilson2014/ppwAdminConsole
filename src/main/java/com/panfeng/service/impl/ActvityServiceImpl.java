package com.panfeng.service.impl;

import java.text.ParseException;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.domain.BaseJob;
import com.panfeng.domain.BaseMsg;
import com.panfeng.persist.ActivityMapper;
import com.panfeng.resource.model.Activity;
import com.panfeng.resource.model.Activity.param;
import com.panfeng.resource.view.ActivityView;
import com.panfeng.service.ActivityService;
import com.panfeng.service.QuartzService;
import com.panfeng.service.impl.job.SendMailJob;
import com.panfeng.service.impl.job.SendSmsJob;
import com.panfeng.util.DateUtils;
import com.panfeng.util.JsonUtil;
import com.panfeng.util.ValidateUtil;

@Service
public class ActvityServiceImpl implements ActivityService {
	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private QuartzService quartzService;

	@Override
	public List<Activity> getAll() throws Exception {
		List<Activity> activityList = activityMapper.findAll();
		if (ValidateUtil.isValid(activityList)) {
			for (Activity activity : activityList) {
				String json = activity.getActivityParamList();
				if (ValidateUtil.isValid(json)) {
					List<param> paramList = JsonUtil.fromJsonArray(json, param.class);
					activity.setParamList(paramList);
				}
			}
		}
		return activityList;
	}

	@Override
	public BaseMsg saveActivity(Activity activity) throws SchedulerException, ParseException {
		paramConversionToString(activity);
		Long res = activityMapper.save(activity);
		if (res != null && res > 0) {
			// 添加定时器
			BaseJob baseJob = getActivityJob(activity);
			quartzService.addOrUpdateJob(baseJob);
			return new BaseMsg(BaseMsg.NORMAL, "", "新增成功！");
		} else {
			return new BaseMsg(BaseMsg.ERROR, "", "新增失败！");
		}

	}

	@Override
	public BaseMsg updateActivity(Activity activity) throws ParseException, SchedulerException {
		paramConversionToString(activity);
		Long res = activityMapper.update(activity);
		if (res != null && res > 0) {
			// 更新定时任务
			BaseJob baseJob = getActivityJob(activity);
			quartzService.addOrUpdateJob(baseJob);
			return new BaseMsg(BaseMsg.NORMAL, "", "修改成功！");
		} else {
			return new BaseMsg(BaseMsg.ERROR, "", "修改失败！");
		}
	}

	@Override
	public BaseMsg deleteActivities(Long[] ids) throws SchedulerException {
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				BaseJob baseJob = new BaseJob();
				baseJob.setJobGroup(BaseJob.ACTIVITY);
				baseJob.setJobName(ids[i] + "");
				quartzService.removeJob(baseJob);
			}
			Long res = activityMapper.delete(ids);
			if (res != null && res > 0) {
				return new BaseMsg(BaseMsg.NORMAL, "", "删除成功！");
			} else {
				return new BaseMsg(BaseMsg.ERROR, "", "删除失败！");
			}
		} else {
			return new BaseMsg(BaseMsg.ERROR, "", "删除失败！");
		}

	}

	public void parseParam(Activity activity) throws Exception {
		paramConversionToObj(activity);
		List<param> paramList = activity.getParamList();
		if (ValidateUtil.isValid(paramList)) {
			for (param param : paramList) {
				if (param.getType() == Activity.SYSTEMPARAM) {
					// 识别参数
					// <option value="0">供应商名称</option>
					// <option value="1">客户名称</option>
				} else if (param.getType() == Activity.CUSTOMPARAM) {

				}
			}
		}
	}

	private void paramConversionToString(Activity activity) {
		List<param> paramList = activity.getParamList();
		if (ValidateUtil.isValid(paramList)) {
			String json = JsonUtil.toJson(paramList);
			activity.setActivityParamList(json);
		}
	}

	private void paramConversionToObj(Activity activity) throws Exception {
		String activityParamList = activity.getActivityParamList();
		if (ValidateUtil.isValid(activityParamList)) {
			List<param> params = JsonUtil.fromJsonArray(activityParamList, param.class);
			activity.setParamList(params);
		}
	}

	/**
	 * must contain the "activity Id"
	 * 
	 * @param activity
	 * @return
	 * @throws ParseException
	 */
	private BaseJob getActivityJob(Activity activity) throws ParseException {
		BaseJob baseJob = new BaseJob();
		baseJob.setJobGroup(BaseJob.ACTIVITY);
		baseJob.setJobName(activity.getActivityId().toString());
		String cron = DateUtils.getCron(activity.getActivityStartTime());
		baseJob.setCronExpression(cron);
		baseJob.setParam(activity.getActivityId());
		// choose Job class
		if (activity.getActicityTempleteType() == 0) {
			// 短信
			baseJob.setJobClass(SendSmsJob.class);
		} else {
			// 邮件
			baseJob.setJobClass(SendMailJob.class);
		}
		return baseJob;
	}

	@Override
	public Activity getActivityById(Long activityId) throws Exception {
		Activity activity = activityMapper.findActivityById(activityId);
		paramConversionToObj(activity);
		return activity;
	}

	@Override
	public List<Activity> listWithPagination(ActivityView view) {
		final List<Activity> lists = activityMapper.listWithPagination(view);
		return lists;
	}

	@Override
	public long maxSize(ActivityView view) {
		return activityMapper.maxSize(view);
	}

}
