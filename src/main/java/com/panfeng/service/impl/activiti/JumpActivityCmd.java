package com.panfeng.service.impl.activiti;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;

/**
 * activiti 节点跳转实现
 * activiti 是基于命令模式实现的
 * @author wang
 *
 */
public class JumpActivityCmd implements Command<Object> {
	private String activityId;
	private String processInstanceId;
	private String jumpOrigin;

	public JumpActivityCmd(String processInstanceId, String activityId) {
		this(processInstanceId, activityId, "jump");
	}

	public JumpActivityCmd(String processInstanceId, String activityId, String jumpOrigin) {
		this.activityId = activityId;
		this.processInstanceId = processInstanceId;
		this.jumpOrigin = jumpOrigin;
	}

	public Object execute(CommandContext commandContext) {
		ExecutionEntity executionEntity = commandContext.getExecutionEntityManager()
				.findExecutionById(processInstanceId);
		executionEntity.destroyScope(jumpOrigin);
		ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
		ActivityImpl activity = processDefinition.findActivity(activityId);
		executionEntity.executeActivity(activity);
		return executionEntity;
	}
}