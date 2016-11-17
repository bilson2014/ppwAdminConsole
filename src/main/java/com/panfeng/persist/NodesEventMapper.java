package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.NodesEvent;

public interface NodesEventMapper {
	List<NodesEvent> getAll();

	NodesEvent findTaskById(@Param("nodesEventid") Long taskid);

	long save(NodesEvent nodesEvent);

	long update(NodesEvent nodesEvent);

	long delete(@Param("nodesEventid") Long nodesEventid);

}
