package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.DIffBean;
import com.panfeng.resource.model.ProductTmp;
import com.panfeng.resource.model.TeamTmp;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.MailView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.Pagination;
import com.panfeng.service.ProductTmpService;
import com.panfeng.service.TeamTmpService;

/**
 * 供应商作品审核
 */
@RestController
@RequestMapping("/portal")
public class ProductTmpController extends BaseController {
	
	@Autowired
	private final ProductTmpService service = null;
	@RequestMapping(value = "/productTmp-list")
	public ModelAndView view(final ModelMap model) {
		return new ModelAndView("productTmp-list", model);
	}
	@RequestMapping(value = "/productTmp/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<ProductTmp> list(final Pagination view, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);

		DataGrid<ProductTmp> dataGrid = new DataGrid<ProductTmp>();
		final List<ProductTmp> list = service.listWithPagination(view);
		dataGrid.setRows(list);
		final long total = service.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	//@RequestMapping(value = "/teamTmp/update", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	/*public void update(TeamTmp teamTmp,HttpServletRequest reqeust) {
		service.updateTeamTmp(teamTmp);
	}
	*//**
	 * 查询team和teamTmp的差异字段
	 *//*
//	@RequestMapping(value = "/teamTmp/find/diff/{teamId}")
	public List<DIffBean> findDiffTeam(@PathVariable("teamId")Integer teamId,HttpServletRequest reqeust) {
		return service.findDiffTeam(teamId);
	}*/
}
