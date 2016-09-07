package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.City;

public interface CityService {
	List<City> findCitysByProvinceId(String ProvinceId);
	List<City> getAll();
}
