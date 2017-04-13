package com.panfeng.flow.data;

import java.util.LinkedList;

import com.paipianwang.pat.common.entity.BaseEntity;

public class FillerParam extends BaseEntity {
	private static final long serialVersionUID = -6721308021040801276L;

	private LinkedList<String> fields;

	private LinkedList<String> relevantPersons;

	public LinkedList<String> getFields() {
		return fields;
	}

	public void setFields(LinkedList<String> fields) {
		this.fields = fields;
	}

	public LinkedList<String> getRelevantPersons() {
		return relevantPersons;
	}

	public void setRelevantPersons(LinkedList<String> relevantPersons) {
		this.relevantPersons = relevantPersons;
	}

}
