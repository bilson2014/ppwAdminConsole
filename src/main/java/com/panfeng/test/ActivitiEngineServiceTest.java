package com.panfeng.test;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.service.ActivitiEngineService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class ActivitiEngineServiceTest {
	@Autowired
	private RuntimeService runtimeService;

//	@Autowired
//	private final RightDao dao = null;
	
	@Autowired
	ActivitiEngineService aes;

	public static String processDefinitionKey = "IndentFlow";
	public static String processInstanceBusinessKey = "wang1";

	@Test
	public void create() {
		String b = aes.startProcess(processDefinitionKey, processInstanceBusinessKey);
		System.out.println(b);
	}

	@Test
	public void completeTask() {
		boolean b = aes.completeTask(processDefinitionKey, processInstanceBusinessKey, "2501");
		System.out.println(b);
	}

	@Test
	public void tttt() {

		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(processInstanceBusinessKey)
				.list();

		for (ProcessInstance processInstance : list) {
			System.out.println(processInstance.getName());
			System.out.println(processInstance.getId());
		}
	}

	@Test
	public void suspendProcess() {
		aes.suspendProcess(processDefinitionKey, processInstanceBusinessKey, "");
	}

	@Test
	public void resumeProcess() {
		aes.resumeProcess(processDefinitionKey, processInstanceBusinessKey, "");
	}

	@Test
	public void removeProcess() {
		aes.removeProcess(processDefinitionKey, processInstanceBusinessKey, "");
	}

	@Test
	public void jumpTask() {
		System.out.println(aes.jumpTask(processDefinitionKey, processInstanceBusinessKey, "fa", "2501"));
	}

	@Test
	public void getNodes() {
		List<ActivityImpl> list = aes.getNodes(processDefinitionKey, processInstanceBusinessKey, "2501");
		for (ActivityImpl activityImpl : list) {
			System.out.println(activityImpl.getId());
			System.out.println(activityImpl.getProperty("name"));
			TaskDefinition td = (TaskDefinition) activityImpl.getProperty("taskDefinition");
			if (td != null)
				System.out.println(
						td.getAssigneeExpression() == null ? "" : td.getAssigneeExpression().getExpressionText());
		}
	}

	@Test
	public void getCurrentTask() {
		Task task = aes.getCurrentTask(processDefinitionKey, processInstanceBusinessKey, "2501");
		System.out.println(task.getName());
		System.out.println(task.getId());
		System.out.println(task.getTaskDefinitionKey());
	}

	@Test
	public void getHistoryProcess() {
		HistoricProcessInstance hpi = aes.getHistoryProcess(processDefinitionKey, processInstanceBusinessKey, "");
		System.out.println(hpi.getStartTime());
		System.out.println(hpi.getEndTime());
	}

	@Test
	public void getHistoryProcessTask() {
		List<HistoricTaskInstance> list = aes.getHistoryProcessTask(processDefinitionKey, processInstanceBusinessKey,
				"2501");

		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println(historicTaskInstance.getAssignee());
			System.out.println(historicTaskInstance.getStartTime());
			System.out.println(historicTaskInstance.getName());
			System.out.println(historicTaskInstance.getTaskDefinitionKey());
		}
	}

}
