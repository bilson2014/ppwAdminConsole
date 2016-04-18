package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class Staff extends BaseObject {

	private static final long serialVersionUID = -8508708642925653475L;

	private long staffId = 0l;
	
	private String staffName = null;
	
	private String staffPosition = null;
	
	private String staffEducation = null;
	
	private String staffDescription = null;
	
	private String staffImageUrl = null;
	
	private String updateDate = null;

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffPosition() {
		return staffPosition;
	}

	public void setStaffPosition(String staffPosition) {
		this.staffPosition = staffPosition;
	}

	public String getStaffEducation() {
		return staffEducation;
	}

	public void setStaffEducation(String staffEducation) {
		this.staffEducation = staffEducation;
	}

	public String getStaffDescription() {
		return staffDescription;
	}

	public void setStaffDescription(String staffDescription) {
		this.staffDescription = staffDescription;
	}

	public String getStaffImageUrl() {
		return staffImageUrl;
	}

	public void setStaffImageUrl(String staffImageUrl) {
		this.staffImageUrl = staffImageUrl;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
}
