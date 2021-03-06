package com.panfeng.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panfeng.domain.DataSource;
import com.panfeng.resource.model.MetaDataColumn;

/**
 * Queryer 基础类
 * @author Jack
 *
 */
public abstract class AbstractQueryer {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected DataSource ds = null;
	
	public AbstractQueryer (final DataSource ds) {
		this.ds = ds;
	}
	
	/**
	 * 解析元数据
	 * @param sqlText
	 * @return
	 */
	public List<MetaDataColumn> parseMetaDataColumns(final String sqlText) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<MetaDataColumn> columns = null;
		
		try {
			logger.debug(sqlText);
			conn = this.getJdbcConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(this.preprocessSqlText(sqlText));
			// 获取元数据
			final ResultSetMetaData rsMetaData = rs.getMetaData();
			// 获取元数据列
			final int count = rsMetaData.getColumnCount();
			columns = new ArrayList<MetaDataColumn>();
			
			// 遍历元数据，然后封装
			for(int i = 1; i <= count;i ++) {
				final MetaDataColumn column = new MetaDataColumn();
				column.setName(rsMetaData.getColumnLabel(i));
				column.setDataType(rsMetaData.getColumnTypeName(i));
				column.setWidth(rsMetaData.getColumnDisplaySize(i));
				
				columns.add(column);
			}
			
		} catch (SQLException ex) {
			logger.error(ex.getMessage());
			throw new RuntimeException("获取元数据出错", ex);
		} finally {
			releaseJdbcResource(conn, stmt, rs);
		}
		
		return columns == null ? new ArrayList<MetaDataColumn>(0) : columns;
	}

	/**
	 * 预处理获取列表集合的sql语句
	 * 可以处理全表查询的SQL，从而避免过多的内存消耗,避免性能问题的出现
	 * @param sqlText SQL语句
	 * @return
	 */
	protected String preprocessSqlText(final String sqlText) {
		return sqlText;
	}

	// 获取jdbc连接
	protected abstract Connection getJdbcConnection();
	
	// 释放数据库资源
	protected void releaseJdbcResource(final Connection conn, final Statement stmt, final ResultSet rs) {
		try {
			if(conn != null) {
				conn.close();
			}
			
			if(stmt != null) {
				stmt.close();
			}
			
			if(rs != null) {
				rs.close();
			}
		} catch (Exception ex) {
			throw new RuntimeException("数据库资源释放异常", ex);
		} 
		
	}
}
