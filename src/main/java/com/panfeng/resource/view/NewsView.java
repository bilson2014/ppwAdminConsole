package com.panfeng.resource.view;

public class NewsView extends Pagination {

	private static final long serialVersionUID = 4923161285696056L;

	private Integer category;

	private String tags = null;
	
	private Boolean status;// 是否显示到首页 1显示 0不显示，默认不显示
	
	private Integer recommend = null;
	
	private Integer visible = null; // 是否可见 0 可见 1不可见 , 默认可见

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}
	
}
