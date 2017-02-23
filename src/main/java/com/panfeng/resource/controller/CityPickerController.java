package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.pat.facade.team.entity.PmsCity;
import com.paipianwang.pat.facade.team.entity.PmsProvince;
import com.paipianwang.pat.facade.team.service.PmsCityFacade;
import com.paipianwang.pat.facade.team.service.PmsProvinceFacade;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.City;
import com.panfeng.resource.model.Province;
import com.panfeng.service.bak_CityService;
import com.panfeng.service.bak_ProvinceService;
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
	private bak_ProvinceService provinceService;
	@Autowired
	private PmsProvinceFacade pmsProvinceFacade;
	@Autowired
	private PmsCityFacade pmsCityFacade;
	
	@RequestMapping("/get/provinces")
	public List<PmsProvince> getAllProvince() {
		//return provinceService.getAll();
		return pmsProvinceFacade.getAll();
	}

	@RequestMapping("/get/citys/{provinceId}")
	public List<PmsCity> getCitys(@PathVariable("provinceId") String provinceId,HttpServletRequest request) {
		if (ValidateUtil.isValid(provinceId)) {
			//return cityService.findCitysByProvinceId(provinceId);
			return pmsCityFacade.findCitysByProvinceId(provinceId);
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("provinceId is null ...",sessionInfo);
			return new ArrayList<>();
		}
	}

	@RequestMapping("/all/citys")
	public List<PmsCity> getCitys() {
		//return cityService.getAll();
		return pmsCityFacade.getAll();
	}

	@RequestMapping("/get/province")
	public PmsProvince getProvince(String provinceId,HttpServletRequest request) {
		if (ValidateUtil.isValid(provinceId)) {
			//return provinceService.findProvinceById(provinceId);
			return pmsProvinceFacade.findProvinceById(provinceId);
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("province is null ...",sessionInfo);
			return new PmsProvince();
		}
	}
}
