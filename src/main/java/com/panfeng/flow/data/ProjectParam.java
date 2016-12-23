package com.panfeng.flow.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.flow.taskchain.EventType;
import com.panfeng.persist.IndentFlowMapper;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.Synergy;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.model.User;
import com.panfeng.service.ActivitiEngineService;
import com.panfeng.service.EmployeeService;
import com.panfeng.service.IndentProjectService;
import com.panfeng.service.SynergyService;
import com.panfeng.service.TeamService;
import com.panfeng.service.UserService;
import com.panfeng.util.ValidateUtil;

@Component
public class ProjectParam implements TemplateDateInterface<Map<String, String[]>, String> {

	@Autowired
	private IndentProjectService indentProjectService;

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private IndentFlowMapper indentFlowMapper;

	@Autowired
	private ActivitiEngineService activitiEngineService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SynergyService synergyService;

	final static String teamName = "teamName";
	final static String teamLinkMan = "teamLinkMan";
	final static String teamPhoneNumber = "teamPhoneNumber";

	final static String userName = "userName";
	final static String userLinkMan = "userLinkMan";
	final static String userPhoneNumber = "userPhoneNumber";

	final static String currentNode = "currentNode";
	final static String projectName = "projectName";
	final static String projectSerialNumber = "projectSerialNumber";

	final static String S_UserName = "S_UserName";
	final static String S_PhoneNumber = "S_PhoneNumber";

	@Override
	public List<String> optionalFields() {
		List<String> fields = new LinkedList<>();
		// 项目相关基础参数
		fields.add(teamName);
		fields.add(teamLinkMan);
		fields.add(teamPhoneNumber);

		fields.add(userName);
		fields.add(userLinkMan);
		fields.add(userPhoneNumber);

		fields.add(currentNode);
		fields.add(projectName);
		fields.add(projectSerialNumber);

		// 相关人员关联参数
		fields.add(S_UserName);
		fields.add(S_PhoneNumber);

		return fields;
	}

	@Override
	public Map<String, String[]> fillData(FillerParam fillerParam, String processId, EventType eventType) {
		return build(fillerParam, processId, eventType);
	}

	private Map<String, String[]> build(FillerParam fillerParam, String processId, EventType eventType) {
		// key --> personsKey,value -->param collection
		Map<String, String[]> result = new HashMap<>();

		// 缓存数据，每次循环再此获取数据。
		IndentFlow indentFlow = indentFlowMapper.findFlowByProcessId(processId);
		IndentProject indentProject = indentProjectService
				.getRedundantProject(new IndentProject(indentFlow.getIfIndentId()));
		Task task = activitiEngineService.getCurrentTask(processId);
		if (task != null)
			indentProject.setTask(ActivitiTask.TaskToActivitiTask(task));

		LinkedList<String> fields = fillerParam.getFields();
		LinkedList<String> relevantPersons = fillerParam.getRelevantPersons();

		User user = null;
		Team team = null;
		List<Employee> providerManager = null;
		List<Employee> manager = null;

		if (ValidateUtil.isValid(fields) && ValidateUtil.isValid(relevantPersons)) {
			// 识别人员 --》构造参数
			String key = null;
			String[] value = null;
			for (String string : relevantPersons) {
				switch (string) {
				case GlobalConstant.ROLE_PROVIDER:
					// 确定参数
					if (team == null)
						team = teamService.findTeamById(indentProject.getTeamId());
					key = parseKey(team, eventType);
					value = new String[fields.size()];
					for (int i = 0; i < fields.size(); i++) {
						String field = fields.get(i);
						if (field.indexOf("S_") > -1) {
							String otherParam = otherParam(field, indentProject, GlobalConstant.ROLE_PROVIDER);
							value[i] = otherParam;
						} else {
							String basicParam = parseBasicParam(field, indentProject);
							value[i] = basicParam;
						}
					}
					result.put(key, value);
					break;
				case GlobalConstant.ROLE_CUSTOMER:
					if (user == null)
						user = userService.findUserById(indentProject.getCustomerId());
					key = parseKey(user, eventType);
					value = new String[fields.size()];
					for (int i = 0; i < fields.size(); i++) {
						String field = fields.get(i);
						if (field.indexOf("S_") > -1) {
							String otherParam = otherParam(field, indentProject, GlobalConstant.ROLE_CUSTOMER);
							value[i] = otherParam;
						} else {
							String basicParam = parseBasicParam(field, indentProject);
							value[i] = basicParam;
						}

					}
					result.put(key, value);
					break;
				case GlobalConstant.ROLE_PROVIDERMANAGER:
					if (providerManager == null)
						providerManager = employeeService.findEmployeeByRoleid(4L);
					// 构造所有供应商管家信息
					if (ValidateUtil.isValid(providerManager)) {
						fillParam(fields, providerManager, eventType, indentProject, result);
					}
					break;
				case GlobalConstant.ROLE_MANAGER:
					if (manager == null)
						manager = getManager(indentProject);

					// 构造所有供应商管家信息
					if (ValidateUtil.isValid(manager)) {
						fillParam(fields, manager, eventType, indentProject, result);
					}
					break;
				}
				key = null;
				value = null;
			}
		}
		return result;
	}

