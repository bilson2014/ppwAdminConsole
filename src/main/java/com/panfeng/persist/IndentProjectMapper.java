package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.view.IndentProjectView;

public interface IndentProjectMapper {

	public long save(IndentProject indentProject);

	public long update(IndentProject indentProject);

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
	
	public long updateState(@Param("id") final long id, @Param("state") int state,@Param("description") String msg);

	/**
	 * add by wanglc 2016-6-28 19:56:47
	 * 添加协同人搜索维度,同时对数据排序,作为组负责人放在前面,协同人放在后面 
	 */
	public List<IndentProject> listWithPaginationAddSynergy(IndentProjectView view);

	/**add by wanglc 2016-6-29 10:45:22 
	 * 查询含有协同人的数据量
	 */
	public long maxSizeAddSynergy(IndentProjectView view);

	/**
	 * 获取所有项目
	 * @return list
	 */
	public List<IndentProject> all();

	public List<IndentProject> listWithPaginationNoLimit(IndentProjectView view);
	
}
