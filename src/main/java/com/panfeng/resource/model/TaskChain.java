package com.panfeng.resource.model;

import java.util.List;

import com.paipianwang.pat.common.entity.BaseEntity;

/**
 * 流程节点绑定的任务链
 * 
 * @author wang
 *
 */
public class TaskChain extends BaseEntity {

	private static final long serialVersionUID = 9122997952274211200L;

	private Long taskChainId;
	private String name;
	private String description;
	private List<NodesEvent> nodesEvents;

	
	public List<NodesEvent> getNodesEvents() {
		return nodesEvents;
	}

	public void setNodesEvents(List<NodesEvent> nodesEvents) {
		this.nodesEvents = nodesEvents;
	}

	public Long getTaskChainId() {
		return taskChainId;
	}

	public void setTaskChainId(Long taskChainId) {
		this.taskChainId = taskChainId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
