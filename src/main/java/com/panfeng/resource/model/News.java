package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class News extends BaseObject {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String title;
	private String discription;
	private String content;
	private String createTime;
	private String updateTime;
	private Integer sortIndex;
	private Boolean status;// 是否显示到首页

	private String tags;
	private String picLDUrl;
	private Integer recommend = 0;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	private boolean next = false;
	private boolean prev = false;

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

}
