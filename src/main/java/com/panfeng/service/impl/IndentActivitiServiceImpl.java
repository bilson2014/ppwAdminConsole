package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.SessionInfo;
import com.panfeng.flow.taskchain.TaskChainHandler;
import com.panfeng.mq.service.SmsMQService;
import com.panfeng.persist.FlowDateMapper;
import com.panfeng.persist.IndentFlowMapper;
import com.panfeng.persist.IndentProjectMapper;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.model.FlowDate;
import com.panfeng.resource.model.FlowNode;
import com.panfeng.resource.model.FlowTemplate;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.Synergy;
import com.panfeng.service.ActivitiEngineService;
import com.panfeng.service.EmployeeService;
import com.panfeng.service.IndentActivitiService;
import com.panfeng.service.IndentCommentService;
import com.panfeng.service.IndentProjectService;
import com.panfeng.service.SynergyService;
import com.panfeng.util.ValidateUtil;

@Service
public class IndentActivitiServiceImpl implements IndentActivitiService {

	@Autowired
	private ActivitiEngineService activitiEngineService;
	@Autowired
	private IndentFlowMapper flowMapper;
	@Autowired
	private FlowDateMapper flowDateMapper;

	private static String processDefinitionKey = "ProjectFlow";
	@Autowired
	private IndentCommentService indentCommentService;
	@Autowired
	private IndentProjectMapper indentProjectMapper;
	@Autowired
	private IndentProjectService indentProjectService;
	@Autowired
	SynergyService synergyService;
	@Autowired
	final EmployeeService employeeService = null;
	@Autowired
	SmsMQService smsMQService;
	@Autowired
	TaskChainHandler taskChainHandler;

	@Override
	public ActivitiTask getCurrentTask(IndentProject indentProject) {
		// new api
		Task task = activitiEngineService.getCurrentTask(getIndentCurrentFlowId(indentProject));
		if (task == null) {
			// 请求流程状态
			boolean isSuspended = activitiEngineService.isSuspended(getIndentCurrentFlowId(indentProject));
			ActivitiTask activitiTask = null;
			if (isSuspended) {
				List<ActivitiTask> list = getAfterTask(indentProject);
				activitiTask = list.get(list.size() - 1);
				activitiTask.setSuspended(true);
			} else {
				activitiTask = new ActivitiTask();
				activitiTask.setName("任务不存在");
			}
			return activitiTask;
		}
		return ActivitiTask.TaskToActivitiTask(task);
	}

	@Override
	public List<IndentProject> fullCurrentTask(final List<IndentProject> ips) {
		if (!ValidateUtil.isValid(ips))
			return null;
		List<String> tIds = new ArrayList<>();
		for (int i = 0; i < ips.size(); i++) {
			IndentProject ip = ips.get(i);
			if (ip.getMasterFlowId() != null)
				tIds.add(ip.getMasterFlowId().toString());
		}
		List<Task> tasks = activitiEngineService.getCurrentTask(tIds);
		if (tasks != null) {
			for (int i = 0; i < tasks.size(); i++) {
				ActivitiTask at = ActivitiTask.TaskToActivitiTask(tasks.get(i));
				for (int j = 0; j < ips.size(); j++) {
					String taskKey = at.getProcessInstanceId();
					IndentProject ip = ips.get(j);
					if (taskKey.equals(ip.getMasterFlowId().toString())) {
						ip.setTask(at);
						break;
					}
				}
			}
		}
		return ips;
	}

	@Override
	public List<ActivitiTask> getAfterTask(IndentProject indentProject) {
		List<HistoricTaskInstance> listTaskInstances = activitiEngineService
				.getAfterTask(getIndentCurrentFlowId(indentProject));

		List<ActivitiTask> list = new ArrayList<>();
		for (HistoricTaskInstance historicTaskInstance : listTaskInstances) {
			list.add(ActivitiTask.TaskToActivitiTask(historicTaskInstance));
		}
		return list;
	}

