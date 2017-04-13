package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;

/**
 * 资源储存实体
 * 
 * @author Wang,LM
 *
 */
public class IndentResource extends BaseEntity {

	private static final long serialVersionUID = 0x7fda5;

	private long irId;
	// 文件原名
	private String irOriginalName;
	// 可以生成 下载链接
	private String irFormatName;
	private String irViewName;
	private String irCreateDate;
	private long irIndentId;
	private String irProcessInstanceId;
	private String irTaskId;
	private String irtype;
	
	private String irUserType="";
	private long irUserId=-1;

	public long getIrId() {
		return irId;
	}

	public void setIrId(long irId) {
		this.irId = irId;
	}

	public String getIrtype() {
		return irtype;
	}

	public void setIrtype(String irtype) {
		this.irtype = irtype;
	}

	public String getIrViewName() {
		return irViewName;
	}

	public void setIrViewName(String irViewName) {
		this.irViewName = irViewName;
	}

	public String getIrOriginalName() {
		return irOriginalName;
	}

	public void setIrOriginalName(String irOriginalName) {
		this.irOriginalName = irOriginalName;
	}

	public String getIrFormatName() {
		return irFormatName;
	}

	public void setIrFormatName(String irFormatName) {
		this.irFormatName = irFormatName;
	}

	public String getIrCreateDate() {
		return irCreateDate;
	}

	public void setIrCreateDate(String irCreateDate) {
		this.irCreateDate = irCreateDate;
	}

	public long getIrIndentId() {
		return irIndentId;
	}

	public void setIrIndentId(long irIndentId) {
		this.irIndentId = irIndentId;
	}

	public String getIrProcessInstanceId() {
		return irProcessInstanceId;
	}

	public void setIrProcessInstanceId(String irProcessInstanceId) {
		this.irProcessInstanceId = irProcessInstanceId;
	}

	public String getIrTaskId() {
		return irTaskId;
	}

	public void setIrTaskId(String irTaskId) {
		this.irTaskId = irTaskId;
	}

	public String getDownloadUrl() {
		return this.irFormatName;
	}


	public IndentResource() {
		super();
	}

	public String getIrUserType() {
		return irUserType;
	}

	public void setIrUserType(String irUserType) {
		this.irUserType = irUserType;
	}

	public long getIrUserId() {
		return irUserId;
	}

	public void setIrUserId(long irUserId) {
		this.irUserId = irUserId;
	}
	///冗余字段///////////////////////
	private UserViewModel userViewModel=null;
	
	private String state="";

	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public UserViewModel getUserViewModel() {
		return userViewModel;
	}

	public void setUserViewModel(UserViewModel userViewModel) {
		this.userViewModel = userViewModel;
	}
	
}
