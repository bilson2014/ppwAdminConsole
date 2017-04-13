package com.panfeng.resource.model;

import java.util.List;

import com.paipianwang.pat.common.entity.BaseEntity;

/**
 * 活动模块实体
 * 
 * @author wang
 *
 */
public class Activity extends BaseEntity {
	private static final long serialVersionUID = -6322954280136241826L;

	private Long activityId = null; // 活动ID
	private String activityName = null; // 活动名
	private String activityCreateTime = null; // 活动创建时间
	private Integer acticityTempleteType = null; // 活动使用模板类型
	private String acticityTempleteId = null; // 模板id
	private String activityParamList = null; // 模板参数列表
	private String activityStartTime = null; // 活动开始时间
	private String actitityRelevantPersons = null;

	// 常量
	public final static int SYSTEMPARAM = 0;
	public final static int CUSTOMPARAM = 1;
	public final static int PERSONS_ALL_PROVIDER = 0;
	public final static int PERSONS_ALL_USER = 1;
	public final static int PERSONS_ALL_EMPLOYEE = 2;

	// 冗余字段
	private List<Activity.param> paramList = null;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityCreateTime() {
		return activityCreateTime;
	}

	public void setActivityCreateTime(String activityCreateTime) {
		this.activityCreateTime = activityCreateTime;
	}

	public Integer getActicityTempleteType() {
		return acticityTempleteType;
	}

	public void setActicityTempleteType(Integer acticityTempleteType) {
		this.acticityTempleteType = acticityTempleteType;
	}

	public String getActicityTempleteId() {
		return acticityTempleteId;
	}

	public void setActicityTempleteId(String acticityTempleteId) {
		this.acticityTempleteId = acticityTempleteId;
	}

	public String getActivityParamList() {
		return activityParamList;
	}

	public void setActivityParamList(String activityParamList) {
		this.activityParamList = activityParamList;
	}

	public String getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(String activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public List<param> getParamList() {
		return paramList;
	}

	public void setParamList(List<param> paramList) {
		this.paramList = paramList;
	}

	public String getActitityRelevantPersons() {
		return actitityRelevantPersons;
	}

	public void setActitityRelevantPersons(String actitityRelevantPersons) {
		this.actitityRelevantPersons = actitityRelevantPersons;
	}

	public static class param {
		private Integer type;
		private String value;

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

}
