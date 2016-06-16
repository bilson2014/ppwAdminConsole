package com.panfeng.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.SynergyMapper;
import com.panfeng.resource.model.Synergy;
import com.panfeng.service.SynergyService;
import com.panfeng.util.ValidateUtil;

@Service
public class SynergyServiceImpl implements SynergyService {

	@Autowired
	SynergyMapper synergyMapper;

	@Override
	public long save(Synergy synergy) {
		synergy = dividePrice(synergy);
		return synergyMapper.save(synergy);
	}

	@Override
	public long update(Synergy synergy) {
		synergy = dividePrice(synergy);
		return synergyMapper.update(synergy);
	}

	@Override
	public Synergy findSynergyById(long synergyId) {
		Synergy synergy = synergyMapper.findSynergyById(synergyId);
		synergy = multiplyPrice(synergy);
		return synergy;
	}

	@Override
	public List<Synergy> findSynergyByProjectId(long synergyId) {
		List<Synergy> synergies = synergyMapper
				.findSynergyByProjectId(synergyId);
		synergies = setIndentProjectsPrice(synergies);
		return synergies;
	}

	@Override
	public List<Synergy> findSynergyByUserId(long userId) {
		List<Synergy> synergies = synergyMapper.findSynergyByUserId(userId);
		synergies = setIndentProjectsPrice(synergies);
		return synergies;
	}

	@Override
	public long delete(long synergyId) {
		return synergyMapper.delete(synergyId);
	}

	@Override
	public Map<Long, List<Synergy>> findSynergyMap() {
		
		final List<Synergy> list = synergyMapper.findSynergyList();
		final Map<Long,List<Synergy>> map = new HashMap<Long,List<Synergy>>();
		for (Synergy synergy : list) {
			List<Synergy> tempList = map.get(synergy.getProjectId());
			if(!ValidateUtil.isValid(tempList)){
				tempList = new ArrayList<Synergy>();
			}
			multiplyPrice(synergy);
			tempList.add(synergy);
			map.put(synergy.getProjectId(), tempList);
		}
		return map;
	}

	private static Synergy multiplyPrice(final Synergy synergy) {
		double ratio = synergy.getRatio();
		BigDecimal ratioBigDecimal = BigDecimal.valueOf(ratio);
		ratioBigDecimal = ratioBigDecimal.multiply(BigDecimal.valueOf(100));
		synergy.setRatio(ratioBigDecimal.doubleValue());
		return synergy;
	}

	private static Synergy dividePrice(final Synergy synergy) {
		double ratio = synergy.getRatio();
		BigDecimal ratioBigDecimal = BigDecimal.valueOf(ratio);
		ratioBigDecimal = ratioBigDecimal.divide(BigDecimal.valueOf(100));
		synergy.setRatio(ratioBigDecimal.doubleValue());
		return synergy;
	}

	private static List<Synergy> setIndentProjectsPrice(
			final List<Synergy> synergies) {
		for (Synergy synergie : synergies) {
			synergie = multiplyPrice(synergie);
		}
		return synergies;
	}

	@Override
	public Map<Long, Synergy> findSynergyMapByProjectId(final long projectId) {
		
		final Map<Long, Synergy> map = synergyMapper.findSynergyMapByProjectId(projectId);
		return map;
	}
}
