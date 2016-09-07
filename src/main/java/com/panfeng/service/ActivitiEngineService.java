package com.panfeng.service;

import java.util.List;
import java.util.Set;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public interface ActivitiEngineService {
	Task getCurrentTask(String processDefinitionKey, String processInstanceBusinessKey, String processInstanceId);

	List<Task> getCurrentTask(String processDefinitionKey, List<String> processInstanceIds);

	List<HistoricTaskInstance> getAfterTask(String processDefinitionKey, String processInstanceBusinessKey,
			String processInstanceId);

	HistoricProcessInstance getHistoryProcess(String processDefinitionKey, String processInstanceBusinessKey,
			String processInstanceId);

	List<HistoricTaskInstance> getHistoryProcessTask(String processDefinitionKey, String processInstanceBusinessKey,
			String processInstanceId);

	boolean completeTask(String processDefinitionKey, String processInstanceBusinessKey, String processInstanceId);

	String startProcess(String processDefinitionKey, String processInstanceBusinessKey);

	boolean suspendProcess(String processDefinitionKey, String processInstanceBusinessKey, String processInstanceId);

	boolean resumeProcess(String processDefinitionKey, String processInstanceBusinessKey, String processInstanceId);

	boolean removeProcess(String processDefinitionKey, String processInstanceBusinessKey, String processInstanceId);

	boolean jumpTask(String processDefinitionKey, String processInstanceBusinessKey, String activityId,
			String processInstanceId);

	List<ActivityImpl> getNodes(String processDefinitionKey, String processInstanceBusinessKey,
			String processInstanceId);

	HistoricTaskInstance getDoneTaskInstance(String processDefinitionKey, String processInstanceBusinessKey,
			String processInstanceId);

	boolean isSuspended(String processDefinitionKey, String processInstanceBusinessKey, String processInstanceId);

	List<ProcessInstance> isSuspendeds(String processDefinitionKey, Set<String> processInstanceIds);

	boolean isFinish(String processDefinitionKey, String processInstanceBusinessKey, String processInstanceId);

	// ---------------------------------phone------------------------------

}
