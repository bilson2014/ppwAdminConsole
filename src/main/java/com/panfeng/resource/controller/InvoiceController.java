package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.Invoice;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.InvoiceView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.InvoiceService;
import com.panfeng.util.ValidateUtil;

@RestController
@RequestMapping("/portal")
public class InvoiceController extends BaseController {

	@Autowired
	private final InvoiceService service = null;

	@RequestMapping("/invoice-customlist")
	public ModelAndView customview(final HttpServletRequest request) {

		return new ModelAndView("invoice-customlist");
	}
	
	@RequestMapping("/invoice-teamlist")
	public ModelAndView teamlist(final HttpServletRequest request) {

		return new ModelAndView("invoice-teamlist");
	}

	/**
	 * 客户发票查询
	 */
	@RequestMapping(value = "/invoice/custom/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<Invoice> custromList(final InvoiceView view, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		view.setInvoiceType(0); // 客户发票

		final List<Invoice> list = service.listWithPagination(view);
		final long total = service.maxSize(view);
		final DataGrid<Invoice> dataGrid = new DataGrid<Invoice>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	/**
	 * 供应商发票查询
	 */
	@RequestMapping(value = "/invoice/team/list", method = RequestMethod.POST, 
			produces = "application/json; charset=UTF-8")
	public DataGrid<Invoice> teamList(final InvoiceView view, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		view.setInvoiceType(1); // 供应商发票

		final List<Invoice> list = service.listWithPagination(view);
		final long total = service.maxSize(view);
		final DataGrid<Invoice> dataGrid = new DataGrid<Invoice>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping("/invoice/update")
	public long update(final Invoice invoice){
	
		final long ret = service.update(invoice);
		return ret;
	}
	
	@RequestMapping("/invoice/save")
	public long save(final Invoice invoice){

		final long ret = service.save(invoice);
		return ret;
	}
	
	@RequestMapping("/invoice/delete")
	public long delete(final long[] ids){
		
		if(ValidateUtil.isValid(ids)){
			
			final long ret = service.deleteByIds(ids);
			return ret;
		}
		
		return 0l;	
	}
	
}
