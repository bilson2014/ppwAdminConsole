package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class Item extends BaseObject{

	private static final long serialVersionUID = 1922925247835036537L;

	private Long itemId = 0l;
	
	private String itemName = null;
	
	private String itemDescription = null; 
	
	private String creatDate = null;
	
	private int od;
	
	private int isActive = 0; // 0:不是活动分类  1:活动分类

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}

	public int getOd() {
		return od;
	}

	public void setOd(int od) {
		this.od = od;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
}
