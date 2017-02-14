package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Item;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.ItemView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.ItemService;
import com.panfeng.util.Log;

/**
 * 类别相关
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class ItemController extends BaseController {

	//private static final Logger logger = LoggerFactory.getLogger("error");
	
	@Autowired
	private final ItemService service = null;
	
	@RequestMapping("item-list")
	public ModelAndView view(final ModelMap model){
		
		return new ModelAndView("item-list",model);
	}
	
	@RequestMapping(value = "/item/list",method = RequestMethod.POST,
					produces = "application/json; charset=UTF-8")
	public DataGrid<Item> list(final ItemView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		final List<Item> list = service.listWithPagination(view);
		final long total = service.maxSize(view);
		final DataGrid<Item> dataGrid = new DataGrid<Item>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping(value = "/item/itemSelect",method = RequestMethod.POST,
					produces = "application/json; charset=UTF-8")
	public List<Item> all(final HttpServletRequest request){
		
		final List<Item> list = service.list();
		return list;
	}
	
	@RequestMapping(value = "/item/save")
	public long save(final Item item,HttpServletRequest request){
		
		final long ret = service.save(item);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("add item ...", sessionInfo);
		return ret;
	}
	
	@RequestMapping(value = "/item/update",method = RequestMethod.POST)
	public long update(final Item item,HttpServletRequest request){
		
		final long ret = service.update(item);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update item ...", sessionInfo);
		return ret;
	}
	
	@RequestMapping(value = "/item/delete",method = RequestMethod.POST)
	public long delete(final long[] ids,HttpServletRequest request){
		
		if(ids.length > 0){
			service.delete(ids);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("delete items ... ids:"+ids.toString(), sessionInfo);
		}else{
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("Item Delete Error ...",sessionInfo);
			throw new RuntimeException("Item Delete Error ...");
		}
		
		return 0l;
	}
	
	// --------------------------------以下是前端展示内容 ----------------------------
	/**
	 * 装载 类别 项
	 */
	@RequestMapping(value = "/item/static/data")
	public List<Item> itemData(){
		
		final List<Item> list = service.listWithoutActive();
		return list;
	}
	
	@RequestMapping(value = "/item/static/get/tags")
	public List<Item> getTags(@RequestBody List<Long> ids){
		return service.getTagsById(ids);
	}
	
}
