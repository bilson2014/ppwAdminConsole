package com.panfeng.service.impl;

import java.awt.image.RasterFormatException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
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
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.panfeng.domain.BaseMsg;
import com.panfeng.flow.core.Auth;
import com.panfeng.flow.core.Resource;
import com.panfeng.flow.taskchain.TaskChainHandler;
import com.panfeng.persist.FlowCoreMapper;
import com.panfeng.persist.TaskChainMapper;
import com.panfeng.resource.model.FlowNode;
import com.panfeng.resource.model.FlowTemplate;
import com.panfeng.resource.model.NodesEvent;
import com.panfeng.resource.model.TaskChain;
import com.panfeng.resource.model.TaskChainNodesEventLink;
import com.panfeng.service.ActivitiEngineService;
import com.panfeng.service.TaskChainService;
import com.panfeng.service.impl.activiti.JumpActivityCmd;
import com.panfeng.util.Log;

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
	private HistoryService historyService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	private FlowCoreMapper flowCoreMapper;

	@Autowired
	private Resource flowTemplateResource;

	@Autowired
	private Auth auth;

	@Autowired
	private TaskChainHandler taskChainHandler;

	@Autowired
	private TaskChainMapper taskChainMapper;

	@Autowired
	private TaskChainService taskChainService;

	public Task getCurrentTask(String processInstanceId) {
		return taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
		// return
		// taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
	}

	public List<HistoricTaskInstance> getAfterTask(String processInstanceId) {
		return historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();

	}

	public HistoricProcessInstance getHistoryProcess(String processInstanceId) {
		return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}

	@Deprecated
	public boolean completeTask(String processInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
		if (task == null)
			return false;
		taskService.complete(task.getId());
		return true;
	}

	public String startProcess(String processDefinitionKey, String processInstanceBusinessKey) {
		ProcessInstance pi;
		if (processInstanceBusinessKey == null)
			pi = runtimeService.startProcessInstanceByKey(processDefinitionKey);
		else
			pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, processInstanceBusinessKey);
		if (pi == null) {
			return "";
		} else {
			return pi.getProcessInstanceId();
		}
	}
	
	public void executeStartEvent(SessionInfo sessionInfo,String flowId){
		Task currentTask = getCurrentTask(flowId);
		String processDefinitionId = currentTask.getProcessDefinitionId();
		FlowTemplate template = getTemplate(processDefinitionId);
		FlowNode flowNode = template.getFlowNodes().get(0);
		if (flowNode != null) {
			taskChainHandler.execute(flowNode.getTaskChainId(), sessionInfo, flowId);
		}
	}

	public boolean suspendProcess(String processInstanceId) {
		ProcessInstance pi = getUnDoneProcessInstance(processInstanceId);
		if (pi == null)
			return false;
		runtimeService.suspendProcessInstanceById(pi.getProcessInstanceId());
		return true;
	}

	public boolean resumeProcess(String processInstanceId) {
		ProcessInstance pi = getUnDoneProcessInstance(processInstanceId);
		if (pi == null)
			return false;
		runtimeService.activateProcessInstanceById(pi.getProcessInstanceId());
		return true;
	}

	public List<ActivityImpl> getNodes(String processInstanceId) {
		HistoricTaskInstance historicTaskInstance = getDoneTaskInstance(processInstanceId);
		if (historicTaskInstance == null)
			return null;
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(historicTaskInstance.getProcessDefinitionId());
		return def.getActivities();
	}

	private ProcessInstance getUnDoneProcessInstance(String processInstanceId) {
		return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}

	public HistoricTaskInstance getDoneTaskInstance(String processInstanceId) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<HistoricTaskInstance> getHistoryProcessTask(String processInstanceId) {
		return historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
	}

	@Override
	public boolean isSuspended(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		return processInstance != null ? processInstance.isSuspended() : false;
	}

	public boolean isFinish(String processInstanceId) {
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).finished().singleResult();
		// 任务已经完成了
		return historicProcessInstance != null;
	}

	@Override
	public List<Task> getCurrentTask(List<String> processInstanceIds) {
		if (!ValidateUtil.isValid(processInstanceIds))
			return null;
		return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).list();
	}

	@Override
	public List<ProcessInstance> isSuspendeds(String processDefinitionKey, Set<String> processInstanceIds) {
		return runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIds).list();
	}

	@Override
	public List<HistoricProcessInstance> isFinishs(String processDefinitionKey, Set<String> ids) {
		List<HistoricProcessInstance> historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey).processInstanceIds(ids).finished().list();
		// 任务已经完成了
		return historicProcessInstance;
	}

	// --------------------new----------------------------------------------------------------------------
	@Override
	public List<FlowTemplate> getAllTemplate() {
		Map<String, FlowTemplate> flowTemplateMap = new HashMap<>();
		List<FlowTemplate> list = flowCoreMapper.getAllTemplate();
		if (ValidateUtil.isValid(list)) {
			// 因为一个流程可能有好多版本，因此需要过滤模板
			for (FlowTemplate flowTemplate : list) {
				if (flowTemplateMap.containsKey(flowTemplate.getD_key())) {
					// 查找最新的
					FlowTemplate f2 = flowTemplateMap.get(flowTemplate.getD_key());

					String f1key = flowTemplate.getD_id();
					String f2key = f2.getD_id();

					String[] f1array = f1key.split("\\:");
					String[] f2array = f2key.split("\\:");
					if (Long.parseLong(f1array[1]) > Long.parseLong(f2array[1])) {
						flowTemplateMap.put(flowTemplate.getD_key(), flowTemplate);
					}
				} else {
					flowTemplateMap.put(flowTemplate.getD_key(), flowTemplate);
				}
			}
		}
		list = new ArrayList<>();
		list.addAll(flowTemplateMap.values());
		return list;
	}

	@Override
	public InputStream getImage(String deployment_id) {
		List<String> names = repositoryService.getDeploymentResourceNames(deployment_id);
		String imageName = null;
		for (String name : names) {
			if (name.indexOf(".png") >= 0) {
				imageName = name;
			}
		}
		if (imageName != null) {
			// 通过部署ID和文件名称得到文件的输入流
			return repositoryService.getResourceAsStream(deployment_id, imageName);
		}
		return null;
	}

	public InputStream getProcessImage(String processId) {
		Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
		try {
			InputStream is = new DefaultProcessDiagramGenerator().generateDiagram(
					repositoryService.getBpmnModel(task.getProcessDefinitionId()), "png",
					runtimeService.getActiveActivityIds(task.getExecutionId()), Collections.<String> emptyList(), "宋体",
					"宋体", null, 1.0);
			return is;
		} catch (RasterFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	public FlowTemplate getTemplate(String flowTemplateId) {
		FlowTemplate flowTemplate = new FlowTemplate();
		LinkedList<FlowNode> flowNodes = new LinkedList<>();
		FlowNode startNode = null;
		FlowNode endNode = null;
		BpmnModel model = repositoryService.getBpmnModel(flowTemplateId);
		if (model != null) {
			flowTemplate.setId(model.getMainProcess().getId());// 一个模板里只有一个流程
			flowTemplate.setName(model.getMainProcess().getName());
			Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
			int index = 1;
			for (FlowElement flowElement : flowElements) {
				if (flowElement instanceof UserTask) {
					UserTask userTask = (UserTask) flowElement;
					FlowNode flowNode = createFlowNode(userTask);
					flowNode.setIndex(index);
					flowNodes.add(flowNode);
					index++;
				} else if (flowElement instanceof StartEvent) {
					startNode = createFlowNode(flowElement);
				} else if (flowElement instanceof EndEvent) {
					endNode = createFlowNode(flowElement);
				}
			}
			startNode.setIndex(0);
			flowNodes.addFirst(startNode);
			endNode.setIndex(index);
			flowNodes.addLast(endNode);
		}
		flowTemplate.setD_id(flowTemplateId);
		flowTemplate.setFlowNodes(flowNodes);
		return flowTemplate;
	}

	private FlowNode createFlowNode(FlowElement element) {
		FlowNode flowNode = new FlowNode();
		flowNode.setflowOptionst(element.getDocumentation());
		flowNode.setId(element.getId());
		flowNode.setName(element.getName());
		if (flowNode.getTaskChainId() != null) {
			List<TaskChainNodesEventLink> linkeds = taskChainMapper.findLinkByTaskChainId(flowNode.getTaskChainId());
			String str = "";
			for (int i = 0; i < linkeds.size(); i++) {
				if (i == 0) {
					str += linkeds.get(i).getNodeEventId();
				} else {
					str += "," + linkeds.get(i).getNodeEventId();
				}
			}
			flowNode.setEvents(str);
		}
		return flowNode;
	}

	public BaseMsg createFlowTemplate(FlowTemplate flowTemplate) {
		// 处理事件链
		List<FlowNode> flowNodes = flowTemplate.getFlowNodes();
		for (int i = 0; i < flowNodes.size(); i++) {
			FlowNode flowNode = flowNodes.get(i);
			TaskChain taskChain = new TaskChain();
			List<NodesEvent> nodesEvents = new ArrayList<>();
			Long[] ids = flowNode.getEventIds();
			if (ids != null) {
				for (Long id : ids) {
					NodesEvent ne = new NodesEvent();
					ne.setNodesEventId(id);
					nodesEvents.add(ne);
				}
				taskChain.setNodesEvents(nodesEvents);
			}
			if (flowNode.getTaskChainId() != null) {// 更新
				taskChain.setTaskChainId(flowNode.getTaskChainId());
				taskChainService.updateNodes(taskChain);
			} else { // 新建
				if (nodesEvents.size() > 0) {
					taskChainService.addNodes(taskChain);
					flowNode.setTaskChainId(taskChain.getTaskChainId());
				}
			}
		}
		boolean res = false;
		if (!ValidateUtil.isValid(flowTemplate.getD_id())) {
			res = flowTemplateResource.createTemplate(flowTemplate);
		} else {
			FlowTemplate flowTemplate2 = getTemplate(flowTemplate.getD_id());
			if (!flowTemplate2.equals(flowTemplate)) {
				Log.error("流程被修改！", null);
				res = flowTemplateResource.createTemplate(flowTemplate);
			} else {
				return new BaseMsg(BaseMsg.NORMAL, "事件修改完毕！", true);
			}
		}
		if (res) {
			return new BaseMsg(BaseMsg.NORMAL, "模板修改成功！", true);
		} else {
			return new BaseMsg(BaseMsg.ERROR, "模板创建失败！", false);
		}
	}

	public BaseMsg completeTask_2(SessionInfo sessionInfo, String processId) {
		Task task = getCurrentTask(processId);
		FlowNode flowNode = new FlowNode();
		flowNode.setflowOptionst(task.getDescription());
		// 身份认证
		boolean res = auth.auth(sessionInfo, flowNode.getAssignee());
		if (res) {
			// 任务完成度认证
			JsonArray jsonArray = taskChainHandler.checkTaskStatus(flowNode.getTaskChainId());
			if (jsonArray == null) {
				// 成功啦，愉快的进入下一步吧
				taskService.complete(task.getId());
				taskChainHandler.execute(flowNode.getTaskChainId(), sessionInfo, processId);
				return new BaseMsg(BaseMsg.NORMAL, "true", "true");
			} else {
				Log.error(jsonArray.toString() + "执行下一步任务失败！", sessionInfo);
				// 失败了，给！你自己去看结果吧
				return new BaseMsg(BaseMsg.ERROR, "有没有完成的任务，别墨迹赶紧的！", jsonArray.toString());
			}
		}
		Log.error("没有权限执行下一步！", sessionInfo);
		return new BaseMsg(BaseMsg.ERROR, "嘿！你是哪来的，你没权限！", null);
	}

	public void prevTask(SessionInfo sessionInfo, String processInstanceId) {
		Task task = getCurrentTask(processInstanceId);
		FlowTemplate flowTemplate = getTemplate(task.getProcessDefinitionId());
		LinkedList<FlowNode> flowNodes = (LinkedList<FlowNode>) flowTemplate.getFlowNodes();
		FlowNode prevNode = null;
		for (int i = 0; i < flowNodes.size(); i++) {
			FlowNode flowNode = flowNodes.get(i);
			if (flowNode.getId().equals(task.getTaskDefinitionKey())) {
				if (prevNode != null) {
					jumpTask(prevNode.getId(), processInstanceId);
					break;
				} else {
					System.out.println("第一个节点不能继续向前了！");
				}
			}
			prevNode = flowNode;
		}

	}

	public void jumpTask(String activityId, String processInstanceId) {
		managementService.executeCommand(new JumpActivityCmd(processInstanceId, activityId));
	}

	public void stopProcess(String processInstanceId) {
		jumpTask("endEvent", processInstanceId);
	}

	public boolean removeProcess(String processInstanceId) {
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		if (pi != null) {
			runtimeService.deleteProcessInstance(pi.getProcessInstanceId(), "");
			historyService.deleteHistoricProcessInstance(pi.getProcessInstanceId());
		}
		return false;
	}

	// public List<BaseFlowModel> getFlows(HashSet<String> ids) {
	// if (ValidateUtil.isValid(ids)) {
	// List<BaseFlowModel> flowModels = new ArrayList<>();
	// // TODO 未完结！流程当前所处节点信息
	// List<Task> list = taskService.createTaskQuery().processInstanceIdIn(new
	// ArrayList<>(ids)).list();
	//
	// ProcessInstanceQuery processInstanceQuery =
	// runtimeService.createProcessInstanceQuery()
	// .processInstanceIds(ids);
	// // 暂停流程实例
	// List<ProcessInstance> suspendList =
	// processInstanceQuery.suspended().list();
	// if (ValidateUtil.isValid(suspendList)) {
	// for (ProcessInstance processInstance : suspendList) {
	// BaseFlowModel baseFlowModel = new BaseFlowModel();
	// baseFlowModel.setFlowId(processInstance.getProcessDefinitionId());
	//
	// String processId = processInstance.getProcessInstanceId();
	// ids.remove(processId);
	// }
	// }
	// // 进行中流程
	// List<ProcessInstance> activeList = processInstanceQuery.active().list();
	// if (ValidateUtil.isValid(activeList)) {
	// for (ProcessInstance processInstance : activeList) {
	// String processId = processInstance.getProcessInstanceId();
	// ids.remove(processId);
	// }
	// }
	//
	// // 历史流程
	// List<HistoricProcessInstance> haHistoricProcessInstances = historyService
	// .createHistoricProcessInstanceQuery().processInstanceIds(ids).finished().list();
	//
	// List<HistoricTaskInstance> historicTaskInstances =
	// historyService.createHistoricTaskInstanceQuery()
	// .processInstanceIdIn(new ArrayList<>(ids)).finished().list();
	//
	// // 拼装
	//
	// }
	// return null;
	// }
	//
	// @Override
	// public List<BaseFlowModel> getAll(String flowTemplateVersionId) {
	// ids
	// 先检查进行中队列 --》暂停
	// 检查进行中队列 --》除去暂停的就是进行中的集合结果
	// 检查历史队列 --》已经完成的集合
	//
	// HistoricProcessInstanceQuery hps =
	// historyService.createHistoricProcessInstanceQuery()
	// .processDefinitionId(flowTemplateVersionId);
	// hps.finished().list();
	// hps.unfinished();
	// ProcessInstance ps;
	//
	//
	// runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIds)
	// List<BaseFlowModel> baseFlowModels = new ArrayList<>();
	// if (ValidateUtil.isValid(hps)) {
	// for (HistoricProcessInstance historicProcessInstance : hps) {
	// BaseFlowModel baseFlowModel = new BaseFlowModel();
	// baseFlowModel.setFlowId(historicProcessInstance.getId());
	//
	// baseFlowModel.setFlowTemplateId(processInstance.getProcessDefinitionKey());
	// baseFlowModel.setFlowTemplateName(processInstance.getProcessDefinitionName());
	// baseFlowModel.setFlowTemplateVersionId(historicProcessInstance.getProcessDefinitionId());
	// baseFlowModel.setCreateTime(processInstance.);
	// }
	// }
	//
	// return null;
	// }

}
