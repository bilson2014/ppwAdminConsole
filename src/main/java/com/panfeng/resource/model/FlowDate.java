package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;

public class FlowDate extends BaseEntity {

	private static final long serialVersionUID = 2532186253061927606L;
	private long fdId;
	private String fdFlowId;
	private String fdStartTime;
	private String fdTaskId;

	public long getFdId() {
		return fdId;
	}

	public void setFdId(long fdId) {
		this.fdId = fdId;
	}

	public String getFdFlowId() {
		return fdFlowId;
	}

	public void setFdFlowId(String fdFlowId) {
		this.fdFlowId = fdFlowId;
	}

	public String getFdStartTime() {
		return fdStartTime;
	}

	public void setFdStartTime(String fdStartTime) {
		this.fdStartTime = fdStartTime;
	}

	public String getFdTaskId() {
		return fdTaskId;
	}

	public void setFdTaskId(String fdTaskId) {
		this.fdTaskId = fdTaskId;
	}

	public FlowDate() {
		super();
	}

	public FlowDate(long fdId, String fdFlowId, String fdStartTime,
			String fdTaskId) {
		super();
		this.fdId = fdId;
		this.fdFlowId = fdFlowId;
		this.fdStartTime = fdStartTime;
		this.fdTaskId = fdTaskId;
	}
}
