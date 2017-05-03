package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.team.entity.PmsCity;
import com.paipianwang.pat.facade.team.entity.PmsProvince;
import com.paipianwang.pat.facade.team.service.PmsCityFacade;
import com.paipianwang.pat.facade.team.service.PmsProvinceFacade;

import com.panfeng.util.Log;

/**
 * 省市选择器
 * 
 * @author wang
 *
 */
@RequestMapping("/portal")
@RestController
public class CityPickerController extends BaseController {

	@Autowired
	private PmsProvinceFacade pmsProvinceFacade;

	@Autowired
	private PmsCityFacade pmsCityFacade;

	/**
	 * 获取所有省
	 * @return
	 */
	@RequestMapping("/get/provinces")
	public List<PmsProvince> getAllProvince() {
		return pmsProvinceFacade.getAll();
	}

	@RequestMapping("/get/citys/{provinceId}")
	public List<PmsCity> getCitys(@PathVariable("provinceId") String provinceId, HttpServletRequest request) {
		if (ValidateUtil.isValid(provinceId)) {
			return pmsCityFacade.findCitysByProvinceId(provinceId);
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("provinceId is null ...", sessionInfo);
			return new ArrayList<>();
		}
	}

	/**
	 * 获取所有城市实体
	 * @return
	 */
	@RequestMapping("/all/citys")
	public List<PmsCity> getCitys() {
		return pmsCityFacade.getAll();
	}

	@RequestMapping("/get/province")
	public PmsProvince getProvince(String provinceId, HttpServletRequest request) {
		if (ValidateUtil.isValid(provinceId)) {
			return pmsProvinceFacade.findProvinceById(provinceId);
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("province is null ...", sessionInfo);
			return new PmsProvince();
		}
	}
}
