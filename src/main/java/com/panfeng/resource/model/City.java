package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;

/**
 * 级联“市”
 * 
 * @author wang
 *
 */
public class City extends BaseEntity {

	private static final long serialVersionUID = 7933928369958873968L;

	private String city;// 市名
	private String cityID;// 市ID
	private String father;// 父级ID（省ID）

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

}
