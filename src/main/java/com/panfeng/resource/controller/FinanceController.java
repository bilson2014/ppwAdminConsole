package com.panfeng.resource.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.DealLog;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.FinanceView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.FinanceService;
import com.panfeng.util.ValidateUtil;

@RestController
@RequestMapping("/portal")
public class FinanceController extends BaseController{

	private final FinanceService service = null;
	
	private static final Logger logger = LoggerFactory.getLogger("error");
	
	@RequestMapping("/finance-list")
	public ModelAndView view(final ModelMap model){
		
		return new ModelAndView("finance-list",model);
	}
	
	@RequestMapping(value = "/finance/list",method = RequestMethod.POST,
					produces = "application/json; charset=UTF-8")
	public DataGrid<DealLog> list(final FinanceView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		final List<DealLog> list = service.listWithPagination(view);
		final long total = service.maxSize(view);
		final DataGrid<DealLog> dataGrid = new DataGrid<DealLog>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping(value = "/finance/save", method = RequestMethod.POST)
	public long save(final DealLog dealLog){
		
		final long ret = service.save(dealLog);
		return ret;
	}
	
	@RequestMapping(value = "/finance/update", method = RequestMethod.POST)
	public long update(final DealLog dealLog){
		
		final long ret = service.update(dealLog);
		return ret;
	}
	
	@RequestMapping(value = "/finance/delete", method = RequestMethod.POST)
	public long delete(final long[] ids){
		
		if(ValidateUtil.isValid(ids)){
			return service.deleteByArray(ids);
		} else {
			logger.error("finance Delete Error ...");
			throw new RuntimeException("finance Delete Error ...");
		}
	}
}
