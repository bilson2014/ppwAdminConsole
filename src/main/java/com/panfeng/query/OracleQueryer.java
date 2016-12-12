package com.panfeng.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.panfeng.domain.DataSource;

/**
 * Oracle数据库查询方案
 * @author Jack
 *
 */
public class OracleQueryer extends AbstractQueryer implements Queryer {

	public OracleQueryer(DataSource ds) {
		super(ds);
	}

	@Override
	protected Connection getJdbcConnection() {
		try {
			return DriverManager.getConnection(this.ds.getJdbcUrl(), this.ds.getUser(), this.ds.getPassword());
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}	
	}

}