	@Override
	public HistoricProcessInstance getHistoryProcess(IndentProject indentProject) {
		HistoricProcessInstance historicProcessInstance = activitiEngineService
				.getHistoryProcess(getIndentCurrentFlowId(indentProject));
		return historicProcessInstance;
	}

	@Override
	public List<ActivitiTask> getHistoryProcessTask(IndentProject indentProject) {
		List<HistoricTaskInstance> listTaskInstances = activitiEngineService
				.getHistoryProcessTask(getIndentCurrentFlowId(indentProject));
		List<ActivitiTask> list = new ArrayList<>();
		for (HistoricTaskInstance historicTaskInstance : listTaskInstances) {
			list.add(ActivitiTask.TaskToActivitiTask(historicTaskInstance));
		}
		return list;
	}

	@Override
	public List<HistoricTaskInstance> getHistoryProcessTask_O(IndentProject indentProject) {
		return activitiEngineService.getHistoryProcessTask(getIndentCurrentFlowId(indentProject));
	}

	public BaseMsg verifyIntegrity(IndentProject indentProject) {
		// 完整性认证
		ActivitiTask activitiTask = getCurrentTask(indentProject);
		indentProject.setTask(activitiTask);
		BaseMsg verifyRes = indentProjectService.verifyIntegrity(indentProject);
		return verifyRes;
	}

	@Override
	public synchronized String completeTask(IndentProject indentProject, String processId, SessionInfo sessionInfo) {
		Task activitiTask = activitiEngineService.getCurrentTask(processId);
		boolean res = false;
		indentCommentService.createSystemMsg("完成了\"" + activitiTask.getName() + "\"任务", indentProject);
		res = activitiEngineService.completeTask(getIndentCurrentFlowId(indentProject));
		// 检测任务是否已经完成

		boolean isFinish = activitiEngineService.isFinish(getIndentCurrentFlowId(indentProject));
		if (isFinish) {
			completeProcess(indentProject, sessionInfo, activitiTask.getProcessInstanceId(),
					activitiTask.getProcessDefinitionId());
		}
		return res + "";
	}

	public synchronized BaseMsg completeTask_2(IndentProject indentProject, String processId, SessionInfo sessionInfo) {
		Task activitiTask = activitiEngineService.getCurrentTask(processId);
		BaseMsg res = null;
		indentCommentService.createSystemMsg("完成了\"" + activitiTask.getName() + "\"任务", indentProject);
		res = activitiEngineService.completeTask_2(sessionInfo, processId);
		// 检测任务是否已经完成

		boolean isFinish = activitiEngineService.isFinish(processId);
		if (isFinish) {
			completeProcess(indentProject, sessionInfo, activitiTask.getProcessInstanceId(),
					activitiTask.getProcessDefinitionId());
		}
		return res;
	}

	/**
	 * 完成一个流程实例
	 * 
	 * @param indentProject
	 */
	private void completeProcess(IndentProject indentProject, SessionInfo sessionInfo, String processId,
			String processDefinitionId) {
		// 更新项目状态
		indentProjectMapper.updateState(indentProject.getId(), IndentProject.PROJECT_FINISH, null);
		// ready to send sms
		indentProject = indentProjectService.getRedundantProject(indentProject);
		List<Long> ids = new ArrayList<>();
		Long userId = indentProject.getUserId();
		ids.add(userId); // 添加主负责人

		Map<Long, Synergy> synergys = synergyService.findSynergyMapByProjectId(indentProject.getId());
		Collection<Synergy> collectionSynergys = synergys.values();
		for (Synergy synergy : collectionSynergys) {
			ids.add(synergy.getUserId());// 添加主协同人
		}
		List<Employee> es = employeeService.findEmployeeByIds(ids.toArray(new Long[ids.size()]));
		if (ValidateUtil.isValid(es)) {
			for (Employee employee : es) {
				String[] param = new String[2];
				param[0] = employee.getEmployeeRealName();
				param[1] = "《" + indentProject.getProjectName() + "》";
				// smsMQService.sendMessage("134606", employee.getPhoneNumber(),
				// param);
			}
		}
		/////////////////////////////////// 给客户发送信息。=、、、、、、、、、、、、、、、、、、、、
		String[] param = new String[2];
		param[0] = indentProject.getUserName();
		param[1] = "《" + indentProject.getProjectName() + "》";
		// smsMQService.sendMessage("134606", indentProject.getUserPhone(),
		// param);
		// execute automatic events
		FlowTemplate template = activitiEngineService.getTemplate(processDefinitionId);
		FlowNode flowNode = template.getFlowNodes().get(0);
		if (flowNode != null) {
			taskChainHandler.execute(flowNode.getTaskChainId(), sessionInfo, processId);
		}
	}

