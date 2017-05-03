package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;

public class IndentComment extends BaseEntity {

	private static final long serialVersionUID = 0xfe5453;

	private long icId=0;
	private String icContent="";
	private String icCreateDate="";
	private String icResourceLink="";
	private String icUserType="";
	private long icUserId=-1;
	private long icIndentId=-1;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("------Indent_Comment------");
		sb.append("\nid:");
		sb.append(this.icId);
		sb.append("\ncontent:");
		sb.append(this.icContent);
		sb.append("\nic_createDate:");
		sb.append(this.icCreateDate);
		sb.append("\nresourceLink:");
		sb.append(this.icResourceLink);
		sb.append("\niuserId:");
		sb.append(this.icUserId);
		sb.append("\nindentId:");
		sb.append(this.icIndentId);
		return sb.toString();
	}

	public long getIcId() {
		return icId;
	}

	public void setIcId(long icId) {
		this.icId = icId;
	}

	public String getIcContent() {
		return icContent;
	}

	public void setIcContent(String icContent) {
		this.icContent = icContent;
	}

	public String getIcCreateDate() {
		return icCreateDate;
	}

	public void setIcCreateDate(String icCreateDate) {
		this.icCreateDate = icCreateDate;
	}

	public String getIcResourceLink() {
		return icResourceLink;
	}

	public void setIcResourceLink(String icResourceLink) {
		this.icResourceLink = icResourceLink;
	}

	public String getIcUserType() {
		return icUserType;
	}

	public void setIcUserType(String icUserType) {
		this.icUserType = icUserType;
	}

	public long getIcUserId() {
		return icUserId;
	}

	public void setIcUserId(long icUserId) {
		this.icUserId = icUserId;
	}

	public long getIcIndentId() {
		return icIndentId;
	}

	public void setIcIndentId(long icIndentId) {
		this.icIndentId = icIndentId;
	}

	public IndentComment(long icId, String icContent, String icCreateDate,
			String icResourceLink, String icUserType, long icUserId,
			long icIndentId) {
		super();
		this.icId = icId;
		this.icContent = icContent;
		this.icCreateDate = icCreateDate;
		this.icResourceLink = icResourceLink;
		this.icUserType = icUserType;
		this.icUserId = icUserId;
		this.icIndentId = icIndentId;
	}

	public IndentComment() {
		super();
	}
	///////冗余字段//////////////////////////////////////////////
	private UserViewModel userViewModel=null;

	public UserViewModel getUserViewModel() {
		return userViewModel;
	}

	public void setUserViewModel(UserViewModel userViewModel) {
		this.userViewModel = userViewModel;
	}
	
}
