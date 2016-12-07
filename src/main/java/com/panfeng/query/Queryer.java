package com.panfeng.query;

import java.util.List;

import com.panfeng.resource.model.MetaDataColumn;

public interface Queryer {

	/**
	 * 从SQL语句中解析出报表的元数据列集合
	 * 
	 * @param sqlText SQL语句
	 * @return 元数据列表
	 */
	public List<MetaDataColumn> parseMetaDataColumns(final String sqlText);
	
}
