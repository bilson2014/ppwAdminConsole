package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class TaskChainNodesEventLink extends BaseObject {

	private static final long serialVersionUID = -7562343427437667262L;

	private Long taskChainNodesEventLinkId;
	private Long taskChainId;
	private Long nodeEventId;

	public Long getTaskChainNodesEventLinkId() {
		return taskChainNodesEventLinkId;
	}

	public TaskChainNodesEventLink() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setTaskChainNodesEventLinkId(Long taskChainNodesEventLinkId) {
		this.taskChainNodesEventLinkId = taskChainNodesEventLinkId;
	}

	public Long getTaskChainId() {
		return taskChainId;
	}

	public void setTaskChainId(Long taskChainId) {
		this.taskChainId = taskChainId;
	}

	public Long getNodeEventId() {
		return nodeEventId;
	}

	public void setNodeEventId(Long nodeEventId) {
		this.nodeEventId = nodeEventId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof TaskChainNodesEventLink) {
				if (((TaskChainNodesEventLink) obj).getTaskChainId().equals(this.taskChainId)
						&& ((TaskChainNodesEventLink) obj).getNodeEventId().equals(this.nodeEventId)) {
					return true;
				}
			}
		}
		return false;
	}

	public TaskChainNodesEventLink(Long taskChainId, Long nodeEventId) {
		super();
		this.taskChainId = taskChainId;
		this.nodeEventId = nodeEventId;
	}

}
