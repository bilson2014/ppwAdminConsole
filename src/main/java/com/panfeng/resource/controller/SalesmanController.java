package com.panfeng.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.Salesman;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.SalesmanView;
import com.panfeng.service.SalesmanService;
import com.panfeng.util.DataUtil;

@RequestMapping("/portal")
@RestController
public class SalesmanController extends BaseController {

	@Autowired
	final private SalesmanService service = null;
	
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
}
