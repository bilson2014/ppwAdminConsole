package com.panfeng.resource.view;

import java.io.Serializable;

public class Pagination implements Serializable {

	private static final long serialVersionUID = -4473519214881594462L;

	private long begin = 0l; 
	
	private long limit = 10l;
	
	private String order = null; // 排序规则
	
	private String sort = null; // 排序字段
	

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

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
