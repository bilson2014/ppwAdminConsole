package com.panfeng.test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class ActivitiTest2 {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	String processDefinitionKey = "test2";// 流程定义的key,也就是bpmn中存在的ID

	@Test
	public void test() {
		Map<String, Object> map = new HashMap<>();
		map.put("age", 22);
		map.put("是否都比", "是的!!");
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(
				processDefinitionKey, "xxx", map);// //按照流程定义的key启动流程实例
		System.out.println("流程实例ID：" + pi.getId());// 流程实例ID：101
		System.out.println("流程实例ID：" + pi.getProcessInstanceId());// 流程实例ID：101
		System.out.println("流程实例ID:" + pi.getProcessDefinitionId());// myMyHelloWorld:1:4
	}

	@Test
	public void getTask() {
		Task t = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("xxx").active().singleResult();
//		taskService.addComment(t.getId(), t.getProcessInstanceId(), "我是一个小评论1");
		List<Comment> comments = taskService.getTaskComments(t.getId());
		for (Comment comment : comments) {
			System.out.println(comment.getFullMessage());
			System.out.println(comment.getUserId());
			System.out.println(comment.getTime());
		}
	}

	@Test
	public void nextTask() {
		Task t = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("xxx").active().singleResult();
		System.out.println(t.getName());
		System.out.println(t.getId());
		taskService.complete(t.getId());
		t = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("xxx").active().singleResult();
		System.out.println(t.getName());
		System.out.println(t.getId());
	}

	@Test
	public void addComment() throws FileNotFoundException {
		Task t = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("xxx").active().singleResult();
		taskService.addComment(t.getId(), t.getProcessInstanceId(), "slt",
				"testxxxx");
	}

	@Test
	public void getComment() {
		Task t = taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.processInstanceBusinessKey("xxx").active().singleResult();
		List<Comment> list = taskService.getTaskComments(t.getId(),"slt");
		for (Comment comment : list) {
			System.out.println(comment.getFullMessage());
			System.out.println(comment.getType());
			System.out.println(comment.getId());
			System.out.println(comment.getTime());
			System.out.println(comment.getUserId());
		}
	}
	@Test
	public void saveFile(){
	}

}
