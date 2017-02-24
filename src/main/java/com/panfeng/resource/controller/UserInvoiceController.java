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
import com.paipianwang.pat.common.util.Constants;
import com.paipianwang.pat.facade.finance.entity.PmsUserInvoice;
import com.paipianwang.pat.facade.finance.service.PmsUserInvoiceFacade;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.view.InvoiceView;
import com.panfeng.util.Log;
import com.panfeng.util.ValidateUtil;

@RestController
@RequestMapping("/portal")
public class UserInvoiceController extends BaseController {

	@Autowired
	private PmsUserInvoiceFacade pmsUserInvoiceFacade = null;

	@RequestMapping("/invoice-customlist")
	public ModelAndView customview(final HttpServletRequest request) {

		return new ModelAndView("invoice-customlist");
	}
	/**
	 * 客户发票查询
	 */
	@RequestMapping(value = "/invoice/custom/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsUserInvoice> custromList(final InvoiceView view, final PageParam pageParam) {

		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invoiceProjectId", view.getInvoiceProjectId());
		paramMap.put("invoiceType", view.getInvoiceType());
		paramMap.put("invoiceCode", view.getInvoiceCode());
		
		final DataGrid<PmsUserInvoice> dataGrid = pmsUserInvoiceFacade.listWithPagination(pageParam,paramMap);
		return dataGrid;
	}
	@RequestMapping("/invoice/update")
	public long update(final PmsUserInvoice invoice,HttpServletRequest request){
		if(invoice.getInvoiceStatus() != Constants.INVOICE_STATUS_OK){//审核通过的发票不能修改
			final long ret = pmsUserInvoiceFacade.update(invoice);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("delete UserInvoice...",sessionInfo);
			return ret;
		}
		return 0l;
	}
	
	@RequestMapping("/invoice/save")
	public long save(final PmsUserInvoice invoice,HttpServletRequest request){

		final long ret = pmsUserInvoiceFacade.save(invoice);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save UserInvoice...",sessionInfo);
		return ret;
	}
	
	@RequestMapping("/invoice/delete")
	public long delete(final long[] ids,HttpServletRequest request){
		
		if(ValidateUtil.isValid(ids)){
			
			final long ret = pmsUserInvoiceFacade.deleteByIds(ids);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("delete UserInvoice...",sessionInfo);
			return ret;
		}
		
		return 0l;	
	}
	/**
	 * 审批
	 */
	@RequestMapping("/invoice/user/auditing")
	public long auditing(final PmsUserInvoice invoice){
		return pmsUserInvoiceFacade.auditing(invoice);
	}
}
