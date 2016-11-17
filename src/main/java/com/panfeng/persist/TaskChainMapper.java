package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.TaskChain;
import com.panfeng.resource.model.TaskChainNodesEventLink;

public interface TaskChainMapper {

	List<TaskChain> getAll();

	long save(TaskChain taskChain);

	long update(TaskChain taskChain);

	long delete(@Param("taskChainId") Long taskChainId);

	long deleteLink(@Param("linkId") Long linkId);

	long saveLink(TaskChainNodesEventLink taskChainNodesEventLink);
	
	TaskChain findTaskChainByTaskChainId(@Param("taskChainId") Long taskChainId);
	
	List<TaskChainNodesEventLink> findLinkByTaskChainId(@Param("taskChainId") Long taskChainId);

}