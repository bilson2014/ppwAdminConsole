package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.domain.BaseMsg;
import com.panfeng.persist.ActivityMapper;
import com.panfeng.resource.model.Activity;
import com.panfeng.resource.model.Activity.param;
import com.panfeng.service.ActivityService;
import com.panfeng.util.JsonUtil;
import com.panfeng.util.ValidateUtil;

@Service
public class ActvityServiceImpl implements ActivityService {
	@Autowired
	private ActivityMapper activityMapper;

	@Override
	public List<Activity> getAll() {
		return activityMapper.findAll();
	}

	@Override
	public BaseMsg saveActivity(Activity activity) {
		paramConvesionToString(activity);
		Long res = activityMapper.save(activity);
		if (res != null && res > 0) {
			return new BaseMsg(BaseMsg.NORMAL, "", "新增成功！");
		} else {
			return new BaseMsg(BaseMsg.ERROR, "", "新增失败！");
		}
	}

	@Override
	public BaseMsg updateActivity(Activity activity) {
		paramConvesionToString(activity);
		Long res = activityMapper.update(activity);
		if (res != null && res > 0) {
			return new BaseMsg(BaseMsg.NORMAL, "", "修改成功！");
		} else {
			return new BaseMsg(BaseMsg.ERROR, "", "修改失败！");
		}
	}

	@Override
	public BaseMsg deleteActivities(Long[] ids) {
		Long res = activityMapper.delete(ids);
		if (res != null && res > 0) {
			return new BaseMsg(BaseMsg.NORMAL, "", "删除成功！");
		} else {
			return new BaseMsg(BaseMsg.ERROR, "", "删除失败！");
		}
	}

	public void parseParam(Activity activity) throws Exception {
		paramConvesionToObj(activity);
		List<param> paramList = activity.getParamList();
		if (ValidateUtil.isValid(paramList)) {
			for (param param : paramList) {
				if (param.getType() == Activity.SYSTEMPARAM) {
					// 识别参数
//					<option value="0">供应商名称</option>
//		            <option value="1">客户名称</option>
					
					
				} else if (param.getType() == Activity.CUSTOMPARAM) {
					
				}
			}
		}
	}

	private void paramConvesionToString(Activity activity) {
		List<param> paramList = activity.getParamList();
		if (ValidateUtil.isValid(paramList)) {
			String json = JsonUtil.toJson(paramList);
			activity.setActivityParamList(json);
		}
	}

	private void paramConvesionToObj(Activity activity) throws Exception {
		String activityParamList = activity.getActivityParamList();
		if (ValidateUtil.isValid(activityParamList)) {
			List<param> params = JsonUtil.fromJsonArray(activityParamList, param.class);
			activity.setParamList(params);
		}
	}

}
