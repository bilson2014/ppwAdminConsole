package com.panfeng.service;

import java.util.List;

import com.panfeng.flow.taskchain.EventType;
import com.panfeng.resource.model.Tree;

public interface TemplateDataManage {
	List<Tree> getDataList();

	<F, T> F fillData(List<String> list, String templateDataKey, T t,EventType eventType);

	List<Tree> optionalFields(String templateDataKey);

	List<Tree> personnel(String templateDataKey);

	List<Tree> getEventHandle();
}
