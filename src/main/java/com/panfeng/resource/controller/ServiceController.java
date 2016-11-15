package com.panfeng.resource.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Product;
import com.panfeng.resource.model.Service;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.ServiceView;
import com.panfeng.service.ProductService;
import com.panfeng.service.ServiceService;
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
	private final ServiceService serService = null;
	
	@Autowired
	private final ProductService proService = null;
	
	@RequestMapping("/service-list")
	public ModelAndView view(final ModelMap model){
		
		return new ModelAndView("service-list",model);
	}
	
	@RequestMapping(value = "/service/list")
	public DataGrid<Service> list(final ServiceView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		final List<Service> list = serService.listWithPagination(view);
		final long total = serService.maxSize(view);
		final DataGrid<Service> dataGrid = new DataGrid<Service>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping(value = "/service/delete",method = RequestMethod.POST,
					produces = "application/json; charset=UTF-8")
	public long delete(final long[] ids,HttpServletRequest request){
		
		final long ret = serService.delete(ids);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete service ... ids:"+ids.toString(),sessionInfo);
		return ret;
	}
	
	/**
	 * 获取全部产品,填充下拉列表
	 */
	@RequestMapping(value = "/service/productSelect",method = RequestMethod.POST,
					produces = "application/json; charset=UTF-8")
	public List<Product> select(){
		
		final List<Product> list = proService.all();
		return list;
	}
	
	@RequestMapping(value = "/service/save",method = RequestMethod.POST)
	public long save(final Service service,HttpServletRequest request){
		
		final double price = service.getServicePrice();
		final double discount = service.getServiceDiscount();
		final double realPrice = price * discount;
		BigDecimal bg = new BigDecimal(realPrice);
		final double roundRealPrice = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		service.setServiceRealPrice(roundRealPrice);
		final long ret = serService.save(service);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save service ...",sessionInfo);
		return ret;
	}
	@RequestMapping(value = "/service/update",method = RequestMethod.POST)
	public long update(final Service service,HttpServletRequest request){
		
		final double price = service.getServicePrice();
		final double discount = service.getServiceDiscount();
		final double realPrice = price * discount;
		BigDecimal bg = new BigDecimal(realPrice);
		final double roundRealPrice = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		service.setServiceRealPrice(roundRealPrice);
		final long ret = serService.update(service);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update service ...",sessionInfo);
		return ret;
	}
	
	
	// --------------------------- 一下是 前端 展示 内容 ------------------
	@RequestMapping("/service/static/loadService/{productId}")
	public List<Service> loadService(@PathVariable("productId") final Integer productId){
		
		final List<Service> list = serService.loadService(productId);
		return list;
	}
}
