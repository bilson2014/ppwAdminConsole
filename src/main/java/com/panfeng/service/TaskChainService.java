package com.panfeng.service;

import java.util.List;

import com.paipianwang.pat.facade.right.entity.PmsTree;
import com.panfeng.resource.model.NodesEvent;
import com.panfeng.resource.model.TaskChain;

public interface TaskChainService {
	List<TaskChain> getAll();

	List<NodesEvent> getAllEvents();

	long updateEvent(NodesEvent nodesEvent);

	long addEvent(NodesEvent nodesEvent);

	long deleteEvent(Long eventId);

	long updateNodes(TaskChain taskChain);

	long addNodes(TaskChain taskChain);
	
	List<PmsTree> getEventTree();
	
	List<PmsTree> contentTemplateTree();
}
