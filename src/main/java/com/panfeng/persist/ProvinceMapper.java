package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Province;

public interface ProvinceMapper {
	List<Province> findAll();

	Province findProvinceById(@Param("ProvinceId") String ProvinceId);
}
