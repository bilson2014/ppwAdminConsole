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
import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.SessionInfo;
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
import com.panfeng.util.ValidateUtil;

/**
 * 流程引擎操作类
 * 
 * @author Wang
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

	/**
	 * 流程资源服务，用于创建新的流程模板
	 */
	@Autowired
	private Resource flowTemplateResource;
	/**
	 * 流程权限认证服务，认证当前用户是否有有权限操坐当前节点
	 */
	@Autowired
	private Auth auth;
	/**
	 * 事件链服务，一个流程模板绑定唯一的任务链<br>
	 */
	@Autowired
	private TaskChainHandler taskChainHandler;

	@Autowired
	private TaskChainMapper taskChainMapper;

	@Autowired
	private TaskChainService taskChainService;

	/**
	 * 获取当前任务节点
	 */
	public Task getCurrentTask(String processInstanceId) {
		return taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
	}

	/**
	 * 获取当前节点之前的节点
	 */
	public List<HistoricTaskInstance> getAfterTask(String processInstanceId) {
		return historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();

	}

	/**
	 * 获取历史流程
	 */
	public HistoricProcessInstance getHistoryProcess(String processInstanceId) {
		return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}

	/**
	 * 推进流程
	 */
	@Deprecated
	public boolean completeTask(String processInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
		if (task == null)
			return false;
		taskService.complete(task.getId());
		return true;
	}

	/**
	 * 启动新的流程实例
	 */
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

	/**
	 * 执行开始任务
	 */
	public void executeStartEvent(SessionInfo sessionInfo, String flowId) {
		Task currentTask = getCurrentTask(flowId);
		String processDefinitionId = currentTask.getProcessDefinitionId();
		FlowTemplate template = getTemplate(processDefinitionId);
		FlowNode flowNode = template.getFlowNodes().get(0);
		if (flowNode != null) {
			taskChainHandler.execute(flowNode.getTaskChainId(), sessionInfo, flowId);
		}
	}

	/**
	 * 暂停当前流程实例
	 */
	public boolean suspendProcess(String processInstanceId) {
		ProcessInstance pi = getUnDoneProcessInstance(processInstanceId);
		if (pi == null)
			return false;
		runtimeService.suspendProcessInstanceById(pi.getProcessInstanceId());
		return true;
	}

	/**
	 * 恢复当前流程
	 */
	public boolean resumeProcess(String processInstanceId) {
		ProcessInstance pi = getUnDoneProcessInstance(processInstanceId);
		if (pi == null)
			return false;
		runtimeService.activateProcessInstanceById(pi.getProcessInstanceId());
		return true;
	}

	/**
	 * 获取当前流程实例的所有节点
	 */
	public List<ActivityImpl> getNodes(String processInstanceId) {
		HistoricTaskInstance historicTaskInstance = getDoneTaskInstance(processInstanceId);
		if (historicTaskInstance == null)
			return null;
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(historicTaskInstance.getProcessDefinitionId());
		return def.getActivities();
	}

	/**
	 * 获取为完成的流程实例
	 * 
	 * @param processInstanceId
	 * @return
	 */
	private ProcessInstance getUnDoneProcessInstance(String processInstanceId) {
		return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}

	/**
	 * 获取已完成的流程节点
	 */
	public HistoricTaskInstance getDoneTaskInstance(String processInstanceId) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取已完成的流程节点
	 */
	public List<HistoricTaskInstance> getHistoryProcessTask(String processInstanceId) {
		return historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
	}

	/**
	 * 检测当前流程实例是否是暂停状态
	 */
	@Override
	public boolean isSuspended(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		return processInstance != null ? processInstance.isSuspended() : false;
	}

	/**
	 * 检测当前流程实例是否是完成状态
	 */
	public boolean isFinish(String processInstanceId) {
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).finished().singleResult();
		// 任务已经完成了
		return historicProcessInstance != null;
	}

	/**
	 * 批量获取当前正在进行中的节点
	 */
	@Override
	public List<Task> getCurrentTask(List<String> processInstanceIds) {
		if (!ValidateUtil.isValid(processInstanceIds))
			return null;
		return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).list();
	}

	/**
	 * 批量获取当前正在暂停状态的流程实例
	 */
	@Override
	public List<ProcessInstance> isSuspendeds(String processDefinitionKey, Set<String> processInstanceIds) {
		return runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIds).list();
	}

	/**
	 * 批量获取当前已经完成状态的流程实例
	 */
	@Override
	public List<HistoricProcessInstance> isFinishs(String processDefinitionKey, Set<String> ids) {
		List<HistoricProcessInstance> historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey).processInstanceIds(ids).finished().list();
		// 任务已经完成了
		return historicProcessInstance;
	}

	// --------------------new----------------------------------------------------------------------------
	/**
	 * 获取数据库中所有模板的最新实例
	 */
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

	/**
	 * 获取当前流程资源生成的 流程图
	 */
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

	/**
	 * 获取当前流程生成的运行中 流程图
	 */
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

	/**
	 * 获取指定流程ID下的最新流程模板 IndentFlow:10:57504
	 */
	public FlowTemplate getTemplate(String flowTemplateId) {
		FlowTemplate flowTemplate = new FlowTemplate();
		LinkedList<FlowNode> flowNodes = new LinkedList<>();
		FlowNode startNode = null;
		FlowNode endNode = null;
		BpmnModel model = repositoryService.getBpmnModel(flowTemplateId);
		if (model != null) {
			flowTemplate.setId(model.getMainProcess().getId());// 一个模板里只有一个流程。即获取默认流程
			flowTemplate.setName(model.getMainProcess().getName());
			Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
			int index = 1;
			/**
			 * 将流程组件转化成，模板节点
			 */
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

	/**
	 * 创建新的流程节点
	 * 
	 * @param element
	 * @return
	 */
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

	/**
	 * 创建新的流程模板
	 */
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

	/**
	 * 推荐流程节点，验证当前权限，验证手动任务。执行自动任务
	 */
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

	/**
	 * 回退到上一个节点
	 */
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

	/**
	 * 节点跳转
	 */
	public void jumpTask(String activityId, String processInstanceId) {
		managementService.executeCommand(new JumpActivityCmd(processInstanceId, activityId));
	}

	/**
	 * 停止当前流程
	 */
	public void stopProcess(String processInstanceId) {
		jumpTask("endEvent", processInstanceId);
	}

	/**
	 * 删除流程实例
	 */
	public boolean removeProcess(String processInstanceId) {
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		if (pi != null) {
			runtimeService.deleteProcessInstance(pi.getProcessInstanceId(), "");
			historyService.deleteHistoricProcessInstance(pi.getProcessInstanceId());
		}
		return false;
	}

}
