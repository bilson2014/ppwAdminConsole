package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.facade.product.entity.PmsProductModule;
import com.paipianwang.pat.facade.product.service.PmsProductModuleFacade;
import com.panfeng.resource.model.ProductModule;
import com.panfeng.service.ProductModuleService;

/**
 * 产品模块化
 */
@RestController
@RequestMapping("/portal")
public class ProductModuleController extends BaseController {

	@Autowired
	private ProductModuleService pmService;
	@Autowired
	private PmsProductModuleFacade pmsProductModuleFacade;

	@RequestMapping(value = "/module-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("module-list", model);
	}

	@RequestMapping(value = "/module/list")
	public List<ProductModule> list() {
		return pmService.list();
	}

	@RequestMapping(value = "/module/save")
	public boolean save(ProductModule productModule, @RequestParam final MultipartFile moduleImg,
			final HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		return pmService.save(productModule, moduleImg);
	}

	@RequestMapping(value = "/module/update")
	public boolean update(ProductModule productModule, @RequestParam final MultipartFile moduleImg,
			final HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		return pmService.update(productModule, moduleImg);
	}

	@RequestMapping("/module/delete")
	public long delete(final long[] ids) {
		return pmService.delete(ids);
	}

	@RequestMapping(value = "/get/productModules")
	public List<PmsProductModule> getproductModules(HttpServletRequest request,@RequestBody Map<String, Long[]> idMap) {
		if (idMap != null && idMap.size() > 0) {
			Long[] ids = idMap.get("ids");
			if (ids != null && ids.length > 0) {
				//return pmService.findListByIds(ids);
				return pmsProductModuleFacade.findListByIds(ids);
			}
		}
		return new ArrayList<>();
	}
}
