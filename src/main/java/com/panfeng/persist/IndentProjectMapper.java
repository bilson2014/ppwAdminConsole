package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.view.IndentProjectView;

public interface IndentProjectMapper {

	public long save(IndentProject indentProject);

	public long update(IndentProject indentProject);

	public long delete(IndentProject indentProject);

	public List<IndentProject> findProjectList(IndentProject indentProject);

	public IndentProject findProjectInfo(IndentProject indentProject);
	
	public List<IndentProject> findProjectByUserName(IndentProject indentProject);
	
	public List<IndentProject> findProjectByIds(@Param("list") List<Long> list);
	
	public long cancelProject(IndentProject indentProject);

	public List<IndentProject> listWithPagination(final IndentProjectView view);

	public long maxSize(final IndentProjectView view);

	public long deleteById(@Param("projectId") final long id);
	
	/**
	 * 获取所有客户
	 */
	public List<IndentProject> getAllUser();
	
	/**
	 * 获取所有供应商
	 */
	public List<IndentProject> getAllTeam();
	
	/**
	 * 获取所有视频管家
	 */
	public List<IndentProject> getAllVersionManager();

	/**
	 * 获取项目
	 */
	public List<IndentProject> getAllProject();
	
	/**
	 * 获取本年有效项目个数
	 */
	public long getProjectCount();
	
	public long updateState(@Param("id") final long id, @Param("state") int state);
	
}
