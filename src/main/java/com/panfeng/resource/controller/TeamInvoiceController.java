package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.DateUtils;
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
import com.panfeng.service.TeamInvoiceService;
import com.panfeng.util.Log;

@RestController
@RequestMapping("/portal")
public class TeamInvoiceController extends BaseController {

	@Autowired
	private PmsTeamInvoiceFacade pmsTeamInvoiceFacade = null;
	
	@Autowired
	private TeamInvoiceService teamInvoiceService = null;
	
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
		paramMap.put("invoiceBeginDate", view.getInvoiceBeginDate());
		paramMap.put("invoiceEndDate", view.getInvoiceEndDate());
		
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
	
	/**
	 * 报表导出
	 * @param view
	 * @param pageParam
	 */
	@RequestMapping("/invoice/team/export")
	public void export(final InvoiceView view, final PageParam pageParam, HttpServletRequest request,final HttpServletResponse response) {
		OutputStream outputStream = null;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			String dateString = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
			String filename = "供应商发票管理" + dateString + ".xlsx";
			//---处理文件名
			String userAgent = request.getHeader("User-Agent"); 		
			if (userAgent.contains("MSIE")||userAgent.contains("Trident") ||userAgent.contains("Edge")) {
				//针对IE或者以IE为内核或Microsoft Edge的浏览器：
				filename = java.net.URLEncoder.encode(filename, "UTF-8");
			} else {
				//非IE浏览器的处理：
				filename = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
			}
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"\r\n");
			outputStream = response.getOutputStream();
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("invoiceProjectId", view.getInvoiceProjectId());
			paramMap.put("invoiceType", view.getInvoiceType());
			paramMap.put("invoiceCode", view.getInvoiceCode());
			paramMap.put("invoiceBeginDate", view.getInvoiceBeginDate());
			paramMap.put("invoiceEndDate", view.getInvoiceEndDate());
			
			// 数据
			List<PmsTeamInvoice> list = pmsTeamInvoiceFacade.findTeamInvoiceWithCondition(paramMap);
			
			teamInvoiceService.generateReport(list,outputStream);
			
			outputStream.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
