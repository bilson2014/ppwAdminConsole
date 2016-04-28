package com.panfeng.resource.model;


/**
 * 用户业务类
 * @author GY
 */
public class User extends Customer{

	private static final long serialVersionUID = -3500839628063835593L;
	
	private long id = 0l;
	
	private String userName = null; // 昵称
	
	private String realName = null; // 真实姓名
	
	private String password = null; // 密码
	
	private int sex = 0; // 性别 : 0:男  1:女  2:保密
	
	private String telephone = null; // 手机
	
	private String email = null; // 邮箱
	
	private String birthday = null; // 生日
	
	private String createDate = null; // 创建日期
	
	private String verification_code = null; // 短信验证码
	
	private String imgUrl = null; // 用户头像
	
	private String qq = null; // QQ号码
	
	private String lType = null; // 第三方登录方式  1.微博登录:weibo  2.qq登录:qq  3.微信登录:wechat
	
	private String uniqueId = null; // 第三方登录用户唯一标识
	
	private String qqUnique = null; // 第三方登录用户唯一ID-QQ
	
	private String wbUnique = null; // 第三方登录用户唯一ID-微博
	
	private String wechatUnique = null; // 第三方登录用户唯一ID-微信
	
	private String imgFileName = null; // 头像文件名称
	
	private String userCompany = null; // 客户公司
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getVerification_code() {
		return verification_code;
	}

	public void setVerification_code(String verification_code) {
		this.verification_code = verification_code;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	
	public String getlType() {
		return lType;
	}

	public void setlType(String lType) {
		this.lType = lType;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getQqUnique() {
		return qqUnique;
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

	public String getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

	public String getUserCompany() {
		return userCompany;
	}

	public void setUserCompany(String userCompany) {
		this.userCompany = userCompany;
	}

}
