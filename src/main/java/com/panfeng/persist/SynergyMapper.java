package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Synergy;

public interface SynergyMapper {
	long save(Synergy synergy);

	long update(Synergy synergy);

	long delete(@Param("synergyId") long synergyId);

	Synergy findSynergyById(@Param("synergyId") long synergyId);
	
	List<Synergy> findSynergyByProjectId(@Param("projectId") long projectId);
	
	List<Synergy> findSynergyByUserId(@Param("userId") long userId);

	/**
	 * 获取协同人Map
	 * @return map
	 */
	public List<Synergy> findSynergyList();
	
}
