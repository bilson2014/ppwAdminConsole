package com.panfeng.service.impl;

import java.util.ArrayList;
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

import com.panfeng.persist.FlowDateMapper;
import com.panfeng.persist.IndentFlowMapper;
import com.panfeng.persist.IndentProjectMapper;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.FlowDate;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.service.ActivitiEngineService;
import com.panfeng.service.IndentActivitiService;
import com.panfeng.service.IndentCommentService;

@Service
public class IndentActivitiServiceImpl implements IndentActivitiService {

	@Autowired
	private ActivitiEngineService activitiEngineService;
	@Autowired
	private IndentFlowMapper flowMapper;
	@Autowired
	private FlowDateMapper flowDateMapper;

	private static String processDefinitionKey = "IndentFlow";
	@Autowired
	private IndentCommentService indentCommentService;
	@Autowired
	private IndentProjectMapper indentProjectMapper;

	@Override
	public ActivitiTask getCurrentTask(IndentProject indentProject) {
		Task task = activitiEngineService.getCurrentTask(processDefinitionKey,
				String.valueOf(indentProject.getId()),
				getIndentCurrentFlowId(indentProject));
		if (task == null) {
			// 请求流程状态
			boolean isSuspended = activitiEngineService.isSuspended(
					processDefinitionKey,
					String.valueOf(indentProject.getId()),
					getIndentCurrentFlowId(indentProject));
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
	public List<ActivitiTask> getAfterTask(IndentProject indentProject) {
		List<HistoricTaskInstance> listTaskInstances = activitiEngineService
				.getAfterTask(processDefinitionKey,
						String.valueOf(indentProject.getId()),
						getIndentCurrentFlowId(indentProject));

		List<ActivitiTask> list = new ArrayList<>();
		for (HistoricTaskInstance historicTaskInstance : listTaskInstances) {
			list.add(ActivitiTask.TaskToActivitiTask(historicTaskInstance));
		}

		return list;
	}

	@Override
	public HistoricProcessInstance getHistoryProcess(IndentProject indentProject) {
		HistoricProcessInstance historicProcessInstance = activitiEngineService
				.getHistoryProcess(processDefinitionKey,
						String.valueOf(indentProject.getId()),
						getIndentCurrentFlowId(indentProject));
		return historicProcessInstance;
	}

	@Override
	public List<ActivitiTask> getHistoryProcessTask(IndentProject indentProject) {
		List<HistoricTaskInstance> listTaskInstances = activitiEngineService
				.getHistoryProcessTask(processDefinitionKey,
						String.valueOf(indentProject.getId()),
						getIndentCurrentFlowId(indentProject));
		List<ActivitiTask> list = new ArrayList<>();
		for (HistoricTaskInstance historicTaskInstance : listTaskInstances) {
			list.add(ActivitiTask.TaskToActivitiTask(historicTaskInstance));
		}
		return list;
	}

	@Override
	public List<HistoricTaskInstance> getHistoryProcessTask_O(
			IndentProject indentProject) {
		return activitiEngineService.getHistoryProcessTask(
				processDefinitionKey, String.valueOf(indentProject.getId()),
				getIndentCurrentFlowId(indentProject));
	}

	@Override
	public boolean completeTask(IndentProject indentProject) {

		boolean res = false;
		ActivitiTask activitiTask=getCurrentTask(indentProject);
		indentCommentService.createSystemMsg("完成了\""+activitiTask.getName() + "\"任务", indentProject);
		res = activitiEngineService.completeTask(processDefinitionKey,
				String.valueOf(indentProject.getId()),
				getIndentCurrentFlowId(indentProject));
		// 检测任务是否已经完成

		boolean isFinish = activitiEngineService.isFinish(processDefinitionKey,
				String.valueOf(indentProject.getId()),
				getIndentCurrentFlowId(indentProject));
		if (isFinish) {
			// 更新项目状态
//			indentProject2.setState(IndentProject.PROJECT_FINISH);
//			indentProjectMapper.update(indentProject2);
//			
//			indentCommentService.createSystemMsg("已经完成"+indentProject.getProjectName()+"项目", indentProject);
			indentProjectMapper.updateState(indentProject.getId(),IndentProject.PROJECT_FINISH);
		}
		return res;
	}

	@Override
	public boolean startProcess(IndentProject indentProject) {
		// 禁用所有流程
		disableAllFlow(indentProject);
		// 启动流程
		String flowId = activitiEngineService.startProcess(
				processDefinitionKey, String.valueOf(indentProject.getId()));
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
		List<FlowDate> dates = IndentFlow.createFlowDates(flowId,
				indentProject, nodes);
		for (FlowDate flowDate : dates) {
			flowDateMapper.save(flowDate);
		}
		indentCommentService.createSystemMsg(
				"创建了 " + indentProject.getProjectName() + "项目", indentProject);
		return l > 0 ? true : false;
	}

	@Override
	public boolean suspendProcess(IndentProject indentProject) {

		boolean res = activitiEngineService.suspendProcess(
				processDefinitionKey, String.valueOf(indentProject.getId()),
				getIndentCurrentFlowId(indentProject));
		if (res) {
			indentCommentService.createSystemMsg(
					" 暂停了 " + indentProject.getProjectName() + "项目",
					indentProject);
			//add suspend state by laowang 2016.5.31 16:51 begin 
				indentProjectMapper.updateState(indentProject.getId(),IndentProject.PROJECT_SUSPEND);
			//add suspend state by laowang 2016.5.31 16:51 end
		}
		return res;
	}

	@Override
	public boolean resumeProcess(IndentProject indentProject) {
		boolean res = activitiEngineService.resumeProcess(processDefinitionKey,
				String.valueOf(indentProject.getId()),
				getIndentCurrentFlowId(indentProject));
		if (res) {
			indentCommentService.createSystemMsg(
					" 恢复了 " + indentProject.getProjectName() + "项目",
					indentProject);
		//add resume state by laowang 2016.5.31 16:51 begin 
			indentProjectMapper.updateState(indentProject.getId(),IndentProject.PROJECT_NORMAL);
		//add resume state by laowang 2016.5.31 16:51 end
		}
		return res;
	}

	/**
	 * 删除当前活动的项目
	 */
	@Override
	public boolean removeProcess(IndentProject indentProject) {
		IndentFlow indentFlow = flowMapper.findFlowByIndent(indentProject);
		flowMapper.deleteByflowId(indentProject, indentFlow.getFdFlowId());
		return activitiEngineService
				.removeProcess(processDefinitionKey,
						String.valueOf(indentProject.getId()),
						indentFlow.getFdFlowId());
	}

	@Override
	public boolean jumpTask(IndentProject indentProject, String activityId) {
		boolean res = activitiEngineService.jumpTask(processDefinitionKey,
				String.valueOf(indentProject.getId()), activityId,
				getIndentCurrentFlowId(indentProject));
		
		if (res) {
			String taskName="";
			List<ActivitiTask> list=getNodes(indentProject);
			for (ActivitiTask activitiTask : list) {
				if(activityId.equals(activitiTask.getTaskDefinitionKey())){
					taskName=activitiTask.getName();
					break;
				}
			}
			indentCommentService.createSystemMsg(
					" 将任务节点跳转到 -->" + taskName+ "阶段",
					indentProject);
		}
		return res;
	}

	private String getIndentCurrentFlowId(IndentProject indentProject) {
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
		List<ActivityImpl> listaActivityImpls = activitiEngineService.getNodes(
				processDefinitionKey, String.valueOf(indentProject.getId()),
				getIndentCurrentFlowId(indentProject));
		if (listaActivityImpls == null || listaActivityImpls.size() < 1)
			return listactivitiTask;

		HistoricTaskInstance task = activitiEngineService.getDoneTaskInstance(
				processDefinitionKey, String.valueOf(indentProject.getId()),
				getIndentCurrentFlowId(indentProject));
		String processInstanceId = task.getProcessInstanceId();

		// 查询预计时间
		List<FlowDate> listDates = flowDateMapper
				.findFlowDateByFlowId(processInstanceId);
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
			TaskDefinition td = (TaskDefinition) activityImpl
					.getProperty("taskDefinition");
			activitiTask.setDescription(activityImpl
					.getProperty("documentation") + "");
			if (td != null && td.getAssigneeExpression() != null) {
				activitiTask.setOwner(td.getAssigneeExpression()
						.getExpressionText());
			}
			// 填充预计时间
			while (iteratorFlow.hasNext()) {
				flowDate = iteratorFlow.next();
				if (flowDate.getFdTaskId().equals(activityImpl.getId())) {
					if (flowDate.getFdStartTime() == null
							|| flowDate.getFdStartTime().equals(
									IndentFlow.defaultDate)) {
						flowDate.setFdStartTime("");
					}
					activitiTask.setScheduledTime(flowDate);
					break;
				}
			}
			// 填充实际时间
			while (iteratorTask.hasNext()) {
				nextTask = iteratorTask.next();
				if (nextTask.getTaskDefinitionKey()
						.equals(activityImpl.getId())) {
					activitiTask.setCreateTime(nextTask.getCreateTime());
				}
			}
			listactivitiTask.add(activitiTask);
		}
		return listactivitiTask;
	}

	@Override
	public HistoricProcessInstance getHistoricProcessInstance(
			IndentFlow indentFlow) {
		return activitiEngineService.getHistoryProcess(processDefinitionKey,
				String.valueOf(indentFlow.getIfIndentId()),
				indentFlow.getIfFlowId());
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
				if (activitiTask2.getTaskDefinitionKey().equals(currKey)
						&& i != 0) {
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
}