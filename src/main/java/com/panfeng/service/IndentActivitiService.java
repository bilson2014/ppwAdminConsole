package com.panfeng.service;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;

import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;

public interface IndentActivitiService {

	ActivitiTask getCurrentTask(IndentProject indentProject);

	List<ActivitiTask> getAfterTask(IndentProject indentProject);

	HistoricProcessInstance getHistoryProcess(IndentProject indentProject);

	List<ActivitiTask> getHistoryProcessTask(IndentProject indentProject);

	boolean completeTask(IndentProject indentProject);

	boolean startProcess(IndentProject indentProject);

	boolean suspendProcess(IndentProject indentProject);

	boolean resumeProcess(IndentProject indentProject);

	boolean removeProcess(IndentProject indentProject);

	boolean jumpTask(IndentProject indentProject, String activityId);

	List<ActivitiTask> getNodes(IndentProject indentProject);

	List<IndentFlow> getIndentFlows(IndentProject indentProject);

	HistoricProcessInstance getHistoricProcessInstance(IndentFlow indentFlow);

	List<HistoricTaskInstance> getHistoryProcessTask_O(
			IndentProject indentProject);

}
