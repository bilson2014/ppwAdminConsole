package com.panfeng.resource.view;

import com.paipianwang.pat.common.entity.BaseEntity;

public class CurrentCustomer extends BaseEntity {

	private static final long serialVersionUID = -6017065228436366631L;

	private String field = null;
	
	private String value = null;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
