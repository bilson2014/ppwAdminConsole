package com.panfeng.service;

import java.io.OutputStream;
import java.util.List;

import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.BizBean;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.view.IndentProjectView;

public interface IndentProjectService {

	public boolean save(IndentProject indentProject,SessionInfo sessionInfo);

	public long update(IndentProject indentProject);

	public long delete(IndentProject indentProject);

	public List<IndentProject> findProjectList(IndentProject indentProject);
	
	public List<IndentProject> findProjectListByPhone(IndentProject indentProject);

	public IndentProject getProjectInfo(IndentProject indentProject);
	
	public IndentProject getProjectInfo(long id);

	public IndentProject getRedundantProject(IndentProject indentProject);

	public boolean updateIndentProject(IndentProject indentProject,boolean isUpdateSynergy);

	public ActivitiTask getTaskInfo(IndentProject indentProject);

	public List<BizBean> getTags();

	public boolean cancelProject(IndentProject indentProject);
	
	/**
	 * 生成 indentProject 所属用户的全部报表
	 * 
	 * @param indentProject
	 * @param outputStream
	 */
	public void getReport(IndentProject indentProject, OutputStream outputStream);

	/**
	 * 生成 list 报表
	 * 
	 * @param list
	 * @param outputStream
	 */
	public void getReport(List<IndentProject> list, OutputStream outputStream);

	/**
	 * 后台分页显示项目信息
	 * 
	 * @param view
	 *            条件
	 */
	public List<IndentProject> listWithPagination(final IndentProjectView view);

	/**
	 * 查询项目总数
	 * 
	 * @param view
	 *            条件
	 */
	public long maxSize(final IndentProjectView view);

	public long delete(final long[] ids);

	public List<IndentProject> getAllTeam();

	public List<IndentProject> getAllUser();

	public List<IndentProject> getAllVersionManager();

	public List<IndentProject> getAllProject();

	/**
	 * 获取当年所有有效的项目
	 * 
	 * @return
	 */
	public String getProjectSerialID(Long id);

	/**
	 * 删除项目协同人
	 * 
	 * @param synergyId
	 * @return
	 */
	public long removeSynergy(long synergyId);

	public List<IndentProject> getSynergys(IndentProject indentProject);

	/**
	 * 获取所有项目
	 * 
	 * @return list
	 */
	public List<IndentProject> all();

	// add by wanglc 2016-6-29 10:41:24 begin
	// 查询含有协同人的数据数量
	/**
	 * 查询含有协同人的数据量
	 */
	public long maxSizeAddSynergy(IndentProjectView view);
	// add by wanglc 2016-6-29 10:41:24 end

	public BaseMsg verifyProjectInfo(long projectId);
	
	public BaseMsg verifyIntegrity(IndentProject indentProject);

	public void getReportForExport(List<IndentProject> list, OutputStream outputStream); 
	
	public void deleteProject(IndentProject indentProject);

	/**
	 * 导出excel表格
	 * @param list
	 * @param outputStream
	 */
	public void export2Excel(final List<IndentProject> list, final OutputStream outputStream);
}
