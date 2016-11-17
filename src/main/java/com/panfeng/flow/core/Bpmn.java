package com.panfeng.flow.core;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang.StringUtils;

public class Bpmn {

	/* 任务节点 */
	protected static UserTask createUserTask(String id, String name, String documentation) {
		UserTask userTask = new UserTask();
		userTask.setId(id);
		userTask.setName(name);
		userTask.setDocumentation(documentation);
		return userTask;
	}

	/* 连线 */
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

	/* 排他网关 */
	protected static ExclusiveGateway createExclusiveGateway(String id) {
		ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setId(id);
		return exclusiveGateway;
	}

	/* 开始节点 */
	protected static StartEvent createStartEvent() {
		StartEvent startEvent = new StartEvent();
		startEvent.setId("startEvent");
		startEvent.setName("Start");
		return startEvent;
	}

	/* 结束节点 */
	protected static EndEvent createEndEvent() {
		EndEvent endEvent = new EndEvent();
		endEvent.setId("endEvent");
		endEvent.setName("End");
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
