package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.City;

public interface CityMapper {
	List<City> findCitysByProvinceId(@Param("ProvinceId") String ProvinceId);
}
