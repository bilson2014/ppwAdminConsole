package com.panfeng.flow.core;

import com.paipianwang.pat.common.entity.BaseEntity;
import com.panfeng.resource.model.FlowNode;

/**
 * 流程实例，公共属性
 * 
 * @author wang
 *
 */
public class BaseFlowModel extends BaseEntity {

	private static final long serialVersionUID = -5438515831300783189L;
	private String flowId = "";
	private String flowTemplateId = "";
	private String flowTemplateName = "";
	private String flowTemplateVersionId = "";
	private String createTime = "";
	private int status;

	private FlowNode currentTask = null;

	public static int RUNING = 0;
	public static int SUSPEND = 1;
	public static int FINISH = 2;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowTemplateId() {
		return flowTemplateId;
	}

	public void setFlowTemplateId(String flowTemplateId) {
		this.flowTemplateId = flowTemplateId;
	}

	public String getFlowTemplateName() {
		return flowTemplateName;
	}

	public FlowNode getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(FlowNode currentTask) {
		this.currentTask = currentTask;
	}

	public void setFlowTemplateName(String flowTemplateName) {
		this.flowTemplateName = flowTemplateName;
	}

	public String getFlowTemplateVersionId() {
		return flowTemplateVersionId;
	}

	public void setFlowTemplateVersionId(String flowTemplateVersionId) {
		this.flowTemplateVersionId = flowTemplateVersionId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
