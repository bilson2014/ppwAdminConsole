package com.panfeng.service;

import java.io.OutputStream;
import java.util.List;

import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.view.IndentProjectView;

public interface IndentProjectService {

	public boolean save(IndentProject indentProject);
	
	public long update(IndentProject indentProject);
	
	public long delete(IndentProject indentProject);
	
	public List<IndentProject> findProjectList(IndentProject indentProject);

	public IndentProject getProjectInfo(IndentProject indentProject);
	
	public IndentProject getRedundantProject(IndentProject indentProject);
	
	public boolean updateIndentProject(IndentProject indentProject);
	
	public ActivitiTask getTaskInfo(IndentProject indentProject);
	
	public String[] getTags();
	
	public boolean cancelProject(IndentProject indentProject);
	
	public void getReport(IndentProject indentProject,OutputStream outputStream);

	/**
	 * 后台分页显示项目信息
	 * @param view 条件
	 */
	public List<IndentProject> listWithPagination(final IndentProjectView view);

	/**
	 * 查询项目总数
	 * @param view 条件
	 */
	public long maxSize(final IndentProjectView view);
}