	@Override
	public boolean startProcess(IndentProject indentProject, SessionInfo sessionInfo) {
		// 禁用所有流程
		disableAllFlow(indentProject);
		// 启动流程
		String flowId = activitiEngineService.startProcess(processDefinitionKey, String.valueOf(indentProject.getId()));
		// 设置关联
		IndentFlow indentFlow = new IndentFlow();
		indentFlow.setIfFlowId(flowId);
		indentFlow.setIfIndentId(indentProject.getId());
		indentFlow.setIfState(IndentFlow.FLOWENABLE);
		long l = flowMapper.save(indentFlow);
		// 填充默认时间
		if (indentProject.getTime() == null) {
			Map<String, String> map = new HashMap<String, String>();
			List<ActivitiTask> activitiTasks = getNodes(indentProject);
			for (ActivitiTask activitiTask : activitiTasks) {
				if (activitiTask != null)
					map.put(activitiTask.getTaskDefinitionKey(), "");
			}
			indentProject.setTime(map);
		}

		// 获取动态节点
		List<ActivitiTask> nodes = getNodes(indentProject);
		// 填充流程时间节点
		List<FlowDate> dates = IndentFlow.createFlowDates(flowId, indentProject, nodes);
		for (FlowDate flowDate : dates) {
			flowDateMapper.save(flowDate);
		}
		if (l > 0) {
			indentCommentService.createSystemMsg("创建了 " + indentProject.getProjectName() + "项目", indentProject);
			activitiEngineService.executeStartEvent(sessionInfo, flowId);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean suspendProcess(IndentProject indentProject, boolean isBack) {
		boolean isSuspended = activitiEngineService.isSuspended(getIndentCurrentFlowId(indentProject));
		if (!isSuspended) {
			boolean res = activitiEngineService.suspendProcess(getIndentCurrentFlowId(indentProject));
			indentCommentService.createSystemMsg(
					" 暂停了 " + indentProject.getProjectName() + "项目，原因：" + indentProject.getDescription(),
					indentProject);
			if (isBack)
				indentProjectMapper.updateStateBack(indentProject.getId(), IndentProject.PROJECT_SUSPEND,
						indentProject.getDescription());
			else
				indentProjectMapper.updateState(indentProject.getId(), IndentProject.PROJECT_SUSPEND,
						indentProject.getDescription());
			return res;
		}
		return true;
	}

	@Override
	public boolean resumeProcess(IndentProject indentProject, boolean isBack) {
		boolean res = activitiEngineService.resumeProcess(getIndentCurrentFlowId(indentProject));
		indentCommentService.createSystemMsg(" 恢复了 " + indentProject.getProjectName() + "项目", indentProject);
		if (isBack)
			indentProjectMapper.updateStateBack(indentProject.getId(), IndentProject.PROJECT_NORMAL,
					indentProject.getDescription());
		else
			indentProjectMapper.updateState(indentProject.getId(), IndentProject.PROJECT_NORMAL,
					indentProject.getDescription());
		return res;
	}

	/**
	 * 删除当前活动的项目
	 */
	@Override
	public boolean removeProcess(IndentProject indentProject) {
		IndentFlow indentFlow = flowMapper.findFlowByIndent(indentProject);
		flowMapper.deleteByflowId(indentProject.getId(), indentFlow.getIfFlowId());
		return activitiEngineService.removeProcess(indentFlow.getIfFlowId());
	}

	@Override
	public boolean jumpTask(IndentProject indentProject, String activityId) {
		activitiEngineService.jumpTask(activityId, getIndentCurrentFlowId(indentProject));
		String taskName = "";
		List<ActivitiTask> list = getNodes(indentProject);
		for (ActivitiTask activitiTask : list) {
			if (activityId.equals(activitiTask.getTaskDefinitionKey())) {
				taskName = activitiTask.getName();
				break;
			}
		}
		indentCommentService.createSystemMsg(" 将任务节点跳转到 -->" + taskName + "阶段", indentProject);
		return true;
	}

	public String getIndentCurrentFlowId(IndentProject indentProject) {
		List<IndentFlow> list = flowMapper.findFlowByIndentId(indentProject);
		if (list != null) {
			for (IndentFlow indentFlow : list) {
				if (indentFlow.getIfState().equals(IndentFlow.FLOWENABLE))
					return indentFlow.getIfFlowId();
			}
		}
		return "";
	}

	private void disableAllFlow(IndentProject indentProject) {
		List<IndentFlow> list = flowMapper.findFlowByIndentId(indentProject);
		if (list != null) {
			for (IndentFlow indentFlow : list) {
				if (indentFlow.getIfState().equals(IndentFlow.FLOWENABLE)) {
					indentFlow.setIfState(IndentFlow.FLOWDISABLE);
					flowMapper.update(indentFlow);
				}
			}
		}
	}

	@Override
	public List<IndentFlow> getIndentFlows(IndentProject indentProject) {
		return flowMapper.findFlowByIndentId(indentProject);
	}

	@Override
	public List<ActivitiTask> getNodes(IndentProject indentProject) {
		List<ActivitiTask> listactivitiTask = new ArrayList<>();
		List<ActivityImpl> listaActivityImpls = activitiEngineService.getNodes(getIndentCurrentFlowId(indentProject));
		if (listaActivityImpls == null || listaActivityImpls.size() < 1)
			return listactivitiTask;

		HistoricTaskInstance task = activitiEngineService.getDoneTaskInstance(getIndentCurrentFlowId(indentProject));
		String processInstanceId = task.getProcessInstanceId();

		// 查询预计时间
		List<FlowDate> listDates = flowDateMapper.findFlowDateByFlowId(processInstanceId);
		// 查询实际执行时间
		List<ActivitiTask> activitiTasks = getAfterTask(indentProject);

		ActivitiTask activitiTask;
		FlowDate flowDate;
		ActivitiTask nextTask;
		// 为节点添加时间
		for (ActivityImpl activityImpl : listaActivityImpls) {
			Iterator<FlowDate> iteratorFlow = listDates.iterator();
			Iterator<ActivitiTask> iteratorTask = activitiTasks.iterator();

			String name = activityImpl.getProperty("name") + "";
			if (name.equals("Start") || name.equals("End"))
				continue;
			activitiTask = new ActivitiTask();
			activitiTask.setTaskDefinitionKey(activityImpl.getId());
			activitiTask.setName(name);
			TaskDefinition td = (TaskDefinition) activityImpl.getProperty("taskDefinition");
			// 兼容性调整-----------
			String property = activityImpl.getProperty("documentation").toString();
			try {
				Gson gson = new Gson();
				FlowNode flowNode = gson.fromJson(property, FlowNode.class);
				property = flowNode.getDescription();
			} catch (JsonSyntaxException e) {
				// e.printStackTrace();
			}
			// ----------------------
			activitiTask.setDescription(property);
			if (td != null && td.getAssigneeExpression() != null) {
				activitiTask.setOwner(td.getAssigneeExpression().getExpressionText());
			}
			// 填充预计时间
			while (iteratorFlow.hasNext()) {
				flowDate = iteratorFlow.next();
				if (flowDate.getFdTaskId().equals(activityImpl.getId())) {
					if (flowDate.getFdStartTime() == null || flowDate.getFdStartTime().equals(IndentFlow.defaultDate)) {
						flowDate.setFdStartTime("");
					}
					activitiTask.setScheduledTime(flowDate);
					break;
				}
			}
			// 填充实际时间
			while (iteratorTask.hasNext()) {
				nextTask = iteratorTask.next();
				if (nextTask.getTaskDefinitionKey().equals(activityImpl.getId())) {
					activitiTask.setCreateTime(nextTask.getCreateTime());
				}
			}
			listactivitiTask.add(activitiTask);
		}
		return listactivitiTask;
	}

	@Override
	public List<String> getBpmnNodes(IndentProject indentProject) {
		List<String> listactivitiTask = new ArrayList<>();
		List<ActivityImpl> listaActivityImpls = activitiEngineService.getNodes(getIndentCurrentFlowId(indentProject));
		if (listaActivityImpls == null || listaActivityImpls.size() < 1)
			return listactivitiTask;
		// 为节点添加时间
		for (ActivityImpl activityImpl : listaActivityImpls) {
			String name = activityImpl.getProperty("name") + "";
			if (name.equals("Start") || name.equals("End"))
				continue;
			listactivitiTask.add(name);
		}
		return listactivitiTask;
	}

	@Override
	public HistoricProcessInstance getHistoricProcessInstance(IndentFlow indentFlow) {
		return activitiEngineService.getHistoryProcess(indentFlow.getIfFlowId());
	}

	@Override
	public boolean jumpPrevTask(IndentProject indentProject) {
		String activityId = "";
		ActivitiTask activitiTask = getCurrentTask(indentProject);
		if (activitiTask != null) {
			String currKey = activitiTask.getTaskDefinitionKey();
			List<ActivitiTask> list = getNodes(indentProject);
			ActivitiTask activitiTask2;
			for (int i = 0; i < list.size(); i++) {
				activitiTask2 = list.get(i);
				if (activitiTask2.getTaskDefinitionKey().equals(currKey) && i != 0) {
					activityId = list.get(i - 1).getTaskDefinitionKey();
					break;
				}
			}
			if (activityId.equals("")) {
				return false;
			}
		} else {
			return false;
		}
		return jumpTask(indentProject, activityId);
	}

	@Override
	public IndentProject updateNodes(IndentProject indentProject) {
		List<ActivitiTask> listactivitiTask = indentProject.getNodes();
		String processInstanceId = indentProject.getMasterFlowId().toString();
		// 查询预计时间
		List<FlowDate> listDates = flowDateMapper.findFlowDateByFlowId(processInstanceId);

		// 查询实际执行时间
		List<ActivitiTask> activitiTasks = getAfterTask(indentProject);
		FlowDate flowDate;
		ActivitiTask nextTask;
		for (int i = 0; i < listactivitiTask.size(); i++) {
			Iterator<FlowDate> iteratorFlow = listDates.iterator();
			Iterator<ActivitiTask> iteratorTask = activitiTasks.iterator();
			// 填充预计时间1341
			while (iteratorFlow.hasNext()) {
				flowDate = iteratorFlow.next();
				if (flowDate.getFdTaskId().equals(listactivitiTask.get(i).getTaskDefinitionKey())) {
					if (flowDate.getFdStartTime() == null || flowDate.getFdStartTime().equals(IndentFlow.defaultDate)) {
						flowDate.setFdStartTime("");
					}
					listactivitiTask.get(i).setScheduledTime(flowDate);
					break;
				}
			}
			// 填充实际时间
			while (iteratorTask.hasNext()) {
				nextTask = iteratorTask.next();
				if (nextTask.getTaskDefinitionKey().equals(listactivitiTask.get(i).getTaskDefinitionKey())) {
					listactivitiTask.get(i).setCreateTime(nextTask.getCreateTime());
				}
			}
		}
		return indentProject;
	}

}