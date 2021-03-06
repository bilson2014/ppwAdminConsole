package com.panfeng.service;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;

import com.paipianwang.pat.common.entity.SessionInfo;
import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;

public interface IndentActivitiService {

	ActivitiTask getCurrentTask(IndentProject indentProject);

	List<IndentProject> fullCurrentTask(List<IndentProject> ips);

	List<ActivitiTask> getAfterTask(IndentProject indentProject);

	HistoricProcessInstance getHistoryProcess(IndentProject indentProject);

	List<ActivitiTask> getHistoryProcessTask(IndentProject indentProject);

	String completeTask(IndentProject indentProject, String processId,SessionInfo sessionInfo);

	boolean startProcess(IndentProject indentProject,SessionInfo sessionInfo);

	boolean suspendProcess(IndentProject indentProject,boolean isBack);

	boolean resumeProcess(IndentProject indentProject,boolean isBack);

	boolean removeProcess(IndentProject indentProject);

	boolean jumpTask(IndentProject indentProject, String activityId);

	boolean jumpPrevTask(IndentProject indentProject);

	List<ActivitiTask> getNodes(IndentProject indentProject);

	IndentProject updateNodes(IndentProject indentProject);

	List<IndentFlow> getIndentFlows(IndentProject indentProject);

	HistoricProcessInstance getHistoricProcessInstance(IndentFlow indentFlow);

	List<HistoricTaskInstance> getHistoryProcessTask_O(IndentProject indentProject);
	
	BaseMsg verifyIntegrity(IndentProject indentProject);
	
	String getIndentCurrentFlowId(IndentProject indentProject);
	
	List<String> getBpmnNodes(IndentProject indentProject);

	BaseMsg completeTask_2(IndentProject indentProject, String processId,SessionInfo sessionInfo );

}
