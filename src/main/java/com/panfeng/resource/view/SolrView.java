package com.panfeng.resource.view;

public class SolrView extends Pagination{

	private static final long serialVersionUID = -3365175562854271223L;

	private String priceFq = null; // 价格区间筛选
	
	private String lengthFq = null; // 时长区间筛选
	
	private String itemFq = null; // 视频类型
	
	private String tagsFq = null; // 标签类型
	
	private String condition = null; // 搜索条件
	
	private int sortord = 0; // 0：升序 1：降序
	
	private String sequence = null; // 排序字段
	
	private String industry = null; // 行业
	
	private String genre = null; // 类型
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getPriceFq() {
		return priceFq;
	}

	public void setPriceFq(String priceFq) {
		this.priceFq = priceFq;
	}

	public String getLengthFq() {
		return lengthFq;
	}

	public void setLengthFq(String lengthFq) {
		this.lengthFq = lengthFq;
	}

	public String getItemFq() {
		return itemFq;
	}

	public void setItemFq(String itemFq) {
		this.itemFq = itemFq;
	}

	public int getSortord() {
		return sortord;
	}

	public void setSortord(int sortord) {
		this.sortord = sortord;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getTagsFq() {
		return tagsFq;
	}

	public void setTagsFq(String tagsFq) {
		this.tagsFq = tagsFq;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

}
