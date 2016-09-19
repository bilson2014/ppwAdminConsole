package com.panfeng.resource.model;

import java.util.List;
import java.util.Map;

import com.panfeng.domain.BaseObject;

public class IndentProject extends BaseObject {

	private static final long serialVersionUID = 4610963805615371485L;

	public final static int PROJECT_NORMAL = 0;
	public final static int PROJECT_CANCEL = 1;
	public final static int PROJECT_FINISH = 2;
	public final static int PROJECT_SUSPEND = 3;

	private long id = 0l; // 项目流程ID
	private long projectId = 0l; // 项目ID，冗余字段，以id属性为准
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
	// 0 正常 1为取消 2为已完成 3为暂停
	private Integer state = null;
	private String userType = "";
	private Long userId = null; // 视频管家ID

	// 价格字段
	private Double priceFirst = null;// 预计起始价格
	private Double priceLast = null;// 预计结束价格
	private Double priceFinish = null;// 最终价格

	private String employeeRealName = null; // 管家名字

	private Long customerId = null; // 客户ID

	private Long teamId = null; // 供应商ID

	private String updateTime = null;

	private String createTime = null;

	private Long referrerId = null; // 友情推荐人Id

	private String referrerName = null;// 友情推荐人Name

	private Double customerPayment = null;// 用户实付金额

	private Double providerPayment = null;// 供应商实付金额

	private List<Synergy> synergys = null; // 协助人集合

	private int clientLevel = 0; // 客户分级

	private String stage = null;// 项目阶段

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Double getCustomerPayment() {
		return customerPayment;
	}

	public void setCustomerPayment(Double customerPayment) {
		this.customerPayment = customerPayment;
	}

	public List<Synergy> getSynergys() {
		return synergys;
	}

	public void setSynergys(List<Synergy> synergys) {
		this.synergys = synergys;
	}

	public Double getProviderPayment() {
		return providerPayment;
	}

	public void setProviderPayment(Double providerPayment) {
		this.providerPayment = providerPayment;
	}

	public Long getReferrerId() {
		return referrerId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setReferrerId(Long referrerId) {
		this.referrerId = referrerId;
	}

	public String getReferrerName() {
		return referrerName;
	}

	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
	}

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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

	public Double getPriceFirst() {
		return priceFirst;
	}

	public void setPriceFirst(Double priceFirst) {
		this.priceFirst = priceFirst;
	}

	public Double getPriceLast() {
		return priceLast;
	}

	public void setPriceLast(Double priceLast) {
		this.priceLast = priceLast;
	}

	public Double getPriceFinish() {
		return priceFinish;
	}

	public void setPriceFinish(Double priceFinish) {
		this.priceFinish = priceFinish;
	}

	// -----------------------------
	// 冗余taskdate--------------------------------------

	private Map<String, String> time = null;
	private String fdStartTime = null;
	private ActivitiTask task = null;
	private String tag = "";
	private UserViewModel userViewModel = null;
	private List<ActivitiTask> nodes = null;
	private Long masterFlowId = null;

	public String getFdStartTime() {
		return fdStartTime;
	}

	public void setFdStartTime(String fdStartTime) {
		this.fdStartTime = fdStartTime;
	}

	public List<ActivitiTask> getNodes() {
		return nodes;
	}

	public void setNodes(List<ActivitiTask> nodes) {
		this.nodes = nodes;
	}

	public Long getMasterFlowId() {
		return masterFlowId;
	}

	public void setMasterFlowId(Long masterFlowId) {
		this.masterFlowId = masterFlowId;
	}

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

	public String getEmployeeRealName() {
		return employeeRealName;
	}

	public void setEmployeeRealName(String employeeRealName) {
		this.employeeRealName = employeeRealName;
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getClientLevel() {
		return clientLevel;
	}

	public void setClientLevel(int clientLevel) {
		this.clientLevel = clientLevel;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

}
