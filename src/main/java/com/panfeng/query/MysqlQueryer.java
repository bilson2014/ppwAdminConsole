package com.panfeng.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.panfeng.domain.DataSource;

/**
 * Mysql 数据库查询方案
 * @author Jack
 *
 */
public class MysqlQueryer extends AbstractQueryer implements Queryer {

	public MysqlQueryer(DataSource ds) {
		super(ds);
	}

	@Override
	protected Connection getJdbcConnection() {
		try {
			return DriverManager.getConnection(ds.getJdbcUrl(), ds.getUser(), ds.getPassword());
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	protected String preprocessSqlText(String sqlText){
		sqlText = StringUtils.stripEnd(sqlText.trim(), ";");
		Pattern pattern = Pattern.compile("limit.*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sqlText);
		if (matcher.find()) {
		    sqlText = matcher.replaceFirst("");
		}
		return sqlText + " limit 1";
	}

}
