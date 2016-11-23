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
public class ProjectParam implements TemplateDateInterface<List<String>, String> {

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
	final static String teamEmail = "teamEmail";
	final static String userName = "userName";
	final static String userEmail = "userEmail";
	final static String myName = "myName";
	final static String currentNode = "currentNode";
	final static String userPhoneNumber = "userPhoneNumber";
	final static String teamPhoneNumber = "teamPhoneNumber";
	final static String projectName = "projectName";

	@Override
	public List<String> optionalFields() {
		List<String> fields = new LinkedList<>();
		fields.add(teamName);
		fields.add(teamEmail);
		fields.add(userName);
		fields.add(userEmail);
		fields.add(myName);
		fields.add(currentNode);
		fields.add(userPhoneNumber);
		fields.add(teamPhoneNumber);
		fields.add(projectName);
		return fields;
	}

	@Override
	public List<String> fillData(List<String> list, String processId, EventType eventType) {
		return build(list, processId, eventType);
	}

	private List<String> build(List<String> list, String processId, EventType eventType) {
		List<String> values = new LinkedList<>();
		if (ValidateUtil.isValid(list)) {
			IndentFlow indentFlow = indentFlowMapper.findFlowByProcessId(processId);
			IndentProject indentProject = indentProjectService
					.getRedundantProject(new IndentProject(indentFlow.getIfIndentId()));

			User user = userService.findUserById(indentProject.getCustomerId());
			Team team = teamService.findTeamById(indentProject.getTeamId());
			for (String field : list) {
				String res = "";
				// 基本属性
				switch (field) {
				case teamName:
					res = indentProject.getTeamName();
					break;
				case currentNode:
					Task task = activitiEngineService.getCurrentTask(processId);
					res = "【" + task.getName() + "】";
					break;
				case myName:
					res = indentProject.getEmployeeRealName();
					break;
				case teamEmail:
					res = team.getEmail();
					break;
				case userEmail:
					res = user.getEmail();
					break;
				case userName:
					res = indentProject.getUserName();
					break;
				case teamPhoneNumber:
					res = indentProject.getTeamPhone();
					break;
				case userPhoneNumber:
					res = indentProject.getUserPhone();
					break;
				case projectName:
					res = "《" + indentProject.getProjectName() + "》";
					break;
				// 相关人员属性
				case GlobalConstant.ROLE_PROVIDER:
					switch (eventType) {
					case MAIL:
						res = team.getEmail();
						break;
					case SMS:
						res = indentProject.getTeamPhone();
						break;
					}
					break;
				case GlobalConstant.ROLE_CUSTOMER:
					switch (eventType) {
					case MAIL:
						res = user.getEmail();
						break;
					case SMS:
						res = indentProject.getUserPhone();
						break;
					}
					break;
				case GlobalConstant.ROLE_MANAGER:
					List<Long> ids = new ArrayList<>();
					Long userId = indentProject.getUserId();
					ids.add(userId); // 添加主负责人
					
					Map<Long, Synergy> synergys = synergyService.findSynergyMapByProjectId(indentProject.getId());
					Collection<Synergy> collectionSynergys = synergys.values();
					for (Synergy synergy : collectionSynergys) {
						ids.add(synergy.getUserId());// 添加主协同人
					}
					List<Employee> es = employeeService.findEmployeeByIds(ids.toArray(new Long[ids.size()]));
					res = listJoin(es,eventType);
					break;
				case GlobalConstant.ROLE_PROVIDERMANAGER:
					// 供应商管家角色ID 4
					List<Employee> employees = employeeService.findEmployeeByRoleid(4L);
					res = listJoin(employees,eventType);
					break;
				}
				values.add(res);
			}
		}
		return values;
	}
	private String listJoin(List<Employee> es,EventType eventType){
		String res="";
		if (ValidateUtil.isValid(es)) {
			switch (eventType) {
			case MAIL:
				List<String> mails = new ArrayList<>();
				for (Employee employee : es) {
					mails.add(employee.getEmail());
				}
				res = String.join(",", mails);
				break;
			case SMS:
				List<String> sms = new ArrayList<>();
				for (Employee employee : es) {
					sms.add(employee.getPhoneNumber());
				}
				res = String.join(",", sms);
				break;
			}
		}
		return res;
	}
	@Override
	public Map<String, String> personnel() {
		Map<String, String> personnel = new HashMap<String, String>();
		personnel.put(GlobalConstant.ROLE_PROVIDER, "供应商");
		personnel.put(GlobalConstant.ROLE_CUSTOMER, "客户");
		personnel.put(GlobalConstant.ROLE_PROVIDERMANAGER, "供应商管家");
		// personnel.put(GlobalConstant.ROLE_MANAGER, "项目相关负责人");
		return personnel;
	}

}
