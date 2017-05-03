package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;

/**
 * 业务字典
 */
public class BizBean extends BaseEntity {

	private static final long serialVersionUID = 2072179123234736135L;

	private String name = null; // 内容
	
	private String type = null; // 业务组

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
