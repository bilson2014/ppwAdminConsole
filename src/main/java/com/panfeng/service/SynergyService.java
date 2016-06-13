package com.panfeng.service;

import java.util.List;
import java.util.Map;

import com.panfeng.resource.model.Synergy;

public interface SynergyService {

	long save(Synergy synergy);

	long update(Synergy synergy);

	Synergy findSynergyById(long synergyId);

	List<Synergy> findSynergyByProjectId(long synergyId);

	List<Synergy> findSynergyByUserId(long userId);
	
	long delete(long synergyId);

	public Map<Long,List<Synergy>> findSynergyMap();

}
