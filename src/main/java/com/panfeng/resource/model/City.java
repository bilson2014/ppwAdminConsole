package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

/**
 * 级联“市”
 * 
 * @author wang
 *
 */
public class City extends BaseObject {

	private static final long serialVersionUID = 7933928369958873968L;

	private int id; // 唯一ID
	private String city;// 市名
	private String cityID;// 市ID
	private String father;// 父级ID（省ID）

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
