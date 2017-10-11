package com.panfeng.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	void exportProjectFlow(List<PmsProjectFlow> list, HttpServletResponse response, SessionInfo sessionInfo);

	void updateProjectSynergy(HttpServletRequest request, BaseMsg result, SessionInfo sessionInfo);

}
