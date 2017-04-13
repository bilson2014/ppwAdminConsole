package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;

public class NodesEvent extends BaseEntity {

	private static final long serialVersionUID = 6528926261399387474L;

	private Long nodesEventId;
	private String nodesEventName = "";
	private String nodesEventDescription = "";
	private String nodesEventClassName = "";
	private int nodesEventModel;
	private String nodesOptions = null;

	private String templateId; // 模板ID
	private int templateType; // 模板ID
	private String dataFiller; // 模板数据装载器
	private String dataFields;// 模板字段
	private String relevantPerson;// 设计人员

	public static int AUTOMODEL = 0; // 自动任务 （静默任务）
	public static int MANUMODEL = 1;

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof NodesEvent) {
			NodesEvent ne = (NodesEvent) obj;
			if (this.nodesEventId == ne.nodesEventId) {
				if (this.nodesEventName.equals(ne.nodesEventName)) {
					if (this.nodesEventDescription.equals(ne.nodesEventDescription)) {
						if (this.nodesEventClassName.equals(ne.nodesEventClassName)) {
							if (this.nodesEventModel == ne.nodesEventModel) {
								if (this.templateId.equals(ne.getTemplateId())) {
									if (this.templateType == ne.templateType) {
										if (this.dataFiller.equals(ne.getDataFiller())) {
											if (this.dataFields.equals(ne.getDataFields())) {
												if (this.relevantPerson.equals(ne.getRelevantPerson()))
													return true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public String getNodesOptions() {
		return nodesOptions;
	}

	public void setNodesOptions(String nodesOptions) {
		this.nodesOptions = nodesOptions;
	}

	public Long getNodesEventId() {
		return nodesEventId;
	}

	public void setNodesEventId(Long nodesEventId) {
		this.nodesEventId = nodesEventId;
	}

	public String getNodesEventName() {
		return nodesEventName;
	}

	public void setNodesEventName(String nodesEventName) {
		this.nodesEventName = nodesEventName;
	}

	public String getNodesEventDescription() {
		return nodesEventDescription;
	}

	public void setNodesEventDescription(String nodesEventDescription) {
		this.nodesEventDescription = nodesEventDescription;
	}

	public String getNodesEventClassName() {
		return nodesEventClassName;
	}

	public void setNodesEventClassName(String nodesEventClassName) {
		this.nodesEventClassName = nodesEventClassName;
	}

	public int getNodesEventModel() {
		return nodesEventModel;
	}

	public void setNodesEventModel(int nodesEventModel) {
		this.nodesEventModel = nodesEventModel;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDataFiller() {
		return dataFiller;
	}

	public void setDataFiller(String dataFiller) {
		this.dataFiller = dataFiller;
	}

	public String getDataFields() {
		return dataFields;
	}

	public void setDataFields(String dataFields) {
		this.dataFields = dataFields;
	}

	public String getRelevantPerson() {
		return relevantPerson;
	}

	public void setRelevantPerson(String relevantPerson) {
		this.relevantPerson = relevantPerson;
	}

}
