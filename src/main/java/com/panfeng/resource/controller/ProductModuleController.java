package com.panfeng.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.ProductModule;
import com.panfeng.service.ProductModuleService;

/**
 * 产品模块化
 */
@RestController
@RequestMapping("/portal")
public class ProductModuleController extends BaseController{
	
	@Autowired
	private ProductModuleService pmService;
	
	@RequestMapping(value = "/module-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("module-list", model);
	}
	
	@RequestMapping(value = "/module/list")
	public List<ProductModule> list() {
		return pmService.list();
	}
	
	@RequestMapping(value = "/module/save")
	public boolean save(@RequestBody ProductModule productModule) {
		return pmService.save(productModule);
	}
	@RequestMapping(value = "/module/update")
	public boolean update(@RequestBody ProductModule productModule) {
		return pmService.update(productModule);
	}
	
	@RequestMapping("/module/delete")
	public long delete(final long[] ids){
		return pmService.delete(ids);
	}
}
