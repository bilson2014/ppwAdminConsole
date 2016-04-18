package com.panfeng.resource.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.Indent;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.IndentView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.IndentService;

/**
 * 订单相关
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class IndentController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger("error");
	
	@Autowired
	private final IndentService service = null;
	
	@RequestMapping("/indent-list")
	public ModelAndView view(final ModelMap model){
		
		return new ModelAndView("indent-list",model);
	}
	
	@RequestMapping(value = "/indent/list",method = RequestMethod.POST,
					produces = "application/json; charset=UTF-8")
	public DataGrid<Indent> list(final IndentView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		final List<Indent> list = service.listWithPagination(view);
		final long total = service.maxSize(view);
		final DataGrid<Indent> dataGrid = new DataGrid<Indent>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping(value = "/indent/save", method = RequestMethod.POST)
	public long save(final Indent indent){
		
		final long ret = service.save(indent);
		return ret;
	}
	
	@RequestMapping(value = "/indent/update", method = RequestMethod.POST)
	public long update(final Indent indent){
		
		final long ret = service.update(indent);
		return ret;
	}
	
	@RequestMapping(value = "/indent/delete", method = RequestMethod.POST)
	public long delete(final long[] ids){
		
		if(ids.length > 0){
			service.delete(ids);
		} else {
			logger.error("Indent Delete Error ...");
			throw new RuntimeException("Indent Delete Error ...");
		}
		
		return 0l;
	}
	
	// 下单
	@RequestMapping(value = "/indent/order",method = RequestMethod.POST,
			produces = "application/json; charset=UTF-8")
	public Long order(@RequestBody final Indent indent) {
		
		try {
			indent.setIndentName(URLDecoder.decode(indent.getIndentName(), "UTF-8"));
			if(indent.getIndent_recomment() != null && !"".equals(indent.getIndent_recomment())){
				indent.setIndent_recomment(URLDecoder.decode(indent.getIndent_recomment(), "UTF-8"));
			}
			final long ret = service.order(indent);
			return ret;
		} catch (UnsupportedEncodingException e) {
			logger.error("Client Order Decoder Failure ...");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 检测 新订单个数
	 * @return 返回 新订单 个数
	 */
	@RequestMapping("/indent/checkStatus/new")
	public long checkStatus(final HttpServletRequest request){
		
		final long count = service.checkStatus(0);
		return count;
	}
}