package com.panfeng.query;

import java.sql.Connection;

import com.panfeng.domain.DataSource;

/**
 * SqlLite查询方案
 * @author Jack
 *
 */
public class SQLiteQueryer extends AbstractQueryer implements Queryer {

	public SQLiteQueryer(DataSource ds) {
		super(ds);
	}

	@Override
	protected Connection getJdbcConnection() {
		
		return null;
	}

}
