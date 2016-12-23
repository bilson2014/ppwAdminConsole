package com.panfeng.service;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.FlowTemplate;

public interface ActivitiEngineService {
	Task getCurrentTask(String processInstanceId);

	List<Task> getCurrentTask(List<String> processInstanceIds);

	List<HistoricTaskInstance> getAfterTask(String processInstanceId);

	HistoricProcessInstance getHistoryProcess(String processInstanceId);

	List<HistoricTaskInstance> getHistoryProcessTask(String processInstanceId);

	boolean completeTask(String processInstanceId);

	String startProcess(String processDefinitionKey, String processInstanceBusinessKey);

	boolean suspendProcess(String processInstanceId);

	boolean resumeProcess(String processInstanceId);

	boolean removeProcess(String processInstanceId);

	void jumpTask(String activityId, String processInstanceId);

	List<ActivityImpl> getNodes(String processInstanceId);

	HistoricTaskInstance getDoneTaskInstance(String processInstanceId);

	boolean isSuspended(String processInstanceId);

	List<ProcessInstance> isSuspendeds(String processDefinitionKey, Set<String> processInstanceIds);

	boolean isFinish(String processInstanceId);

	List<HistoricProcessInstance> isFinishs(String processDefinitionKey, Set<String> ids);

	// ---------------------------------new----------------------------------------

	List<FlowTemplate> getAllTemplate();

	InputStream getImage(String deployment_id);

	InputStream getProcessImage(String processId);

	FlowTemplate getTemplate(String flowTemplateId);

	BaseMsg createFlowTemplate(FlowTemplate flowTemplate);

	BaseMsg completeTask_2(SessionInfo sessionInfo, String processId);

	void prevTask(SessionInfo sessionInfo, String processInstanceId);

	void stopProcess(String processInstanceId);

	void executeStartEvent(SessionInfo sessionInfo,String flowId);

}
