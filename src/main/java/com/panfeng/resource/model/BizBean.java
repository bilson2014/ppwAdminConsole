package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

/**
 * 业务字典
 */
public class BizBean extends BaseObject {

	private static final long serialVersionUID = 2072179123234736135L;

	private long id = 0l; // 唯一ID
	
	private String name = null; // 内容
	
	private String type = null; // 业务组

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
