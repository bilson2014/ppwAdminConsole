package com.panfeng.resource.view;

import java.io.Serializable;
import java.util.List;

public class DataGrid<T> implements Serializable {

	private static final long serialVersionUID = -8488550127239105293L;

	private long total = 0l;
	
	private List<T> rows = null;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
