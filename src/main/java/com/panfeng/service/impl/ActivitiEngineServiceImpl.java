package com.panfeng.service.impl;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.service.ActivitiEngineService;
import com.panfeng.service.impl.activiti.JumpActivityCmd;

/**
 * 流程引擎操作类
 * 
 * @author Wang,LM
 *
 */
@Service
public class ActivitiEngineServiceImpl implements ActivitiEngineService {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ManagementService managementService;

	public Task getCurrentTask(String processDefinitionKey,
			String processInstanceBusinessKey, String processInstanceId) {
		return taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).active().singleResult();
	}

	public List<HistoricTaskInstance> getAfterTask(String processDefinitionKey,
			String processInstanceBusinessKey, String processInstanceId) {
		return historyService.createHistoricTaskInstanceQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).list();
	}

	public HistoricProcessInstance getHistoryProcess(
			String processDefinitionKey, String processInstanceBusinessKey,
			String processInstanceId) {
		return historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).singleResult();

	}

	public boolean completeTask(String processDefinitionKey,
			String processInstanceBusinessKey, String processInstanceId) {
		Task task = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).active().singleResult();
		if (task == null)
			return false;
		taskService.complete(task.getId());
		return true;
	}

	public String startProcess(String processDefinitionKey,
			String processInstanceBusinessKey) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(
				processDefinitionKey, processInstanceBusinessKey);
		return pi == null ? "" : pi.getProcessInstanceId();
	}

	public boolean suspendProcess(String processDefinitionKey,
			String processInstanceBusinessKey, String processInstanceId) {
		ProcessInstance pi = getUnDoneProcessInstance(processDefinitionKey,
				processInstanceBusinessKey, processInstanceId);
		if (pi == null)
			return false;
		runtimeService.suspendProcessInstanceById(pi.getProcessInstanceId());
		return true;
	}

	public boolean resumeProcess(String processDefinitionKey,
			String processInstanceBusinessKey, String processInstanceId) {
		ProcessInstance pi = getUnDoneProcessInstance(processDefinitionKey,
				processInstanceBusinessKey, processInstanceId);
		if (pi == null)
			return false;
		runtimeService.activateProcessInstanceById(pi.getProcessInstanceId());
		return true;
	}

	public boolean removeProcess(String processDefinitionKey,
			String processInstanceBusinessKey, String processInstanceId) {
		ProcessInstance pi = getUnDoneProcessInstance(processDefinitionKey,
				processInstanceBusinessKey, processInstanceId);
		if (pi != null) {
			runtimeService.deleteProcessInstance(pi.getProcessInstanceId(), "");
			historyService.deleteHistoricProcessInstance(pi
					.getProcessInstanceId());
		}
		return false;
	}

	public boolean jumpTask(String processDefinitionKey,
			String processInstanceBusinessKey, String activityId,
			String processInstanceId) {
		Task task = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).active().singleResult();
		managementService.executeCommand(new JumpActivityCmd(task
				.getProcessInstanceId(), activityId));
		return true;
	}

	public List<ActivityImpl> getNodes(String processDefinitionKey,
			String processInstanceBusinessKey, String processInstanceId) {
		HistoricTaskInstance historicTaskInstance = getDoneTaskInstance(
				processDefinitionKey, processInstanceBusinessKey,
				processInstanceId);
		if (historicTaskInstance == null)
			return null;
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(historicTaskInstance
						.getProcessDefinitionId());
		return def.getActivities();
	}

	private ProcessInstance getUnDoneProcessInstance(
			String processDefinitionKey, String processInstanceBusinessKey,
			String processInstanceId) {
		return runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).singleResult();
	}

	public HistoricTaskInstance getDoneTaskInstance(
			String processDefinitionKey, String processInstanceBusinessKey,
			String processInstanceId) {
		List<HistoricTaskInstance> list = historyService
				.createHistoricTaskInstanceQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<HistoricTaskInstance> getHistoryProcessTask(
			String processDefinitionKey, String processInstanceBusinessKey,
			String processInstanceId) {
		return historyService.createHistoricTaskInstanceQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).list();
	}

	@Override
	public boolean isSuspended(String processDefinitionKey,
			String processInstanceBusinessKey, String processInstanceId) {
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).singleResult();
		return processInstance != null ? processInstance.isSuspended() : false;
	}

	@Override
	public boolean isFinish(String processDefinitionKey,
			String processInstanceBusinessKey, String processInstanceId) {
		HistoricProcessInstance historicProcessInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey(processInstanceBusinessKey)
				.processInstanceId(processInstanceId).finished().singleResult();
		// 任务已经完成了
		return historicProcessInstance != null;
	}

}
