package com.panfeng.resource.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.finance.entity.PmsDealLog;
import com.paipianwang.pat.facade.finance.service.PmsFinanceFacade;
import com.panfeng.resource.model.DealLog;
import com.panfeng.resource.view.FinanceView;
import com.panfeng.service.DealLogService;
import com.panfeng.util.Log;

@RestController
@RequestMapping("/portal")
public class FinanceController extends BaseController{

	//private static final Logger logger = LoggerFactory.getLogger("error");

	@Autowired
	private final DealLogService dealLogService = null; 
	
	@Autowired
	private final PmsFinanceFacade pmsFinanceFacade = null; 
	
	
	@RequestMapping("/finance-list")
	public ModelAndView view(final ModelMap model){
		
		return new ModelAndView("finance-list",model);
	}
	
	@RequestMapping("/finance-offline")
	public ModelAndView offlineView(final ModelMap model){
		
		return new ModelAndView("finance-offline",model);
	}
	
	@RequestMapping(value = "/finance/list",method = RequestMethod.POST,
					produces = "application/json; charset=UTF-8")
	public DataGrid<PmsDealLog> list(final FinanceView view,final PageParam pageParam){
		
		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("logType", view.getLogType());
		paramMap.put("dealLogSource",view.getDealLogSource());
		final DataGrid<PmsDealLog> dataGrid = pmsFinanceFacade.listWithPagination(pageParam,paramMap);
		return dataGrid;
	}
	
	/**
	 * 线下支付列表
	 */
	@RequestMapping(value = "/finance-offline/list",method = RequestMethod.POST,
			produces = "application/json; charset=UTF-8")
	public DataGrid<PmsDealLog> offlineList(final FinanceView view,final PageParam pageParam){
	
	final long page = pageParam.getPage();
	final long rows = pageParam.getRows();
	pageParam.setBegin((page - 1) * rows);
	pageParam.setLimit(rows);
	
	Map<String, Object> paramMap = new HashMap<>();
	paramMap.put("dealLogSource",1);		
	paramMap.put("logType", view.getLogType());
	paramMap.put("beginTime", view.getBeginTime());
	paramMap.put("endTime", view.getEndTime());
	final DataGrid<PmsDealLog> dataGrid = pmsFinanceFacade.listWithPagination(pageParam,paramMap);
	return dataGrid;
	}
	
	@RequestMapping(value = "/finance/save", method = RequestMethod.POST)
	public long save(final DealLog dealLog,HttpServletRequest request){
		dealLog.setDealLogSource(1); // 线下支付
		dealLog.setDealStatus(1); // 线下支付默认支付成功
		dealLog.setUserType(PmsConstant.ROLE_CUSTOMER);
		final Long projectId = dealLog.getProjectId();
		
		if(projectId != null){
			
			final DealLog newDealLog = dealLogService.getDefaultDeal(projectId);
			dealLog.setBillNo(newDealLog.getBillNo());
		}
		 //TODO
		//此处有支付服务(还没开始写)，暂时处理成json转换
		Gson gson = new Gson();
		String json = gson.toJson(dealLog);
		final long ret = pmsFinanceFacade.save(gson.fromJson(json, PmsDealLog.class));
		
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("finance save :"+ dealLog.toString(), sessionInfo);
		return ret;
	}
	
	@RequestMapping(value = "/finance/update", method = RequestMethod.POST)
	public long update(final PmsDealLog dealLog,HttpServletRequest request){
		
		dealLog.setDealLogSource(1); // 线下支付
		dealLog.setDealStatus(1); // 线下支付默认支付成功
		dealLog.setUserType(PmsConstant.ROLE_CUSTOMER);
		
		final long ret = pmsFinanceFacade.update(dealLog);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("finance update :"+ dealLog.toString(), sessionInfo);
		return ret;
	}
	
	@RequestMapping(value = "/finance/delete", method = RequestMethod.POST)
	public long delete(final long[] ids,HttpServletRequest request){
		SessionInfo sessionInfo = getCurrentInfo(request);
		if(ValidateUtil.isValid(ids)){
			Log.error("finance delete ids:"+ ids.toString() , sessionInfo);
			return pmsFinanceFacade.deleteByArray(ids);
		} else {
			Log.error("finance Delete Error ...",sessionInfo);
			throw new RuntimeException("finance Delete Error ...");
		}
	}
}