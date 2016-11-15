package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;
/**
 * 产品模块化
 */
public class ProductModule extends BaseObject {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer pid;
	private String moduleName;
	private String description;
	private String modulePrice;
	private String pic;
	private String createTime;
	private String updateTime;
	private Integer sortIndex;
	//提供给combotree的冗余
	private String text;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getModulePrice() {
		return modulePrice;
	}
	public void setModulePrice(String modulePrice) {
		this.modulePrice = modulePrice;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
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
}
