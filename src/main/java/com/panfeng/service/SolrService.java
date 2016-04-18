package com.panfeng.service;

import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;

import com.panfeng.domain.BaseObject;
import com.panfeng.resource.model.Solr;

public interface SolrService {

	/**
	 * 添加document
	 * @param solrUrl solr服务地址
	 * @param docs 文档集合
	 */
	public long addDocs(final String solrUrl,final Collection<? extends BaseObject> docs);
	
	/**
	 * 删除 document
	 * @param solrUrl solr服务地址
	 * @param ids 主键集合
	 */
	public long delDocs(final String solrUrl,final List<String> ids);
	
	/**
	 * 查询文档
	 * @param solrUrl solr服务地址
	 * @param query 条件
	 * @return 符合条件的 集合
	 */
	public List<Solr> queryDocs(final String solrUrl,final SolrQuery query);
	
	public void deleteDoc(final long productId,final String solrUrl);
	
	public List<String> suggestDocs(final String solrUrl,final SolrQuery query);
}
