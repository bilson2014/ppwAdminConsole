package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class Right extends BaseObject {

	private static final long serialVersionUID = 2821043977105046450L;

	private long rightId = 0l;
	
	private String rightName = null;
	
	private String url = null;

	private String rightDescription = null;
	
	private String icon = null;
	
	private Long pId = null;
	
	private int seq = 0;
	
	private int status = 1; // 状态(0:不可用 1:可用)
	
	private String createDate = null;
	
	private String updateDate = null;
	
	private int resourceType = 1; // 资源类型(0:菜单 1:按钮)

	private int pos = 0;
	
	private long code = 0l;
	
	private boolean isCommon;
	
	private String iconCls;

	public long getRightId() {
		return rightId;
	}

	public void setRightId(long rightId) {
		this.rightId = rightId;
	}

	public String getRightName() {
		return rightName;
	}

	public void setRightName(String rightName) {
		this.rightName = rightName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRightDescription() {
		return rightDescription;
	}

	public void setRightDescription(String rightDescription) {
		this.rightDescription = rightDescription;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
		this.iconCls= icon;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getResourceType() {
		return resourceType;
	}

	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public boolean getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(boolean isCommon) {
		this.isCommon = isCommon;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public void setCommon(boolean isCommon) {
		this.isCommon = isCommon;
	}

}
