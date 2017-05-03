package com.panfeng.resource.model;

import java.util.List;

import com.paipianwang.pat.common.entity.BaseEntity;

public class SynergyList extends BaseEntity{

	private static final long serialVersionUID = -6253604169518281641L;
	
	private long projectId = 0l;
	
	private List<Synergy> list = null;

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public List<Synergy> getList() {
		return list;
	}

	public void setList(List<Synergy> list) {
		this.list = list;
	}
	
}
