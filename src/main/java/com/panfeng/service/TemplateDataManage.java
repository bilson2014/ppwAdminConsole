package com.panfeng.service;

import java.util.List;

import com.paipianwang.pat.facade.right.entity.PmsTree;
import com.panfeng.flow.data.FillerParam;
import com.panfeng.flow.taskchain.EventType;

public interface TemplateDataManage {
	List<PmsTree> getDataList();

	<F, T> F fillData(FillerParam fillerParam, String templateDataKey, T t,EventType eventType);

	List<PmsTree> optionalFields(String templateDataKey);

	List<PmsTree> personnel(String templateDataKey);

	List<PmsTree> getEventHandle();
}
