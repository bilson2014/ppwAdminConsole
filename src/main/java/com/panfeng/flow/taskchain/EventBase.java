package com.panfeng.flow.taskchain;

import com.paipianwang.pat.common.entity.SessionInfo;
import com.panfeng.resource.model.NodesEvent;

/**
 * 任务节点父类
 * 
 * @author wang
 *
 */
public abstract class EventBase {

	private Long Id;
	private String Name;
	private String Description;

	private int model; // 任务模式，自动/手动

	// 检测链条当前步骤状态
	public abstract TaskStatus checkStatus();

	/**
	 * 执行相关任务,如果是自动完成任务请使用异步发起
	 */
	public abstract void execute(NodesEvent autoEvent, SessionInfo sessionInfo, String processId);

	// 关闭任务
	public void closeTask() {
	};

	// 获取任务结果（特殊任务）
	public <T> T getResult() {
		return null;
	};

	// 获取任务节点相关信息
	public <T> T getInfo() {
		return null;
	};

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

}
