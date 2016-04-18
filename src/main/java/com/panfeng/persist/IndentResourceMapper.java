package com.panfeng.persist;

import java.util.List;

import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.IndentResource;

public interface IndentResourceMapper {

	public long save(IndentResource indentResource);

	public long delete(IndentResource indentResource);

	public long update(IndentResource indentResource);

	public IndentResource findResourceById(IndentResource indentResource);

	public List<IndentResource> findResourcetListByIndentId(IndentProject indentProject);
	
	public List<IndentResource> findIndentListByTaskId(ActivitiTask activitiTask);

	public long deleteIndentResourceList(IndentProject indentProject);
}
