package com.panfeng.query;

import org.apache.commons.lang.StringUtils;

import com.panfeng.domain.DataSource;

/**
 * jdbc工厂模式，自动匹配数据源
 * @author Jack
 *
 */
public class QueryFactory {

	public static Queryer create(final String jdbcUrl, final String user, final String password) {
		final DataSource ds = new DataSource(jdbcUrl, user, password);
		return create(ds);
	}

	private static Queryer create(final DataSource ds) {
		if(ds != null) {
			final String jdbcUrl = ds.getJdbcUrl();
			
			// mysql
			if(StringUtils.containsIgnoreCase(jdbcUrl, "jdbc:mysql")) {
				return new MysqlQueryer(ds);
			}
			
			// oracle
			if(StringUtils.containsIgnoreCase(jdbcUrl, "jdbc:oracle")) {
				return new OracleQueryer(ds);
			}
			
			// sqlite
			if(StringUtils.containsIgnoreCase(jdbcUrl, "jdbc:sqlite")) {
				return new SQLiteQueryer(ds);
			}
			
			// phoenix
			if(StringUtils.containsIgnoreCase(ds.getJdbcUrl(), "jdbc:phoenix")) {
				return new HBaseQueryer(ds);
			}
		}
		return null;
	}
}
