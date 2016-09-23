package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.City;
import com.panfeng.resource.model.Province;
import com.panfeng.service.CityService;
import com.panfeng.service.ProvinceService;
import com.panfeng.util.Log;
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

	//private static Logger logger = LoggerFactory.getLogger("error");

	@Autowired
	private ProvinceService provinceService;
	@Autowired
	private CityService cityService;

	@RequestMapping("/get/provinces")
	public List<Province> getAllProvince() {
		return provinceService.getAll();
	}

	@RequestMapping("/get/citys/{provinceId}")
	public List<City> getCitys(@PathVariable("provinceId") String provinceId,HttpServletRequest request) {
		if (ValidateUtil.isValid(provinceId)) {
			return cityService.findCitysByProvinceId(provinceId);
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("provinceId is null ...",sessionInfo);
			return new ArrayList<>();
		}
	}

	@RequestMapping("/all/citys")
	public List<City> getCitys() {
		return cityService.getAll();
	}

	@RequestMapping("/get/province")
	public Province getProvince(String provinceId,HttpServletRequest request) {
		if (ValidateUtil.isValid(provinceId)) {
			return provinceService.findProvinceById(provinceId);
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("province is null ...",sessionInfo);
			return new Province();
		}
	}
}
