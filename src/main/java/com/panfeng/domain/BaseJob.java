package com.panfeng.domain;

import java.io.Serializable;

import org.quartz.Job;
import org.quartz.TriggerKey;
import org.quartz.Trigger.TriggerState;

public class BaseJob implements Serializable {

	private static final long serialVersionUID = -6150974583596910225L;

	private String jobName;
	private String jobGroup;
	private Class<? extends Job> jobClass; // 执行任务的class
	private String cronExpression; // 时间表达式
	private TriggerState triggerState; // 任务状态
	private Object param; // 传递给任务的参数

	public static String ACTIVITY = "activity"; // 分组名
	//public static String PARAMKEY = "paramkey"; // 传递参数的KEY

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public Class<? extends Job> getJobClass() {
		return jobClass;
	}

	public void setJobClass(Class<? extends Job> jobClass) {
		this.jobClass = jobClass;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public TriggerState getTriggerState() {
		return triggerState;
	}

	public void setTriggerState(TriggerState triggerState) {
		this.triggerState = triggerState;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public TriggerKey getTriggerKey() {
		// 如果jobGroup 是空则会默认用DEFAULT分组
		TriggerKey triggerKey = TriggerKey.triggerKey(this.jobName+"_trigger", this.jobGroup);
		return triggerKey;
	}
}
