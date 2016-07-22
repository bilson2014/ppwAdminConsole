package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Province;

public interface ProvinceService {
	List<Province> getAll();

	Province findProvinceById(String ProvinceId);
}
