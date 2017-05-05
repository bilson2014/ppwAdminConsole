package com.panfeng.resource.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.facade.product.entity.PmsProductCase;
import com.paipianwang.pat.facade.product.entity.PmsRequire;
import com.paipianwang.pat.facade.product.service.PmsRequireFacade;

@RestController
@RequestMapping("/portal")
public class RequireController extends BaseController {

	@Autowired
	private PmsRequireFacade pmsRequireFacade;

	@RequestMapping("/require-list")
	public ModelAndView activityView() {
		return new ModelAndView("/require-list");
	}

	@RequestMapping("/require/list")
	public DataGrid<PmsRequire> getAll(final PmsProductCase view, final PageParam param) {
		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", view.getpName());
		final DataGrid<PmsRequire> dataGrid = pmsRequireFacade.listWithPagination(param, paramMap);
		return dataGrid;
	}

//	@RequestMapping("/chanpin/save")
//	public BaseMsg save(PmsChanPin chanPin, HttpServletRequest request) {
//		Map<String, Object> save = pmsChanPinFacade.save(chanPin);
//		Map<String, String[]> sceneTag = request.getParameterMap();
//		String[] tags = sceneTag.get("sceneTag");
//		if (tags != null && tags.length > 0) {
//			Long id = (Long) save.get(BaseEntity.SAVE_MAP_ID);
//			List<PmsScene> list = new ArrayList<>();
//			for (int i = 0; i < tags.length; i++) {
//				PmsScene ps = new PmsScene();
//				ps.setSceneId(Long.valueOf(tags[i]));
//				list.add(ps);
//			}
//			pmsChanPinFacade.updateChanpinScene(id, list);
//		}
//		BaseMsg baseMsg = new BaseMsg();
//		baseMsg.setCode(BaseMsg.NORMAL);
//		baseMsg.setResult(save.get(BaseEntity.SAVE_MAP_ROWS));
//		return baseMsg;
//	}
//
//	@RequestMapping("/chanpin/update")
//	public BaseMsg update(PmsChanPin chanpin, HttpServletRequest request) {
//		long update = pmsChanPinFacade.update(chanpin);
//
//		Map<String, String[]> sceneTag = request.getParameterMap();
//		String[] tags = sceneTag.get("sceneTag");
//		if (tags != null && tags.length > 0) {
//			List<PmsScene> list = new ArrayList<>();
//			for (int i = 0; i < tags.length; i++) {
//				PmsScene ps = new PmsScene();
//				ps.setSceneId(Long.valueOf(tags[i]));
//				list.add(ps);
//			}
//			pmsChanPinFacade.updateChanpinScene(chanpin.getChanpinId(), list);
//		}
//		BaseMsg baseMsg = new BaseMsg();
//		baseMsg.setCode(BaseMsg.NORMAL);
//		baseMsg.setResult(update);
//		return baseMsg;
//	}
//
//	@RequestMapping("/chanpin/delete")
//	public BaseMsg delete(String ids) {
//		BaseMsg baseMsg = new BaseMsg();
//		if (ValidateUtil.isValid(ids)) {
//			String[] id = ids.split(",");
//			for (int i = 0; i < id.length; i++) {
//				pmsChanPinFacade.delete(Long.valueOf(id[i]));
//			}
//			baseMsg.setCode(BaseMsg.NORMAL);
//
//		} else {
//			baseMsg.setCode(BaseMsg.ERROR);
//			baseMsg.setErrorMsg("id不存在！");
//		}
//		return baseMsg;
//	}
//
//	@RequestMapping("/chanpin/scene/list/{chanpinId}")
//	public BaseMsg getScene(@PathVariable("chanpinId") Long chanpinId) {
//		BaseMsg baseMsg = new BaseMsg();
//		DataGrid<PmsScene> allScene = pmsSceneFacade.getAllScene();
//		List<PmsScene> pList = pmsSceneFacade.getSceneByChanPinId(chanpinId);
//		if (ValidateUtil.isValid(pList) && allScene != null && ValidateUtil.isValid(allScene.getRows())) {
//			for (PmsScene pmsScene : pList) {
//				for (PmsScene p : allScene.getRows()) {
//					Long pListSceneId = pmsScene.getSceneId();
//					Long sceneId = p.getSceneId();
//					if (pListSceneId.equals(sceneId)) {
//						p.setChecked(true);
//						break;
//					}
//				}
//			}
//		}
//		baseMsg.setErrorCode(BaseMsg.NORMAL);
//		baseMsg.setResult(allScene);
//		return baseMsg;
//	}

}
