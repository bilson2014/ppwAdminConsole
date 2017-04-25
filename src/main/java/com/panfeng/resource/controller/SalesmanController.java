package com.panfeng.resource.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.indent.service.PmsIndentFacade;
import com.paipianwang.pat.facade.sales.entity.PmsSalesman;
import com.paipianwang.pat.facade.sales.service.PmsSalesmanFacade;
import com.panfeng.resource.model.Salesman;
import com.panfeng.resource.view.SalesmanView;
import com.panfeng.util.DataUtil;
import com.panfeng.util.HttpUtil;
import com.panfeng.util.Log;

@RequestMapping("/portal")
@RestController
public class SalesmanController extends BaseController {
	
	
	@Autowired
	private PmsIndentFacade pmsIndentFacade = null;
	@Autowired
	private PmsSalesmanFacade pmsSalesmanFacade = null;
	@RequestMapping("/salesman-list")
	public ModelAndView view(){
		return new ModelAndView("salesman-list");
	}
	
	@RequestMapping(value = "/salesman/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public DataGrid<PmsSalesman> list(final SalesmanView view,final PageParam pageParam){
		
		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("uniqueId", view.getUniqueId());
		paramMap.put("salesmanName", view.getSalesmanName());
		final DataGrid<PmsSalesman> dataGrid = pmsSalesmanFacade.listWithPagination(paramMap,pageParam);
		List<PmsSalesman> list = dataGrid.getRows();
 		// 装载订单总数及总金额
		for (final PmsSalesman salesman : list) {
			final String salesmanUniqueId = salesman.getUniqueId();
			final long total = pmsIndentFacade.countBySalesmanUniqueId(salesmanUniqueId);
			final Double price = pmsIndentFacade.sumPriceBySalesmanUniqueId(salesmanUniqueId);
			if(price != null){
				salesman.setSumPrice(price);
			}else{
				salesman.setSumPrice(0);
			}
			salesman.setTotal(total);
		}
		dataGrid.setRows(list);
		return dataGrid;
	}
	
	@RequestMapping("/salesman/update")
	public long update(final PmsSalesman salesman,HttpServletRequest request){
		String uniqueId = salesman.getUniqueId(); // 唯一标识，即渠道
		if(StringUtils.isBlank(uniqueId)) {
			uniqueId = DataUtil.getUuid();
		}
		salesman.setUniqueId(uniqueId);
		final long ret = pmsSalesmanFacade.update(salesman);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update Salesman ...",sessionInfo);
		return ret;
	}
	
	@RequestMapping("/salesman/save")
	public long save(final PmsSalesman salesman,HttpServletRequest request){
		String uniqueId = salesman.getUniqueId(); // 唯一标识，即渠道
		if(StringUtils.isBlank(uniqueId)) {
			uniqueId = DataUtil.getUuid();
		}
		salesman.setUniqueId(uniqueId);
		final long ret = pmsSalesmanFacade.save(salesman);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save Salesman ...",sessionInfo);
		return ret;
	}
	
	@RequestMapping("/salesman/delete")
	public long delete(final long[] ids,HttpServletRequest request){
		
		final long ret = pmsSalesmanFacade.delete(ids);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete Salesmans ... ids:"+ids.toString(),sessionInfo);
		return ret;
	}
	
	@RequestMapping("/salesman/all")
	public List<PmsSalesman> all(){
		final List<PmsSalesman> list = pmsSalesmanFacade.list();
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
		
		url.append("http://www.apaipian.com/phone/salesman/order/");
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
		
		url.append("http://www.apaipian.com");
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
	
}
