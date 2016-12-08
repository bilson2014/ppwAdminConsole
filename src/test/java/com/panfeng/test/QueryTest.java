package com.panfeng.test;

import java.util.List;

import org.junit.Test;

import com.panfeng.query.QueryFactory;
import com.panfeng.query.Queryer;
import com.panfeng.resource.model.MetaDataColumn;

public class QueryTest {

	@Test
	public void test() {
		Queryer queryer = QueryFactory.create("jdbc:mysql://123.59.75.93:3306/pat?characterEncoding=utf-8", "pat", "PfYw@4now");
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT PRODUCTNAME AS '作品名称', FLAG AS '状态' ");
		sb.append(" FROM PRODUCT");
		List<MetaDataColumn> list = queryer.parseMetaDataColumns(sb.toString());
		for (final MetaDataColumn metaDataColumn : list) {
			System.err.println(metaDataColumn.getName() + " : " + metaDataColumn.getText());
		}
	}
}
