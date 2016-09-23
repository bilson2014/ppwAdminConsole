package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.TeamInvoice;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.InvoiceView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.TeamInvoiceService;
import com.panfeng.util.Constants;
import com.panfeng.util.Log;
import com.panfeng.util.ValidateUtil;

@RestController
@RequestMapping("/portal")
public class TeamInvoiceController extends BaseController {

	@Autowired
	private final TeamInvoiceService service = null;

	@RequestMapping("/invoice-teamlist")
	public ModelAndView teamlist(final HttpServletRequest request) {

		return new ModelAndView("invoice-teamlist");
	}
	/**
	 * 供应商发票查询
	 */
	@RequestMapping(value = "/invoice/team/list", method = RequestMethod.POST, 
			produces = "application/json; charset=UTF-8")
	public DataGrid<TeamInvoice> teamList(final InvoiceView view, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);

		final List<TeamInvoice> list = service.listWithPagination(view);
		final long total = service.maxSize(view);
		final DataGrid<TeamInvoice> dataGrid = new DataGrid<TeamInvoice>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping("/invoice/team/update")
	public long update(final TeamInvoice invoice,HttpServletRequest request){
		if(invoice.getInvoiceStatus() != Constants.INVOICE_STATUS_OK){//审核通过的单子不能修改
			final long ret = service.update(invoice);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("update  TeamInvoice ...",sessionInfo);
			return ret;
		}
		return 0l;
	}
	
	@RequestMapping("/invoice/team/save")
	public long save(final TeamInvoice invoice,HttpServletRequest request){

		final long ret = service.save(invoice);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save  TeamInvoice ...",sessionInfo);
		return ret;
	}
	
	@RequestMapping("/invoice/team/delete")
	public long delete(final long[] ids,HttpServletRequest request){
		if(ValidateUtil.isValid(ids)){
			final long ret = service.deleteByIds(ids);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("delete  TeamInvoice ... ids:"+ids.toString(),sessionInfo);
			return ret;
		}
		return 0l;	
	}
	
	/**
	 * 审批
	 */
	@RequestMapping("/invoice/team/auditing")
	public long auditing(final TeamInvoice invoice){
		return service.auditing(invoice);
	}
	
}
