package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class Job extends BaseObject {

	private static final long serialVersionUID = 1373773335319764229L;

	private long jobId = 0l;
	
	private String jobName = null; // 岗位名称
	
	private String jobDescription = null; // 岗位简介
	
	private String demand = null; // 招聘要求
	
	private String startDate = null; // 开始时间
	
	private String endDate = null; // 结束时间
	
	private String updateDate = null; // 更新日期
	
	private String createDate = null; // 创建日期

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getDemand() {
		return demand;
	}

	public void setDemand(String demand) {
		this.demand = demand;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
