package com.panfeng.persist;

import java.util.List;

import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.view.IndentProjectView;

public interface IndentProjectMapper {

	public long save(IndentProject indentProject);

	public long update(IndentProject indentProject);

	public long delete(IndentProject indentProject);

	public List<IndentProject> findProjectList(IndentProject indentProject);

	public IndentProject findProjectInfo(IndentProject indentProject);
	
	public List<IndentProject> findProjectByUserName(IndentProject indentProject);
	
	public long cancelProject(IndentProject indentProject);

	public List<IndentProject> listWithPagination(final IndentProjectView view);

	public long maxSize(final IndentProjectView view);
	
}
