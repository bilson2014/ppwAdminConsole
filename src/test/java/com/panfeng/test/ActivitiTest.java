package com.panfeng.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.service.impl.activiti.JumpActivityCmd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class ActivitiTest {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;

	String processDefinitionKey = "IndentFlow";// 流程定义的key,也就是bpmn中存在的ID
	@Autowired
	private ManagementService managementService;

	@Test
	public void init() {
		List<Task> t = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("test1").list();
		for (Task task : t) {
			System.out.println(task.getName());
		}
	}

	@Test
	public void test() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", 22);
		map.put("是否都比", "是的!!");
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("slt",
				"test1", map);// //按照流程定义的key启动流程实例
		System.out.println("流程实例ID：" + pi.getProcessInstanceId());// 流程实例ID：101
		System.out.println("activitiID" + pi.getActivityId());

	}

	/** 完成任务 */
	@Test
	public void completeTask() {
		// String assignee = "陆涛";// 当前任务办理人
		// Task tasks = taskService.createTaskQuery()// 创建一个任务查询对象
		// .taskAssignee(assignee).singleResult();
		// taskService.complete(tasks.getId());
		// System.out.println("完成任务：" + tasks.getId());
		
		
		
		Task task = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("test1").singleResult();
		System.out.println(task.getName());
		// map.put("state", "next");
		taskService.complete(task.getId());

	}

	@Test
	public void test2() {
		List<Task> task = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("test1").list();
		for (Task task2 : task) {
			System.out.println(task2.getName());
		}
	}

	@Test
	public void jump() {
		Task task = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("test1").active().singleResult();
		System.out.println(task.getName());
		managementService.executeCommand(new JumpActivityCmd(task
				.getProcessInstanceId(), "gt"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		task = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("test1").active().singleResult();
		System.out.println(task.getName());
	}

	@Test
	public void getnode() {

		Task task = taskService.createTaskQuery().processDefinitionKey("test")
				.processInstanceBusinessKey("test1").singleResult();

		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(task.getProcessDefinitionId());
		List<ActivityImpl> activitiList = def.getActivities();
		for (ActivityImpl activityImpl : activitiList) {
			System.out.println(activityImpl.getId());
			System.out.println(activityImpl.getProperty("name"));
		}
	}

	@Test
	public void findPersonnelTaskList() {
		// String assignee = "陆涛";// 当前任务办理人
		// List<Task> tasks = taskService.createTaskQuery()// 创建一个任务查询对象
		// .taskAssignee(assignee).list();
		List<Task> tasks = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("test1").list();
		if (tasks != null && tasks.size() > 0) {
			for (Task task : tasks) {
				System.out.println("任务ID:" + task.getId());
				System.out.println("任务的办理人:" + task.getAssignee());
				System.out.println("任务名称:" + task.getName());
				System.out.println("任务的创建时间:" + task.getCreateTime());
				System.out.println("任务ID:" + task.getId());
				System.out.println("流程实例ID:" + task.getProcessInstanceId());
				System.out.println("#####################################");
			}
		}

	}

	@Test
	public void getfinish() {
		long runtimeCounter = runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey("test1").count();
		long historicCounter = historyService
				.createHistoricProcessInstanceQuery().finished()
				.processInstanceBusinessKey("test1").count();
		System.out.println(runtimeCounter + "~~~~~~~~~~~~~~~~~~~~~~~~~"
				+ historicCounter + "~~~~~~~~~~~~~~~~");
	}

//	private HistoricActivityInstance findHistoricUserTask(
//			ProcessInstance processInstance, String activityId) {
//		HistoricActivityInstance rtnVal = null;
//		// 查询当前流程实例审批结束的历史节点
//		List<HistoricActivityInstance> historicActivityInstances = historyService
//				.createHistoricActivityInstanceQuery().activityType("userTask")
//				.processInstanceId(processInstance.getId())
//				.activityId(activityId).finished()
//				.orderByHistoricActivityInstanceEndTime().desc().list();
//		if (historicActivityInstances.size() > 0) {
//			rtnVal = historicActivityInstances.get(0);
//		}
//
//		return rtnVal;
//	}

	public void aa() {

	}

	/** 查询历史的流程变量 */
	@Test
	public void getHistoryProcessVariables() {
		List<HistoricVariableInstance> list = historyService
				.createHistoricVariableInstanceQuery()// 创建一个历史的流程变量查询
				.variableName("age").list();
		if (list != null && list.size() > 0) {
			for (HistoricVariableInstance hiv : list) {
				System.out.println(hiv.getTaskId() + "  "
						+ hiv.getVariableName() + "     " + hiv.getValue()
						+ "      " + hiv.getVariableTypeName());
			}
		}
	}
}
