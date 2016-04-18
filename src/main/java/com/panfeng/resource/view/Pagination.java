package com.panfeng.resource.view;

import com.panfeng.domain.BaseObject;

public class Pagination extends BaseObject {

	private static final long serialVersionUID = -4473519214881594462L;

	private long begin = 0l; 
	
	private long limit = 10l;

	public long getBegin() {
		return begin;
	}

	public void setBegin(long begin) {
		this.begin = begin;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}
	
}
