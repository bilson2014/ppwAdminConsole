package com.panfeng.service.impl;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.persist.FlowDateMapper;
import com.panfeng.persist.IndentFlowMapper;
import com.panfeng.persist.IndentProjectMapper;
import com.panfeng.poi.GenerateExcel;
import com.panfeng.poi.ProjectPoiAdapter;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.FlowDate;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.UserViewModel;
import com.panfeng.resource.view.IndentProjectView;
import com.panfeng.service.IndentActivitiService;
import com.panfeng.service.IndentCommentService;
import com.panfeng.service.IndentProjectService;
import com.panfeng.service.UserTempService;
import com.panfeng.util.ValidateUtil;

@Service
public class IndentProjectServiceImpl implements IndentProjectService {

	@Autowired
	IndentProjectMapper indentProjectMapper;
	@Autowired
	IndentActivitiService indentActivitiService;
	@Autowired
	IndentFlowMapper indentFlowMapper;
	@Autowired
	FlowDateMapper flowDateMapper;
	@Autowired
	IndentCommentService indentCommentService;
	@Autowired
	UserTempService userTempService;

	@Override
	public boolean save(IndentProject indentProject) {
		indentProjectMapper.save(indentProject);
		if (indentProject != null) {
			indentCommentService.createSystemMsg(
					"创建了 " + indentProject.getProjectName() + "项目",
					indentProject);
			return indentActivitiService.startProcess(indentProject);
		}
		return false;
	}

	@Override
	public long update(IndentProject indentProject) {
		return indentProjectMapper.update(indentProject);
	}

	@Override
	public long delete(IndentProject indentProject) {
		return indentProjectMapper.delete(indentProject);
	}

	@Override
	public List<IndentProject> findProjectList(IndentProject indentProject) {
		String userType = indentProject.getUserType();
		//UserViewModel userViewModel = userTempService.getInfo(userType, userId);
		//String userName = userViewModel.getOrgName();
		List<IndentProject> list=null;
		switch (userType) {
		// 用户身份 -- 客户
		case GlobalConstant.ROLE_CUSTOMER:
			list= indentProjectMapper
					.findProjectByUserName(indentProject);
			break;
		// 用户身份 -- 供应商
		case GlobalConstant.ROLE_PROVIDER:
			list= indentProjectMapper
					.findProjectByUserName(indentProject);
			break;
		// 用户身份 -- 视频管家
		case GlobalConstant.ROLE_MANAGER:
			list= indentProjectMapper.findProjectList(indentProject);
			break;
		}
		return list;
	}

	@Override
	public IndentProject getProjectInfo(IndentProject indentProject) {
		return indentProjectMapper.findProjectInfo(indentProject);
	}

	@Override
	public IndentProject getRedundantProject(IndentProject indentProject) {
		indentProject = indentProjectMapper.findProjectInfo(indentProject);
		List<IndentFlow> listDates = indentFlowMapper
				.findFlowDateByIndentId(indentProject);
		IndentFlow.indentProjectFillDate(indentProject, listDates);
		return indentProject;
	}

	@Override
	public boolean updateIndentProject(IndentProject indentProject) {
		// update project
		long l = indentProjectMapper.update(indentProject);
		if (l > 0) {
			List<IndentFlow> listDates = indentFlowMapper
					.findFlowDateByIndentId(indentProject);
			List<FlowDate> dates = IndentFlow.getFlowDates(listDates);
			IndentFlow.updateFlowDates(indentProject, dates);
			for (FlowDate flowDate : dates) {
				flowDateMapper.update(flowDate);
			}
			indentCommentService.createSystemMsg(
					"更新了 " + indentProject.getProjectName() + "项目",
					indentProject);
			return true;
		}
		return false;
	}

