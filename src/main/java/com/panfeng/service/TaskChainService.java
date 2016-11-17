package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.NodesEvent;
import com.panfeng.resource.model.TaskChain;
import com.panfeng.resource.model.Tree;

public interface TaskChainService {
	List<TaskChain> getAll();

	List<NodesEvent> getAllEvents();

	long updateEvent(NodesEvent nodesEvent);

	long addEvent(NodesEvent nodesEvent);

	long deleteEvent(Long eventId);

	long updateNodes(TaskChain taskChain);

	long addNodes(TaskChain taskChain);
	
	List<Tree> getEventTree();
	
	List<Tree> contentTemplateTree();
}
