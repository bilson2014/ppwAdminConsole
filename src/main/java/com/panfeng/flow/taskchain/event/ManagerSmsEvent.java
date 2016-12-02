package com.panfeng.flow.taskchain.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.panfeng.domain.SessionInfo;
import com.panfeng.flow.taskchain.EventBase;
import com.panfeng.flow.taskchain.TaskStatus;
import com.panfeng.mq.service.SmsMQService;
import com.panfeng.persist.IndentFlowMapper;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.NodesEvent;
import com.panfeng.resource.model.Synergy;
import com.panfeng.service.ActivitiEngineService;
import com.panfeng.service.EmployeeService;
import com.panfeng.service.IndentProjectService;
import com.panfeng.service.SynergyService;
import com.panfeng.util.ValidateUtil;

@Component
public class ManagerSmsEvent extends EventBase {

	@Autowired
	private IndentProjectService indentProjectService;

	@Autowired
	private IndentFlowMapper indentFlowMapper;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SynergyService synergyService;

	@Autowired
	private SmsMQService smsMQService;

	@Autowired
	private ActivitiEngineService activitiEngineService;

	@Override
	public TaskStatus checkStatus() {
		return TaskStatus.Finish;
	}

	public void execute(SessionInfo sessionInfo, String processId) {
		System.out.println("发送邮件中");
	}

	@Override
	public void closeTask() {
		System.out.println("关闭了右键发送程序");
	}

	@Override
	public <BaseMsg> BaseMsg getResult() {
		return null;
	}

	@Override
	public <Mail> Mail getInfo() {
		return null;
	}

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
	public void execute(NodesEvent autoEvent, SessionInfo sessionInfo, String processId) {
		IndentFlow indentFlow = indentFlowMapper.findFlowByProcessId(processId);
		IndentProject indentProject = indentProjectService
				.getRedundantProject(new IndentProject(indentFlow.getIfIndentId()));
		indentFlowMapper.findFlowByProcessId(processId);
		List<Long> ids = new ArrayList<>();
		Long userId = indentProject.getUserId();
		ids.add(userId); // 添加主负责人

		Map<Long, Synergy> synergys = synergyService.findSynergyMapByProjectId(indentProject.getId());
		Collection<Synergy> collectionSynergys = synergys.values();
		for (Synergy synergy : collectionSynergys) {
			ids.add(synergy.getUserId());// 添加主协同人
		}
		List<Employee> es = employeeService.findEmployeeByIds(ids.toArray(new Long[ids.size()]));
		Task task = activitiEngineService.getCurrentTask(processId);
		if (ValidateUtil.isValid(es)) {
			for (Employee employee : es) {
				String[] param = new String[3];
				param[0] = employee.getEmployeeRealName();
				param[1] = "《" + indentProject.getProjectName() + "》";
				param[2] = "【" + task.getName() + "】";
				//smsMQService.sendMessage("134601", employee.getPhoneNumber(), param);
			}
		}
	}
}
