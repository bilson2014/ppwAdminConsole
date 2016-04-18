package com.panfeng.resource.view;

import com.panfeng.domain.BaseObject;

public class PageFilter extends BaseObject {

	private static final long serialVersionUID = -2068769820893993360L;
	private int page;// 当前页
	private int rows;// 每页显示记录数

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}