	private void fillParam(LinkedList<String> fields, List<Employee> manager, EventType eventType,
			IndentProject indentProject, Map<String, String[]> result) {
		for (Employee employee : manager) {
			String key = parseKey(employee, eventType);
			String[] value = new String[fields.size()];
			for (int i = 0; i < fields.size(); i++) {
				String field = fields.get(i);
				if (field.indexOf("S_") > -1) {
					String otherParam = otherParam(field, employee);
					value[i] = otherParam;
				} else {
					String basicParam = parseBasicParam(field, indentProject);
					value[i] = basicParam;
				}
			}
			result.put(key, value);
		}
	}

	private List<Employee> getManager(IndentProject indentProject) {
		List<Long> ids = new ArrayList<>();
		Long userId = indentProject.getUserId();
		ids.add(userId); // 添加主负责人
		Map<Long, Synergy> synergys = synergyService.findSynergyMapByProjectId(indentProject.getId());
		Collection<Synergy> collectionSynergys = synergys.values();
		for (Synergy synergy : collectionSynergys) {
			ids.add(synergy.getUserId());// 添加主协同人
		}
		return employeeService.findEmployeeByIds(ids.toArray(new Long[ids.size()]));
	}

	private String parseBasicParam(String field, IndentProject indentProject) {
		String result = "";
		// 基本参数
		switch (field) {
		case teamName:
			result = indentProject.getTeamName();
			break;
		case teamLinkMan:
			result = indentProject.getTeamContact();
			break;
		case teamPhoneNumber:
			result = indentProject.getTeamPhone();
			break;
		case userName:
			result = indentProject.getUserName();
			break;
		case userLinkMan:
			result = indentProject.getUserContact();
			break;
		case userPhoneNumber:
			result = indentProject.getUserPhone();
			break;
		case currentNode:
			ActivitiTask task = indentProject.getTask();
			if (task != null)
				result = "【" + task.getName() + "】";
			else
				result = "";

			break;
		case projectName:
			result = "【" + indentProject.getProjectName() + "】";
			break;
		case projectSerialNumber:
			result = indentProject.getSerial();
			break;
		}
		return result;
	}

	private String otherParam(String field, Employee employee) {
		String result = "";
		switch (field) {
		case S_UserName:
			result = employee.getEmployeeRealName();
			break;
		case S_PhoneNumber:
			result = employee.getPhoneNumber();
			break;
		}
		return result;
	}

	private String otherParam(String field, IndentProject indentProject, String role) {
		String result = "";
		if (role.equals(GlobalConstant.ROLE_CUSTOMER)) {
			switch (field) {
			case S_UserName:
				result = indentProject.getUserName();
				break;
			case S_PhoneNumber:
				result = indentProject.getUserPhone();
				break;
			}
		} else if (role.equals(GlobalConstant.ROLE_PROVIDER)) {
			switch (field) {
			case S_UserName:
				result = indentProject.getTeamName();
				break;
			case S_PhoneNumber:
				result = indentProject.getTeamPhone();
				break;
			}
		}
		return result;
	}
	// private String otherParam(String field, User user) {
	// String result = "";
	// switch (field) {
	// case S_UserName:
	// result = user.getUserName();
	// break;
	// case S_PhoneNumber:
	// result = user.getTelephone();
	// break;
	// }
	// return result;
	// }
	//
	// private String otherParam(String field, Team team) {
	// String result = "";
	// switch (field) {
	// case S_UserName:
	// result = team.getTeamName();
	// break;
	// case S_PhoneNumber:
	// result = team.getPhoneNumber();
	// break;
	// }
	// return result;
	// }

	private String parseKey(Employee employee, EventType eventType) {
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

	private String parseKey(User user, EventType eventType) {
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

	private String parseKey(Team team, EventType eventType) {
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

	@Override
	public Map<String, String> personnel() {
		Map<String, String> personnel = new HashMap<String, String>();
		personnel.put(GlobalConstant.ROLE_PROVIDER, "供应商");
		personnel.put(GlobalConstant.ROLE_CUSTOMER, "客户");
		personnel.put(GlobalConstant.ROLE_PROVIDERMANAGER, "供应商管家");
		personnel.put(GlobalConstant.ROLE_MANAGER, "项目相关负责人");
		return personnel;
	}

}
