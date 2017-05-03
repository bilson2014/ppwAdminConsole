package com.panfeng.resource.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.Constants;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.finance.entity.PmsTeamInvoice;
import com.paipianwang.pat.facade.finance.service.PmsTeamInvoiceFacade;
import com.panfeng.resource.view.InvoiceView;
import com.panfeng.util.Log;

@RestController
@RequestMapping("/portal")
public class TeamInvoiceController extends BaseController {

	@Autowired
	private PmsTeamInvoiceFacade pmsTeamInvoiceFacade = null;

	@RequestMapping("/invoice-teamlist")
	public ModelAndView teamlist(final HttpServletRequest request) {

		return new ModelAndView("invoice-teamlist");
	}
	/**
	 * 供应商发票查询
	 */
	@RequestMapping(value = "/invoice/team/list", method = RequestMethod.POST, 
			produces = "application/json; charset=UTF-8")
	public DataGrid<PmsTeamInvoice> teamList(final InvoiceView view, final PageParam pageParam) {

		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invoiceProjectId", view.getInvoiceProjectId());
		paramMap.put("invoiceType", view.getInvoiceType());
		paramMap.put("invoiceCode", view.getInvoiceCode());
		
		final DataGrid<PmsTeamInvoice> dataGrid = pmsTeamInvoiceFacade.listWithPagination(pageParam,paramMap);
		return dataGrid;
	}
	
	@RequestMapping("/invoice/team/update")
	public long update(final PmsTeamInvoice invoice,HttpServletRequest request){
		if(invoice.getInvoiceStatus() != Constants.INVOICE_STATUS_OK){//审核通过的单子不能修改
			final long ret = pmsTeamInvoiceFacade.update(invoice);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("update  TeamInvoice ...",sessionInfo);
			return ret;
		}
		return 0l;
	}
	
	@RequestMapping("/invoice/team/save")
	public long save(final PmsTeamInvoice invoice,HttpServletRequest request){

		final long ret = pmsTeamInvoiceFacade.save(invoice);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save  TeamInvoice ...",sessionInfo);
		return ret;
	}
	
	@RequestMapping("/invoice/team/delete")
	public long delete(final long[] ids,HttpServletRequest request){
		if(ValidateUtil.isValid(ids)){
			final long ret = pmsTeamInvoiceFacade.deleteByIds(ids);
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
	public long auditing(final PmsTeamInvoice invoice){
		return pmsTeamInvoiceFacade.auditing(invoice);
	}
	
}
