package com.panfeng.service;

import java.util.List;

import com.panfeng.flow.data.FillerParam;
import com.panfeng.flow.taskchain.EventType;
import com.panfeng.resource.model.Tree;

public interface TemplateDataManage {
	List<Tree> getDataList();

	<F, T> F fillData(FillerParam fillerParam, String templateDataKey, T t,EventType eventType);

	List<Tree> optionalFields(String templateDataKey);

	List<Tree> personnel(String templateDataKey);

	List<Tree> getEventHandle();
}
