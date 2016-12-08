package com.panfeng.domain;

/**
 * 数据源
 * @author Jack
 *
 */
public class DataSource extends BaseObject {

	private static final long serialVersionUID = 4566138888831176286L;

	private String driverClass; // 驱动类
	
	private String jdbcUrl; // 数据源连接字符串
	
	private String user; // 用户名
	
	private String password; // 密码

	public DataSource() {
		
	}

	public DataSource(String jdbcUrl, String user, String password) {
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
	}
	
	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
