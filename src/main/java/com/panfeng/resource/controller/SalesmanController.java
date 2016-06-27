package com.panfeng.resource.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.resource.model.Salesman;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.SalesmanView;
import com.panfeng.service.IndentService;
import com.panfeng.service.SalesmanService;
import com.panfeng.util.DataUtil;
import com.panfeng.util.HttpUtil;
import com.panfeng.util.JsonUtil;
import com.panfeng.util.ValidateUtil;

@RequestMapping("/portal")
@RestController
public class SalesmanController extends BaseController {

	@Autowired
	final private SalesmanService service = null;
	
	@Autowired
	final private IndentService indentService = null;
	
	@RequestMapping("/salesman-list")
	public ModelAndView view(){
		
		return new ModelAndView("salesman-list");
	}
	
	@RequestMapping(value = "/salesman/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public DataGrid<Salesman> list(final SalesmanView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		DataGrid<Salesman> dataGrid = new DataGrid<Salesman>();
		
		final List<Salesman> list = service.listWithPagination(view);
		
		// 装载订单总数及总金额
		for (final Salesman salesman : list) {
			final String salesmanUniqueId = salesman.getUniqueId(); 
			final long total = indentService.countBySalesmanUniqueId(salesmanUniqueId);
			final Double price = indentService.sumPriceBySalesmanUniqueId(salesmanUniqueId);
			if(price != null){
				salesman.setSumPrice(price);
			}else{
				salesman.setSumPrice(0);
			}
			salesman.setTotal(total);
		}
		
		dataGrid.setRows(list);
		final long total = service.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping("/salesman/update")
	public long update(final Salesman salesman){
		
		final long ret = service.update(salesman);
		return ret;
	}
	
	@RequestMapping("/salesman/save")
	public long save(final Salesman salesman){
		
		final String uniqueId = DataUtil.getUuid();
		salesman.setUniqueId(uniqueId);
		final long ret = service.save(salesman);
		return ret;
	}
	
	@RequestMapping("/salesman/delete")
	public long delete(final long[] ids){
		
		final long ret = service.delete(ids);
		return ret;
	}
	
	@RequestMapping("/salesman/all")
	public List<Salesman> all(){
		
		final List<Salesman> list = service.list();
		return list;
	}
	
	@RequestMapping("/salesman/download/code")
	public void download(final HttpServletRequest request,final HttpServletResponse response){
		
		final String json = request.getParameter("json");
		Salesman sale = new Salesman();
		if(ValidateUtil.isValid(json)){
			
			sale = JsonUtil.toBean(json, Salesman.class);
		}
		
		final StringBuffer url = new StringBuffer();
		url.append("http://qr.liantu.com/api.php?text=");
		url.append(GlobalConstant.FILE_PROFIX);
		url.append("/phone/salesman/order/");
		url.append(sale.getUniqueId());
		
		File image=(File) HttpUtil.httpGetFile(url.toString(), null)[1];
		try{
			FileInputStream fileInputStream=new FileInputStream(image);
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("image/png; charset=UTF-8");
			String filename=URLEncoder.encode(sale.getSalesmanName() + "下单二维码.png", "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+  filename+ "\"\r\n");
			OutputStream outputStream = response.getOutputStream();
			HttpUtil.saveTo(fileInputStream, outputStream);
			if(fileInputStream!=null){
				fileInputStream.close();
			}
			if(outputStream!=null){
				outputStream.flush();
				outputStream.close();
			}
			image.delete();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/salesman/download/order/code")
	public void orderDownload(final HttpServletRequest request,final HttpServletResponse response){
		
		final String json = request.getParameter("json");
		Salesman sale = new Salesman();
		if(ValidateUtil.isValid(json)){
			
			sale = JsonUtil.toBean(json, Salesman.class);
		}
		
		final StringBuffer url = new StringBuffer();
		url.append("http://qr.liantu.com/api.php?text=");
		url.append(GlobalConstant.FILE_PROFIX);
		url.append("/phone/salesman/");
		url.append(sale.getUniqueId());
		
		File image=(File) HttpUtil.httpGetFile(url.toString(), null)[1];
		try{
			FileInputStream fileInputStream=new FileInputStream(image);
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("image/png; charset=UTF-8");
			String filename=URLEncoder.encode(sale.getSalesmanName() + "产品二维码.png", "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+  filename+ "\"\r\n");
			OutputStream outputStream = response.getOutputStream();
			HttpUtil.saveTo(fileInputStream, outputStream);
			if(fileInputStream!=null){
				fileInputStream.close();
			}
			if(outputStream!=null){
				outputStream.flush();
				outputStream.close();
			}
			image.delete();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 验证分销人UniqueId是否为合法
	 * @return true or false
	 */
	@RequestMapping(value = "/salesman/valid",method = RequestMethod.POST)
	public Boolean isSalesmanValid(@RequestBody final Salesman man){
		
		if(man != null){
			final String uniqueId = man.getUniqueId();
			if(ValidateUtil.isValid(uniqueId)){
				final Salesman salesman = service.findSalesmanByUniqueId(uniqueId);
				if(salesman != null)
					return true;
			}
		}
		
		return false;
	}
}
