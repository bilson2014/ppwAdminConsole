package com.panfeng.resource.model;

import java.util.List;

/**
 * 职工表
 * 
 * @author GY
 *
 */
public class Employee extends Customer {

	private static final long serialVersionUID = -2956477405160485997L;

	private long employeeId = 0l; // 职工ID

	private String employeeLoginName = null; // 职工登录名

	private String employeeRealName = null; // 职工姓名

	private String employeePassword = null; // 登陆密码

	private String employeeDescription = null; // 职工描述

	private String createDate = null; // 创建时间

	private String updateDate = null; // 更新时间

	private List<Long> roleIds = null; // 角色ID集合

	private String employeeImg = null;

	private String phoneNumber = null; // 电话号码

	private String email = null; // 邮箱

	// laowang add
	private String qqUnique = null; // QQToken

	private String wbUnique = null; // 微博Token

	private String wechatUnique = null; // 微信Token

	private String thirdLoginType = null; // 第三方登录类型

	private Integer dimissionStatus = null; // 离职状态

	public final static String LTYPE_WEIBO = "weibo";

	public final static String LTYPE_QQ = "qq";

	public final static String LTYPE_WECHAT = "wechat";
	
	public final static int DIMISSIONSTATUS_TEST = 2;// 测试

	public final static int DIMISSIONSTATUS_DIMISSION = 1; // 离职

	public final static int DIMISSIONSTATUS_IN_SERVICE = 0;// 在职
	

	public String getQqUnique() {
		return qqUnique;
	}
	
	public Integer getDimissionStatus() {
		return dimissionStatus;
	}

	public void setDimissionStatus(Integer dimissionStatus) {
		this.dimissionStatus = dimissionStatus;
	}

	public void setQqUnique(String qqUnique) {
		this.qqUnique = qqUnique;
	}

	public String getWbUnique() {
		return wbUnique;
	}

	public void setWbUnique(String wbUnique) {
		this.wbUnique = wbUnique;
	}

	public String getWechatUnique() {
		return wechatUnique;
	}

	public void setWechatUnique(String wechatUnique) {
		this.wechatUnique = wechatUnique;
	}

	public String getThirdLoginType() {
		return thirdLoginType;
	}

	public void setThirdLoginType(String thirdLoginType) {
		this.thirdLoginType = thirdLoginType;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeLoginName() {
		return employeeLoginName;
	}

	public void setEmployeeLoginName(String employeeLoginName) {
		this.employeeLoginName = employeeLoginName;
	}

	public String getEmployeeRealName() {
		return employeeRealName;
	}

	public void setEmployeeRealName(String employeeRealName) {
		this.employeeRealName = employeeRealName;
	}

	public String getEmployeePassword() {
		return employeePassword;
	}

	public void setEmployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
	}

	public String getEmployeeDescription() {
		return employeeDescription;
	}

	public void setEmployeeDescription(String employeeDescription) {
		this.employeeDescription = employeeDescription;
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

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public String getEmployeeImg() {
		return employeeImg;
	}

	public void setEmployeeImg(String employeeImg) {
		this.employeeImg = employeeImg;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
