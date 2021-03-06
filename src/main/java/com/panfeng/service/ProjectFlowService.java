package com.panfeng.service;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.panfeng.domain.BaseMsg;

public interface ProjectFlowService {

	/**
	 * 流程数据导出
	 * @param list
	 * @param response
	 * @param sessionInfo
	 */
	void exportProjectFlow(List<PmsProjectFlow> list, OutputStream os, SessionInfo sessionInfo);

	void updateProjectSynergy(HttpServletRequest request, BaseMsg result, SessionInfo sessionInfo);
	/**
	 * 项目删除
	 * @param projectIds
	 */
	PmsResult deleteProjectFlow(String[] projectIds);

}
