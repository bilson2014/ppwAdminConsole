package com.panfeng.resource.model;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.panfeng.flow.core.Bpmn.NodeType;

public class FlowNode implements Serializable {

	private static final long serialVersionUID = -8877099356358266869L;

	private String id = "";
	private String name = "";
	private NodeType nodeType;
	private Integer index;

	// --------------options--------------
	private Long taskChainId = null; // 事件链Id
	private boolean allowSkip; // 是否允许跳过
	private String assignee = "";// 代理人
	private String description = "";

	// ----------------冗余字段----------------
	private String events = "";

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof FlowNode) {
			FlowNode fn = (FlowNode) obj;
			if (this.id.equals(fn.id)) {
				if (this.name.equals(fn.name)) {
					if (this.index == fn.index) {
						if (this.taskChainId == fn.taskChainId) {
							if (this.allowSkip == fn.allowSkip) {
								if (this.assignee.equals(fn.assignee)) {
									if (this.description.equals(fn.description))
										return true;
								}

							}
						}
					}
				}
			}
		}
		return false;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
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

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Long getTaskChainId() {
		return taskChainId;
	}

	public void setTaskChainId(Long taskChainId) {
		this.taskChainId = taskChainId;
	}

	public boolean isAllowSkip() {
		return allowSkip;
	}

	public void setAllowSkip(boolean allowSkip) {
		this.allowSkip = allowSkip;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	// -------------------tools ---------------------

	public String getflowOptionsToJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("taskChainId", this.taskChainId);
		jsonObject.addProperty("assignee", this.assignee);
		jsonObject.addProperty("allowSkip", this.allowSkip);
		jsonObject.addProperty("description", this.description);
		return jsonObject.toString();
	}

	public Long[] getEventIds() {
		Long[] ids = null;
		if (ValidateUtil.isValid(this.events)) {
			String[] array = this.events.split("\\,");
			ids = new Long[array.length];
			for (int i = 0; i < array.length; i++) {
				ids[i] = Long.parseLong(array[i]);
			}
		}
		return ids;
	}

	public void setflowOptionst(String json) {
		if (ValidateUtil.isValid(json)) {
			try {
				Gson gson = new Gson();
				FlowNode flowNode = gson.fromJson(json, FlowNode.class);
				this.allowSkip = flowNode.allowSkip;
				this.assignee = flowNode.assignee;
				this.taskChainId = flowNode.taskChainId;
				this.description = flowNode.description;
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
		}
	}

}
