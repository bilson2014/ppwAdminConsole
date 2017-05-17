package com.panfeng.resource.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.BaseEntity;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.product.entity.PmsProductCase;
import com.paipianwang.pat.facade.product.entity.PmsScene;
import com.paipianwang.pat.facade.product.service.PmsProductCaseFacade;
import com.paipianwang.pat.facade.product.service.PmsSceneFacade;
import com.panfeng.domain.BaseMsg;

/**
 * 产品案例Controller
 * 
 * @author wang
 *
 */
@RestController
@RequestMapping("/portal")
public class ProductCaseController extends BaseController {

	@Autowired
	private PmsProductCaseFacade productcase;

	@Autowired
	private PmsSceneFacade pmsSceneFacade;

	@RequestMapping("/product-case-list")
	public ModelAndView activityView() {
		return new ModelAndView("/product-case-list");
	}

	@RequestMapping("/case/save")
	public BaseMsg save(PmsProductCase productCase, HttpServletRequest request) {
		Map<String, String[]> sceneTag = request.getParameterMap();
		String[] tags = sceneTag.get("sceneTag");
		if (tags != null && tags.length > 0) {
			String join = StringUtils.join(tags, ',');
			productCase.setpScene(join);
		} else {
			productCase.setpScene("");
		}
		Map<String, Object> save = productcase.save(productCase);
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setErrorCode(BaseMsg.ERROR);
		baseMsg.setErrorMsg("保存失败！");
		if (save != null) {
			Object object = save.get(BaseEntity.SAVE_MAP_ROWS);
			if (object != null) {
				Long row = (Long) object;
				if (row > 0) {
					baseMsg.setErrorCode(BaseMsg.NORMAL);
					baseMsg.setErrorMsg("保存成功！");
				}
			}
		}
		return baseMsg;
	}

	@RequestMapping("/case/update")
	public BaseMsg update(PmsProductCase productCase, HttpServletRequest request) {
		Map<String, String[]> sceneTag = request.getParameterMap();
		String[] tags = sceneTag.get("sceneTag");
		if (tags != null && tags.length > 0) {
			String join = StringUtils.join(tags, ',');
			productCase.setpScene(join);
		} else {
			productCase.setpScene("");
		}
		long update = productcase.update(productCase);
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setErrorCode(BaseMsg.ERROR);
		baseMsg.setErrorMsg("更新失败！");
		if (update > 0) {
			baseMsg.setErrorCode(BaseMsg.NORMAL);
			baseMsg.setErrorMsg("更新成功！");
		}
		return baseMsg;
	}

	@RequestMapping("/case/delete")
	public BaseMsg delete(String ids) {
		BaseMsg baseMsg = new BaseMsg();
		if (ValidateUtil.isValid(ids)) {
			String[] id = ids.split(",");
			for (int i = 0; i < id.length; i++) {
				productcase.delete(Long.valueOf(id[i]));
			}
			baseMsg.setCode(BaseMsg.NORMAL);

		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("id不存在！");
		}
		return baseMsg;
	}

	@RequestMapping("/case/list")
	public DataGrid<PmsProductCase> getAll(final PmsProductCase view, final PageParam param) {
		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pName", view.getpName());
		final DataGrid<PmsProductCase> dataGrid = productcase.listWithPagination(param, paramMap);
		return dataGrid;
	}

	@RequestMapping("/case/scene/list")
	public BaseMsg getScene(Long caseId) {
		BaseMsg baseMsg = new BaseMsg();
		DataGrid<PmsScene> allScene = pmsSceneFacade.getAllScene();
		PmsProductCase caseInfo = productcase.getCaseInfo(caseId);
		if (caseInfo != null) {
			String sceneTag = caseInfo.getpScene();
			if (ValidateUtil.isValid(sceneTag)) {
				String[] split = sceneTag.split("\\,");
				if (split != null && split.length > 0) {
					for (int i = 0; i < split.length; i++) {
						for (PmsScene pmsScene : allScene.getRows()) {
							if (pmsScene.getSceneName().equals(split[i])) {
								pmsScene.setChecked(true);
								break;
							}
						}
					}
				}
			}
		}
		baseMsg.setErrorCode(BaseMsg.NORMAL);
		baseMsg.setResult(allScene);
		return baseMsg;
	}

}
