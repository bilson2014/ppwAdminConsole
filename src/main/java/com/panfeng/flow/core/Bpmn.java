package com.panfeng.flow.core;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang.StringUtils;

import com.panfeng.resource.model.FlowNode;

/**
 * activiti 框架动态创建流程节点工具类
 * 
 * @author wang
 *
 */
public class Bpmn {

	/**
	 * 任务节点 ,附加信息已JSON字符串嵌入到流程数据表中 {@link FlowNode}
	 * 
	 * @param id
	 * @param name
	 * @param documentation
	 * @return
	 */
	protected static UserTask createUserTask(String id, String name, String documentation) {
		UserTask userTask = new UserTask();
		userTask.setId(id);
		userTask.setName(name);
		userTask.setDocumentation(documentation);
		return userTask;
	}

	/**
	 * 连线
	 */
	protected static SequenceFlow createSequenceFlow(String from, String to, String name, String conditionExpression) {
		SequenceFlow flow = new SequenceFlow();
		flow.setSourceRef(from);
		flow.setTargetRef(to);
		flow.setName(name);
		if (StringUtils.isNotEmpty(conditionExpression)) {
			flow.setConditionExpression(conditionExpression);
		}
		return flow;
	}

	/**
	 * 排他网关
	 */
	protected static ExclusiveGateway createExclusiveGateway(String id) {
		ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setId(id);
		return exclusiveGateway;
	}

	/**
	 * 开始节点,
	 * 
	 * @param documentation
	 * @return
	 */
	protected static StartEvent createStartEvent(String documentation) {
		StartEvent startEvent = new StartEvent();
		startEvent.setId("startEvent");
		startEvent.setName("Start");
		startEvent.setDocumentation(documentation);
		return startEvent;
	}

	/**
	 * 结束节点
	 * 
	 * @param documentation
	 * @return
	 */
	protected static EndEvent createEndEvent(String documentation) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId("endEvent");
		endEvent.setName("End");
		endEvent.setDocumentation(documentation);
		return endEvent;
	}

	public enum NodeType {
		START, END, USERTASK, SEQUENCE, EXCLUSIVEGATEWAY;
		@Override
		public String toString() {
			return this.name();
		}
	}
}
