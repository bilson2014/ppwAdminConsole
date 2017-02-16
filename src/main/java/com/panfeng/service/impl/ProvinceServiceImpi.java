package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.ProvinceMapper;
import com.panfeng.resource.model.Province;
import com.panfeng.service.bak_ProvinceService;

@Service
public class ProvinceServiceImpi implements bak_ProvinceService {

	@Autowired
	private ProvinceMapper provinceMapper;

	/*@Override
	public List<Province> getAll() {
		return provinceMapper.findAll();
	}*/

	/*@Override
	public Province findProvinceById(String ProvinceId) {
		return provinceMapper.findProvinceById(ProvinceId);
	}*/
}
