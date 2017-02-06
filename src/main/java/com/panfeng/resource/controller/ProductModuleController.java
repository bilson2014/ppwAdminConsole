package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.panfeng.service.FDFSService;

/**
 * 产品模块化
 */
@RestController
@RequestMapping("/portal")
public class ProductModuleController extends BaseController {

	@Autowired
	private PmsProductModuleFacade pmsProductModuleFacade;
	@Autowired
	private FDFSService fdfsService;

	@RequestMapping(value = "/module-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("module-list", model);
	}

	@RequestMapping(value = "/module/list")
	public List<PmsProductModule> list() {
		return pmsProductModuleFacade.list();
	}

	@RequestMapping(value = "/module/save")
	public boolean save(PmsProductModule productModule, @RequestParam final MultipartFile moduleImg,
			final HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		if(!moduleImg.isEmpty()){
			//上传图片
			String fileId = fdfsService.upload(moduleImg);
			productModule.setPic(fileId);
		}
		if(productModule.getPid() == null){
			productModule.setPid(0);
		}
		productModule.setSortIndex(0);
		return pmsProductModuleFacade.save(productModule)>0;
	}

	@RequestMapping(value = "/module/update")
	public boolean update(PmsProductModule productModule, @RequestParam final MultipartFile moduleImg,
			final HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		if(!moduleImg.isEmpty()){
			//删除以前的图片
			delOldPicById(productModule.getId());
			String fileId = fdfsService.upload(moduleImg);
			productModule.setPic(fileId);
		}
		if(productModule.getPid() == null){
			productModule.setPid(0);
		}
		productModule.setSortIndex(0);
		return pmsProductModuleFacade.update(productModule)>0;
	}
	private void delOldPicById(long pmId) {
		String oldPath = pmsProductModuleFacade.getPic(pmId);
		if(StringUtils.isNotBlank(oldPath)){
			fdfsService.delete(oldPath);
		}
	}

	@RequestMapping("/module/delete")
	public long delete(final long[] ids) {
		if(ids != null && ids.length > 0){
			for (final long id : ids) {
				String str = pmsProductModuleFacade.getchild(id);
				String[] delId = str.split(",");
				for(final String i : delId){
					delOldPicById(Long.valueOf(i));
					pmsProductModuleFacade.delete(Long.valueOf(i));
				}
			}
			return 1l;
		}
		return 0l;
	}

	@RequestMapping(value = "/get/productModules")
	public List<PmsProductModule> getproductModules(HttpServletRequest request,@RequestBody Map<String, Long[]> idMap) {
		if (idMap != null && idMap.size() > 0) {
			Long[] ids = idMap.get("ids");
			if (ids != null && ids.length > 0) {
				return pmsProductModuleFacade.findListByIds(ids);
			}
		}
		return new ArrayList<>();
	}
}
