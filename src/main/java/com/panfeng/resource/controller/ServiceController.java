package com.panfeng.resource.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.product.entity.PmsProduct;
import com.paipianwang.pat.facade.product.entity.PmsService;
import com.paipianwang.pat.facade.product.service.PmsProductFacade;
import com.paipianwang.pat.facade.product.service.PmsServiceFacade;
import com.panfeng.resource.view.ServiceView;
import com.panfeng.util.Log;

/**
 * 服务相关
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class ServiceController extends BaseController{

	@Autowired
	private final PmsServiceFacade pmsServiceFacade = null;
	
	@Autowired
	private final PmsProductFacade pmsProductFacade = null;
	
	@RequestMapping("/service-list")
	public ModelAndView view(final ModelMap model){
		
		return new ModelAndView("service-list",model);
	}
	
	@RequestMapping(value = "/service/list")
	public DataGrid<PmsService> list(final ServiceView view,final PageParam param){
		
		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("productName", view.getProductName());
		final DataGrid<PmsService> dataGrid = pmsServiceFacade.listWithPagination(param,paramMap);
		return dataGrid;
	}
	
	@RequestMapping(value = "/service/delete",method = RequestMethod.POST,
					produces = "application/json; charset=UTF-8")
	public long delete(final long[] ids,HttpServletRequest request){
		
		final long ret = pmsServiceFacade.delete(ids);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete service ... ids:"+ids.toString(),sessionInfo);
		return ret;
	}
	
	/**
	 * 获取全部产品,填充下拉列表
	 */
	@RequestMapping(value = "/service/productSelect",method = RequestMethod.POST,
					produces = "application/json; charset=UTF-8")
	public String select(){
		final List<PmsProduct> list = pmsProductFacade.all();
		JsonArray jaArray = new JsonArray();
		if(ValidateUtil.isValid(list)){
			for (PmsProduct product : list) {
				JsonObject jo = new JsonObject();
				jo.addProperty("productId", product.getProductId());
				jo.addProperty("productName", product.getProductName());
				jaArray.add(jo);
			}
		}
		return jaArray.toString();
	}
	
	@RequestMapping(value = "/service/save",method = RequestMethod.POST)
	public long save(final PmsService service,HttpServletRequest request){
		final double price = service.getServicePrice();
		final double discount = service.getServiceDiscount();
		final double realPrice = price * discount;
		BigDecimal bg = new BigDecimal(realPrice);
		final double roundRealPrice = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		service.setServiceRealPrice(roundRealPrice);
		final long ret = pmsServiceFacade.save(service);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save service ...",sessionInfo);
		return ret;
	}
	@RequestMapping(value = "/service/update",method = RequestMethod.POST)
	public long update(final PmsService service,HttpServletRequest request){
		
		final double price = service.getServicePrice();
		final double discount = service.getServiceDiscount();
		final double realPrice = price * discount;
		BigDecimal bg = new BigDecimal(realPrice);
		final double roundRealPrice = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		service.setServiceRealPrice(roundRealPrice);
		if(StringUtils.isBlank(service.getPriceDetail())){
			service.setPriceDetail("-1");//设置-1，代表是后台将该数据清除了，否则前台项目每次保存都会清除他，因为前台不传该数据 
		}
		final long ret = pmsServiceFacade.update(service);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update service ...",sessionInfo);
		return ret;
	}
	
}
