package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.UserInvoice;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.InvoiceView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.UserInvoiceService;
import com.panfeng.util.Constants;
import com.panfeng.util.ValidateUtil;

@RestController
@RequestMapping("/portal")
public class UserInvoiceController extends BaseController {

	@Autowired
	private final UserInvoiceService service = null;

	@RequestMapping("/invoice-customlist")
	public ModelAndView customview(final HttpServletRequest request) {

		return new ModelAndView("invoice-customlist");
	}
	/**
	 * 客户发票查询
	 */
	@RequestMapping(value = "/invoice/custom/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<UserInvoice> custromList(final InvoiceView view, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);

		final List<UserInvoice> list = service.listWithPagination(view);
		final long total = service.maxSize(view);
		final DataGrid<UserInvoice> dataGrid = new DataGrid<UserInvoice>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	@RequestMapping("/invoice/update")
	public long update(final UserInvoice invoice){
		if(invoice.getInvoiceStatus() != Constants.INVOICE_STATUS_OK){//审核通过的发票不能修改
			final long ret = service.update(invoice);
			return ret;
		}
		return 0l;
	}
	
	@RequestMapping("/invoice/save")
	public long save(final UserInvoice invoice){

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
	/**
	 * 审批
	 */
	@RequestMapping("/invoice/user/auditing")
	public long auditing(final UserInvoice invoice){
		return service.auditing(invoice);
	}
}
