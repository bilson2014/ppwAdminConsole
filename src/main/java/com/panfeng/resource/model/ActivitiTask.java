package com.panfeng.resource.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

import com.panfeng.domain.BaseObject;

public class ActivitiTask extends BaseObject {

	private static final long serialVersionUID = 112464574554L;

	private String owner = "";
	private String createTime = null;
	private String id = "";
	private String name = "";
	private String processInstanceId = "";
	private String taskDefinitionKey = "";
	private String description="";
	
	private boolean suspended=false;

	// 每个任务可以拥有自己的预计执行需要用的时间
	private FlowDate scheduledTime=null;


	public ActivitiTask(String owner, String createTime, String id,
			String name, String processInstanceId, String taskDefinitionKey,
			String description, boolean suspended, FlowDate scheduledTime) {
		super();
		this.owner = owner;
		this.createTime = createTime;
		this.id = id;
		this.name = name;
		this.processInstanceId = processInstanceId;
		this.taskDefinitionKey = taskDefinitionKey;
		this.description = description;
		this.suspended = suspended;
		this.scheduledTime = scheduledTime;
	}

	public ActivitiTask() {
		super();
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("owner:");
		stringBuffer.append(this.owner);
		stringBuffer.append("\ncreateTime:");
		stringBuffer.append(this.createTime);
		stringBuffer.append("\nid:");
		stringBuffer.append(this.id);
		stringBuffer.append("\nname:");
		stringBuffer.append(this.name);
		stringBuffer.append("\nprocessInstanceId:");
		stringBuffer.append(this.processInstanceId);
		stringBuffer.append("\ntaskDefinitionKey:");
		stringBuffer.append(this.taskDefinitionKey);
		stringBuffer.append("\nscheduledTime:");
		stringBuffer.append("\ndescription");
		stringBuffer.append(this.description);
		stringBuffer.append(this.scheduledTime == null ? ""
				: this.scheduledTime);
		return stringBuffer.toString();
	}

	public static <T extends HistoricTaskInstance> ActivitiTask TaskToActivitiTask(T task) {
		ActivitiTask activitiTask = new ActivitiTask();
		Date date=task.getEndTime();
		if(date!=null){
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH-ss-mm");
			activitiTask.setCreateTime(simpleDateFormat.format(task.getEndTime()));
		}else{
			activitiTask.setCreateTime("");
		}
		activitiTask.setOwner(task.getAssignee());
		activitiTask.setId(task.getId());
		activitiTask.setName(task.getName());
		activitiTask.setProcessInstanceId(task.getProcessInstanceId());
		activitiTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
		activitiTask.setDescription(task.getDescription());
		activitiTask.setSuspended(false);
		return activitiTask;
	}
	
	public static <T extends Task> ActivitiTask TaskToActivitiTask(T task) {
		ActivitiTask activitiTask = new ActivitiTask();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH-ss-mm");
		String date=simpleDateFormat.format(task.getCreateTime());
		activitiTask.setCreateTime(date);
		activitiTask.setOwner(task.getAssignee());
		activitiTask.setId(task.getId());
		activitiTask.setName(task.getName());
		activitiTask.setProcessInstanceId(task.getProcessInstanceId());
		activitiTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
		activitiTask.setDescription(task.getDescription());
		activitiTask.setSuspended(false);
		return activitiTask;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwner() {
		return owner;
	}
	
	public FlowDate getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(FlowDate scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

}