package com.panfeng.resource.model;

import java.util.List;

import com.panfeng.domain.BaseObject;

public class FlowTemplate extends BaseObject {

	private static final long serialVersionUID = 7598528321076177988L;

	private String id = "";
	private String name = "";
	private List<FlowNode> flowNodes = null;

	// ----------------- db 映射 ----------------------

	private String d_id;
	private String d_key;
	private String d_deployment_id;

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof FlowTemplate) {
			FlowTemplate ft = (FlowTemplate) obj;
			if(this.id.equals(ft.getId())){
				if(this.name.equals(ft.getName())){
					if(flowNodes.equals(ft.getFlowNodes()))
						return true;
				}
			}
		}
		return false;
	}

	public String getD_id() {
		return d_id;
	}

	public void setD_id(String d_id) {
		this.d_id = d_id;
	}

	public String getD_key() {
		return d_key;
	}

	public void setD_key(String d_key) {
		this.d_key = d_key;
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

	public List<FlowNode> getFlowNodes() {
		return flowNodes;
	}

	public void setFlowNodes(List<FlowNode> flowNodes) {
		this.flowNodes = flowNodes;
	}

	public String getD_deployment_id() {
		return d_deployment_id;
	}

	public void setD_deployment_id(String d_deployment_id) {
		this.d_deployment_id = d_deployment_id;
	}
}
