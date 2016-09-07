package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.resource.model.City;
import com.panfeng.resource.model.Province;
import com.panfeng.service.CityService;
import com.panfeng.service.ProvinceService;
import com.panfeng.util.ValidateUtil;

/**
 * 省市选择器
 * 
 * @author wang
 *
 */
@RequestMapping("/portal")
@RestController
public class CityPickerController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger("error");

	@Autowired
	private ProvinceService provinceService;
	@Autowired
	private CityService cityService;

	@RequestMapping("/get/provinces")
	public List<Province> getAllProvince() {
		return provinceService.getAll();
	}

	@RequestMapping("/get/citys/{provinceId}")
	public List<City> getCitys(@PathVariable("provinceId") String provinceId) {
		if (ValidateUtil.isValid(provinceId)) {
			return cityService.findCitysByProvinceId(provinceId);
		} else {
			logger.error("provinceId is null ...");
			return new ArrayList<>();
		}
	}

	@RequestMapping("/all/citys")
	public List<City> getCitys() {
		return cityService.getAll();
	}

	@RequestMapping("/get/province")
	public Province getProvince(String provinceId) {
		if (ValidateUtil.isValid(provinceId)) {
			return provinceService.findProvinceById(provinceId);
		} else {
			logger.error("province is null ...");
			return new Province();
		}
	}
}
