package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;

public interface IndentFlowMapper {

	public long save(IndentFlow indentFlow);

	public List<IndentFlow> findFlowByIndentId(IndentProject indentProject);

	public IndentFlow findFlowById(@Param("flowid") long flowid);
	
	public IndentFlow findFlowByIndent(IndentProject indentProject);

	public long delete(IndentFlow indentFlow);

	public long deleteByflowId(IndentProject indentProject, @Param("ifFlowId") String ifFlowId);

	public long update(IndentFlow indentFlow);
	
	public List<IndentFlow> findFlowDateByIndentId(IndentProject indentProject);
	
	public IndentFlow findFlowDateByFlowKey(@Param("id") long id,@Param("key") String key);
	
}