	@Override
	public ActivitiTask getTaskInfo(IndentProject indentProject) {
		String taskName = indentProject.getTask().getName();
		List<ActivitiTask> activitiTasks = indentActivitiService
				.getHistoryProcessTask(indentProject);

		ActivitiTask at = null;
		for (ActivitiTask activitiTask : activitiTasks) {
			if (activitiTask.getName().equals(taskName)) {
				at = activitiTask;
				break;
			}
		}
		if (at != null) {
			// 填充预计时间
			IndentFlow indentFlow = indentFlowMapper.findFlowDateByFlowKey(
					indentProject.getId(), at.getTaskDefinitionKey());
			at.setScheduledTime(new FlowDate(indentFlow.getFdId(), indentFlow
					.getFdFlowId(), indentFlow.getFdStartTime(), indentFlow
					.getFdTaskId()));
		} else {
			at = new ActivitiTask();
		}
		return at;
	}

	@Override
	public String[] getTags() {
		String[] tags = new String[6];
		tags[0] = "网站下单";
		tags[1] = "友情推荐";
		tags[2] = "活动下单";
		tags[3] = "渠道优惠";
		tags[4] = "团购下单";
		tags[5] = "媒体推广";
		return tags;
	}

	@Override
	public boolean cancelProject(IndentProject indentProject) {
		indentProject.setState(IndentProject.PROJECT_CANCEL);
		long l = indentProjectMapper.cancelProject(indentProject);
		return (l > 0);
	}

	@Override
	public void getReport(IndentProject indentProject,OutputStream outputStream) {
		ProjectPoiAdapter projectPoiAdapter = new ProjectPoiAdapter();
		GenerateExcel ge = new GenerateExcel();
		List<IndentProject> list = indentProjectMapper.findProjectList(indentProject);
		for (IndentProject indentProject2 : list) {
			List<IndentFlow> listDates = indentFlowMapper
					.findFlowDateByIndentId(indentProject2);
			IndentFlow.indentProjectFillDate(indentProject2, listDates);
			ActivitiTask at = indentActivitiService
					.getCurrentTask(indentProject2);
			if (at.getId().equals("")) {
				List<HistoricTaskInstance> listHistoricTaskInstances = indentActivitiService
						.getHistoryProcessTask_O(indentProject2);
				HistoricTaskInstance historicTaskInstance = listHistoricTaskInstances
						.get(listHistoricTaskInstances.size() - 1);
				at.setId("");
				at.setName("已完成");
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
				at.setCreateTime(simpleDateFormat.format(historicTaskInstance.getEndTime()));
			}
			indentProject2.setTask(at);
			//填充管家
			UserViewModel userViewModel=userTempService.getInfo(indentProject2.getUserType(), indentProject2.getUserId());
			indentProject2.setUserViewModel(userViewModel);
			projectPoiAdapter.getData().add(indentProject2);
		}
		ge.generate(projectPoiAdapter,outputStream);
	}

	public List<IndentProject> listWithPagination(final IndentProjectView view) {
		
		List<IndentProject> list = indentProjectMapper.listWithPagination(view);
		return list;
	}

	public long maxSize(final IndentProjectView view) {
		
		final long total = indentProjectMapper.maxSize(view);
		return total;
	}

	@Transactional
	public long delete(final long[] ids) {
		
		if(ValidateUtil.isValid(ids)){
			for (final long id : ids) {
				indentProjectMapper.deleteById(id);
			}
		}
		return 1l;
	}

	public List<IndentProject> getAllTeam() {
		
		final List<IndentProject> list = indentProjectMapper.getAllTeam();
		return list;
	}

	public List<IndentProject> getAllUser() {
		
		final List<IndentProject> list = indentProjectMapper.getAllUser();
		return list;
	}
	
	public List<IndentProject> getAllVersionManager() {
		
		final List<IndentProject> list = indentProjectMapper.getAllVersionManager();
		return list;
	}

	public List<IndentProject> getAllProject() {
		
		final List<IndentProject> list = indentProjectMapper.getAllProject();
		return list;
	}
	
}
