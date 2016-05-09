package com.panfeng.resource.model;

import java.util.Map;

import com.panfeng.domain.BaseObject;

public class IndentProject extends BaseObject {

	private static final long serialVersionUID = 4610963805615371485L;

	public final static int PROJECT_NORMAL = 0;
	public final static int PROJECT_CANCEL = 1;
	public final static int PROJECT_FINISH = 2;

	private long id = 0l; // 项目流程ID
	private String projectName = ""; //  
	private String userName = ""; // 客户公司名称
	private String userContact = ""; // 客户名称
	private String userPhone = "";
	private String teamName = "";
	private String teamContact = "";
	private String teamPhone = "";
	private String description = "";
	private String serial = "";
	private String source = "";
	// 0 正常 1为取消 2为已完成
	private int state = 0;
	private String userType = "";
	private long userId = -1; // 视频管家ID
	
	//价格字段
	private double priceFirst =0.00;//预计起始价格
	private double priceLast =0.00;//预计结束价格
	private double priceFinish =0.00;//最终价格

	
	private String managerRealName = null; // 管家名字
	
	private Long customerId = null; // 客户ID
	
	private Long teamId = null; // 供应商ID

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserContact() {
		return userContact;
	}

	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamContact() {
		return teamContact;
	}

	public void setTeamContact(String teamContact) {
		this.teamContact = teamContact;
	}

	public String getTeamPhone() {
		return teamPhone;
	}

	public void setTeamPhone(String teamPhone) {
		this.teamPhone = teamPhone;
	}

	public IndentProject() {
		super();
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}


	
	
	// -----------------------------
	// 冗余taskdate--------------------------------------

	public double getPriceFirst() {
		return priceFirst;
	}

	public void setPriceFirst(double priceFirst) {
		this.priceFirst = priceFirst;
	}

	public double getPriceLast() {
		return priceLast;
	}

	public void setPriceLast(double priceLast) {
		this.priceLast = priceLast;
	}

	public double getPriceFinish() {
		return priceFinish;
	}

	public void setPriceFinish(double priceFinish) {
		this.priceFinish = priceFinish;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	private Map<String, String> time = null;
	private ActivitiTask task = null;
	private String tag = "";
	private UserViewModel userViewModel = null; 

	public UserViewModel getUserViewModel() {
		return userViewModel;
	}

	public void setUserViewModel(UserViewModel userViewModel) {
		this.userViewModel = userViewModel;
	}

	public ActivitiTask getTask() {
		return task;
	}

	public void setTask(ActivitiTask task) {
		this.task = task;
	}

	public Map<String, String> getTime() {
		return time;
	}

	public void setTime(Map<String, String> time) {
		this.time = time;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getManagerRealName() {
		return managerRealName;
	}

	public void setManagerRealName(String managerRealName) {
		this.managerRealName = managerRealName;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

}
