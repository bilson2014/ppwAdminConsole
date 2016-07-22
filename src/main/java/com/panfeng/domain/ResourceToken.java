package com.panfeng.domain;

public class ResourceToken extends BaseObject {

	private static final long serialVersionUID = 454898756731549497L;

	public boolean flag = false; // 状态位
	
	public String solrUrl = GlobalConstant.SOLR_URL; // 搜索core地址

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getSolrUrl() {
		return solrUrl;
	}

	public void setSolrUrl(String solrUrl) {
		this.solrUrl = solrUrl;
	}
	
}
