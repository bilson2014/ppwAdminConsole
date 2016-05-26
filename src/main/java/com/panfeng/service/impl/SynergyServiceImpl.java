package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.SynergyMapper;
import com.panfeng.resource.model.Synergy;
import com.panfeng.service.SynergyService;

@Service
public class SynergyServiceImpl implements SynergyService {

	@Autowired
	SynergyMapper synergyMapper;

	@Override
	public long save(Synergy synergy) {
		return synergyMapper.save(synergy);
	}

	@Override
	public long update(Synergy synergy) {
		return synergyMapper.update(synergy);
	}

	@Override
	public Synergy findSynergyById(long synergyId) {
		return synergyMapper.findSynergyById(synergyId);
	}

	@Override
	public List<Synergy> findSynergyByProjectId(long synergyId) {
		return synergyMapper.findSynergyByProjectId(synergyId);
	}

	@Override
	public List<Synergy> findSynergyByUserId(long userId) {
		return synergyMapper.findSynergyByUserId(userId);
	}

	@Override
	public long delete(long synergyId) {
		return synergyMapper.delete(synergyId);
	}

}
