package com.panfeng.persist;

import java.util.List;
import java.util.Map;

import com.panfeng.resource.model.ActivitiGroup;

public interface ActivitiGroupMapper {

	public long count(Map<String, Object> paramMap);
	
	public List<ActivitiGroup> listWithPagination(Map<String, Object> paramMap);
}
