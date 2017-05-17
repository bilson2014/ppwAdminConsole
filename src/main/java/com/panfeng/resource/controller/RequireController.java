package com.panfeng.resource.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.product.entity.PmsProductCase;
import com.paipianwang.pat.facade.product.entity.PmsRequire;
import com.paipianwang.pat.facade.product.service.PmsRequireFacade;
import com.panfeng.domain.BaseMsg;

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

	@RequestMapping("/require/updatepm")
	public BaseMsg updatePM(String requireIds, Long employeeId) {
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.ERROR);
		baseMsg.setErrorMsg("更新失败！");

		if (ValidateUtil.isValid(requireIds)) {
			String[] split = requireIds.split(",");
			if (split != null && split.length > 0) {
				for (int i = 0; i < split.length; i++) {
					String id = split[i];
					if (!"".equals(id) && !"".equals(id.trim())) {
						Long requireId = Long.valueOf(id);
						PmsRequire require = new PmsRequire();
						require.setRequireId(requireId);
						require.setEmployeeId(employeeId);
						pmsRequireFacade.updatePM(require);
					}
				}
				baseMsg.setCode(BaseMsg.NORMAL);
				baseMsg.setErrorMsg("更新成功！");
			}
		}
		return baseMsg;
	}

	@RequestMapping("/require/delete")
	public BaseMsg delete(String requireIds) {
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.ERROR);
		baseMsg.setErrorMsg("删除失败！");

		if (ValidateUtil.isValid(requireIds)) {
			String[] split = requireIds.split(",");
			if (split != null && split.length > 0) {
				for (int i = 0; i < split.length; i++) {
					String id = split[i];
					if (!"".equals(id) && !"".equals(id.trim())) {
						Long requireId = Long.valueOf(id);
						pmsRequireFacade.delete(requireId);
					}
				}
				baseMsg.setCode(BaseMsg.NORMAL);
				baseMsg.setErrorMsg("更新成功！");
			}
		}
		return baseMsg;
	}

}
