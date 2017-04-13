package com.panfeng.resource.model;

import java.io.Serializable;
/**
 * 级联“省”
 * @author wang
 *
 */
public class Province implements Serializable {

	private static final long serialVersionUID = -9199573140181925699L;

	private int id; // 唯一ID
	private String provinceID; // 省ID
	private String provinceName;// 省名

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}


}
