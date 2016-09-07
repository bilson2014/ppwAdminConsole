package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.CityMapper;
import com.panfeng.resource.model.City;
import com.panfeng.service.CityService;
@Service
public class CityServiceImpl implements CityService {
	@Autowired
	private CityMapper cityMapper;

	@Override
	public List<City> findCitysByProvinceId(String ProvinceId) {
		return cityMapper.findCitysByProvinceId(ProvinceId);
	}

	@Override
	public List<City> getAll() {
		return cityMapper.getAll();
	}
}
