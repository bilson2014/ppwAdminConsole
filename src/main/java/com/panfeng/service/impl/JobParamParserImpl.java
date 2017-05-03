package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;
import com.paipianwang.pat.facade.user.entity.PmsUser;
import com.paipianwang.pat.facade.user.service.PmsUserFacade;
import com.panfeng.flow.taskchain.EventType;
import com.panfeng.resource.model.Activity;
import com.panfeng.resource.model.Activity.param;
import com.panfeng.service.ActivityService;
import com.panfeng.service.JobParamParser;

@Service
public class JobParamParserImpl implements JobParamParser {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private PmsTeamFacade pmsTeamFacade = null;
	
	@Autowired
	private PmsUserFacade pmsUserFacade = null;
	
	@Autowired
	private PmsEmployeeFacade pmsEmployeeFacade = null;
	

	public Map<String, String[]> parser(Long activityId) throws Exception {
		Activity activity = activityService.getActivityById(activityId);
		Map<String, String[]> resMap = new HashMap<>();
		// 先解析人员，然后解析参数
		String relevantPersons = activity.getActitityRelevantPersons();
		EventType eventType = null;
		if (activity.getActicityTempleteType() == 0) {
			eventType = EventType.valueOf("SMS");
		} else {
			eventType = EventType.valueOf("MAIL");
		}
		if (ValidateUtil.isValid(relevantPersons)) {
			String[] split = relevantPersons.split(",|，");
			List<param> paramList = activity.getParamList();
			if (paramList == null)
				paramList = new ArrayList<>();
			if (split != null && split.length > 0) {
				for (String string : split) {
					Integer id = Integer.parseInt(string);
					switch (id) {
					case Activity.PERSONS_ALL_PROVIDER:
						//List<Team> allTeam = teamService.getAll();
						List<PmsTeam> allTeam = pmsTeamFacade.getAll();
						if (ValidateUtil.isValid(allTeam)) {
							for (PmsTeam team : allTeam) {
								String key = parseKey(team, eventType);
								if (ValidateUtil.isValid(key)) {
									String[] value = parseParam(paramList, team);
									resMap.put(key, value);
								}
							}
						}
						break;
					case Activity.PERSONS_ALL_USER:
						//List<User> allUser = userService.all();
						List<PmsUser> allUser = pmsUserFacade.all();
						if (ValidateUtil.isValid(allUser)) {
							for (PmsUser team : allUser) {
								String key = parseKey(team, eventType);
								if (ValidateUtil.isValid(key)) {
									String[] value = parseParam(paramList, team);
									resMap.put(key, value);
								}
							}
						}
						break;
					case Activity.PERSONS_ALL_EMPLOYEE:
						List<PmsEmployee> employeeList = pmsEmployeeFacade.findEmployeeList();
						if (ValidateUtil.isValid(employeeList)) {
							for (PmsEmployee employee : employeeList) {
								String key = parseKey(employee, eventType);
								if (ValidateUtil.isValid(key)) {
									String[] value = parseParam(paramList, employee);
									resMap.put(key, value);
								}
							}
						}
						break;
					}
				}
			}
		}
		return resMap;
	}

	/*private String[] parseParam(List<param> paramList, User user) {
		String[] values = new String[paramList.size()];
		for (int i = 0; i < paramList.size(); i++) {
			param p = paramList.get(i);
			String value = p.getValue();
			if (p.getType() == Activity.SYSTEMPARAM) {
				switch (value) {
				case "0":
					// 客户名称
					values[i] = user.getUserName();
					break;
				case "1":
					// 电话
					values[i] = user.getTelephone();
					break;
				}
			} else {
				values[i] = value;
			}
		}
		return values;
	}*/
	private String[] parseParam(List<param> paramList, PmsUser user) {
		String[] values = new String[paramList.size()];
		for (int i = 0; i < paramList.size(); i++) {
			param p = paramList.get(i);
			String value = p.getValue();
			if (p.getType() == Activity.SYSTEMPARAM) {
				switch (value) {
				case "0":
					// 客户名称
					values[i] = user.getUserName();
					break;
				case "1":
					// 电话
					values[i] = user.getTelephone();
					break;
				}
			} else {
				values[i] = value;
			}
		}
		return values;
	}

	private String[] parseParam(List<param> paramList, PmsEmployee employee) {
		String[] values = new String[paramList.size()];
		for (int i = 0; i < paramList.size(); i++) {
			param p = paramList.get(i);
			String value = p.getValue();
			if (p.getType() == Activity.SYSTEMPARAM) {
				switch (value) {
				case "0":
					// 客户名称
					values[i] = employee.getEmployeeRealName();
					break;
				case "1":
					// 电话
					values[i] = employee.getPhoneNumber();
					break;
				}
			} else {
				values[i] = value;
			}
		}
		return values;
	}

	private String[] parseParam(List<param> paramList, PmsTeam team) {
		String[] values = new String[paramList.size()];
		for (int i = 0; i < paramList.size(); i++) {
			param p = paramList.get(i);
			String value = p.getValue();
			if (p.getType() == Activity.SYSTEMPARAM) {
				switch (value) {
				case "0":
					// 客户名称
					values[i] = team.getTeamName();
					break;
				case "1":
					// 电话
					values[i] = team.getPhoneNumber();
					break;
				}
			} else {
				values[i] = value;
			}
		}
		return values;
	}

	/*private String parseKey(User user, EventType eventType) {
		String result = "";
		switch (eventType) {
		case MAIL:
			result = user.getEmail();
			break;
		case SMS:
			result = user.getTelephone();
			break;
		}
		return result;
	}*/
	private String parseKey(PmsUser user, EventType eventType) {
		String result = "";
		switch (eventType) {
		case MAIL:
			result = user.getEmail();
			break;
		case SMS:
			result = user.getTelephone();
			break;
		}
		return result;
	}

	private String parseKey(PmsTeam team, EventType eventType) {
		String result = "";
		switch (eventType) {
		case MAIL:
			result = team.getEmail();
			break;
		case SMS:
			result = team.getPhoneNumber();
			break;
		}
		return result;
	}

	private String parseKey(PmsEmployee employee, EventType eventType) {
		String result = "";
		switch (eventType) {
		case MAIL:
			result = employee.getEmail();
			break;
		case SMS:
			result = employee.getPhoneNumber();
			break;
		}
		return result;
	}
}
