package com.panfeng.query;

import java.sql.Connection;

import com.panfeng.domain.DataSource;

public class HBaseQueryer extends AbstractQueryer implements Queryer {

	public HBaseQueryer(DataSource ds) {
		super(ds);
	}

	@Override
	protected Connection getJdbcConnection() {
		// TODO Auto-generated method stub
		return null;
	}

}
