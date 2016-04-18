package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.FlowDate;

public interface FlowDateMapper {

	void save(FlowDate flowDate);

	long update(FlowDate flowDate);

	long delete(@Param("fdId") long fdId);

	List<FlowDate> findFlowDateByFlowId(@Param("flowId") String flowId);

	FlowDate findFlowDateById(@Param("irId") long irId);
	

}
