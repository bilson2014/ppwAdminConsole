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

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.panfeng.flow.taskchain.EventType;
import com.panfeng.persist.IndentFlowMapper;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.Synergy;
import com.panfeng.service.ActivitiEngineService;
import com.panfeng.service.IndentProjectService;
import com.panfeng.service.SynergyService;

/**
 * 流程任务参数解析类
 * 
 * @author wang
 *
 *         现在反回来看看，只有老天才知道。这代码特么是啥意思
 */
@Component
public class ProjectParam implements TemplateDateInterface<Map<String, String[]>, String> {

	@Autowired
	private IndentProjectService indentProjectService;

	@Autowired
	private IndentFlowMapper indentFlowMapper;

	@Autowired
	private ActivitiEngineService activitiEngineService;

	@Autowired
	private final PmsEmployeeFacade pmsEmployeeFacade = null;

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

		List<PmsEmployee> providerManager = null;
		List<PmsEmployee> manager = null;

		if (ValidateUtil.isValid(fields) && ValidateUtil.isValid(relevantPersons)) {
			// 识别人员 --》构造参数
			String key = null;
			String[] value = null;
			for (String string : relevantPersons) {
				switch (string) {
				case PmsConstant.ROLE_PROVIDER:
					// 确定参数
					key = indentProject.getTeamPhone();
					value = new String[fields.size()];
					for (int i = 0; i < fields.size(); i++) {
						String field = fields.get(i);
						if (field.indexOf("S_") > -1) {
							String otherParam = otherParam(field, indentProject, PmsConstant.ROLE_PROVIDER);
							value[i] = otherParam;
						} else {
							String basicParam = parseBasicParam(field, indentProject);
							value[i] = basicParam;
						}
					}
					result.put(key, value);
					break;
				case PmsConstant.ROLE_CUSTOMER:
					key = indentProject.getUserPhone();
					value = new String[fields.size()];
					for (int i = 0; i < fields.size(); i++) {
						String field = fields.get(i);
						if (field.indexOf("S_") > -1) {
							String otherParam = otherParam(field, indentProject, PmsConstant.ROLE_CUSTOMER);
							value[i] = otherParam;
						} else {
							String basicParam = parseBasicParam(field, indentProject);
							value[i] = basicParam;
						}

					}
					result.put(key, value);
					break;
				case PmsConstant.ROLE_PROVIDERMANAGER:
					if (providerManager == null)
						providerManager = pmsEmployeeFacade.findEmployeesByRoleId(4l);
					// 构造所有供应商管家信息
					if (ValidateUtil.isValid(providerManager)) {
						fillParam(fields, providerManager, eventType, indentProject, result);
					}
					break;
				case PmsConstant.ROLE_MANAGER:
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

	/**
	 * 填充参数
	 * 
	 * @param fields
	 * @param manager
	 * @param eventType
	 * @param indentProject
	 * @param result
	 */
	private void fillParam(LinkedList<String> fields, List<PmsEmployee> manager, EventType eventType,
			IndentProject indentProject, Map<String, String[]> result) {
		for (PmsEmployee employee : manager) {
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

	private List<PmsEmployee> getManager(IndentProject indentProject) {
		List<Long> ids = new ArrayList<>();
		Long userId = indentProject.getUserId();
		ids.add(userId); // 添加主负责人
		Map<Long, Synergy> synergys = synergyService.findSynergyMapByProjectId(indentProject.getId());
		Collection<Synergy> collectionSynergys = synergys.values();
		for (Synergy synergy : collectionSynergys) {
			ids.add(synergy.getUserId());// 添加主协同人
		}
		return pmsEmployeeFacade.findEmployeeByIds(ids.toArray(new Long[ids.size()]));
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

	private String otherParam(String field, PmsEmployee employee) {
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
		if (role.equals(PmsConstant.ROLE_CUSTOMER)) {
			switch (field) {
			case S_UserName:
				result = indentProject.getUserName();
				break;
			case S_PhoneNumber:
				result = indentProject.getUserPhone();
				break;
			}
		} else if (role.equals(PmsConstant.ROLE_PROVIDER)) {
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

	@Override
	public Map<String, String> personnel() {
		Map<String, String> personnel = new HashMap<String, String>();
		personnel.put(PmsConstant.ROLE_PROVIDER, "供应商");
		personnel.put(PmsConstant.ROLE_CUSTOMER, "客户");
		personnel.put(PmsConstant.ROLE_PROVIDERMANAGER, "供应商管家");
		personnel.put(PmsConstant.ROLE_MANAGER, "项目相关负责人");
		return personnel;
	}

}
