package com.panfeng.resource.model;

import java.util.LinkedList;

import com.panfeng.domain.BaseObject;
import com.panfeng.persist.FlowCoreMapper;

/**
 * 自定义流程实体，对应引擎流程实例的简化版本 flowNodes 为自定义流程节点{@link FlowNode},<br>
 * 将流程
 * 
 * @author wang
 *
 */
public class FlowTemplate extends BaseObject {

	private static final long serialVersionUID = 7598528321076177988L;
	/**
	 * 流程ID
	 */
	private String id = "";
	/**
	 * 流程名
	 */
	private String name = "";
	/**
	 * 流程节点
	 */
	private LinkedList<FlowNode> flowNodes = null;
	// ----------------- db 映射 ----------------------
	/**
	 * 好奇怪这些玩应哪来的？ <br>
	 * {@link FlowCoreMapper}
	 * SELECT arp.ID_ AS d_id,<br>
	 * arp.KEY_ AS d_key,<br>
	 * arp.KEY_ AS id,<br>
	 * arp.NAME_ AS name,<br>
	 * arp.DEPLOYMENT_ID_ AS d_deployment_id FROM<br>
	 * ACT_RE_PROCDEF arp;
	 * 用户查询所有引擎建立的模板
	 */
	private String d_id;
	private String d_key;
	private String d_deployment_id;

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof FlowTemplate) {
			FlowTemplate ft = (FlowTemplate) obj;
			if (this.id.equals(ft.getId())) {
				if (this.name.equals(ft.getName())) {
					if (flowNodes.equals(ft.getFlowNodes()))
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

	public LinkedList<FlowNode> getFlowNodes() {
		return flowNodes;
	}

	public void setFlowNodes(LinkedList<FlowNode> flowNodes) {
		this.flowNodes = flowNodes;
	}

	public String getD_deployment_id() {
		return d_deployment_id;
	}

	public void setD_deployment_id(String d_deployment_id) {
		this.d_deployment_id = d_deployment_id;
	}
}
