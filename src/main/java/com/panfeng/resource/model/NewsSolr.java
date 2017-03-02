package com.panfeng.resource.model;

import org.apache.solr.client.solrj.beans.Field;

import com.panfeng.domain.BaseObject;

/**
 * 新闻solr存储实体
 * @author Jack
 *
 */
public class NewsSolr extends BaseObject {

	private static final long serialVersionUID = 5263692798871248537L;

	@Field
	private String id = null; // 唯一编号
	
	@Field
	private String title = null; // 标题
	
	@Field
	private String discription = null; // 描述
	
	@Field
	private boolean status = false; // 是否显示在首页
	
	@Field
	private String creationTime = null; // 创建时间
	
	@Field
	private Integer sort_index = 0; // 排序
	
	@Field
	private String tags = null; // 标签
	
	@Field
	private String picLDUrl = null; // 图片链接
	
	@Field
	private long total = 0l; // 总数

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public Integer getSort_index() {
		return sort_index;
	}

	public void setSort_index(Integer sort_index) {
		this.sort_index = sort_index;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getPicLDUrl() {
		return picLDUrl;
	}

	public void setPicLDUrl(String picLDUrl) {
		this.picLDUrl = picLDUrl;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
}
