package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.Role;
import com.panfeng.resource.model.Tree;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.RoleView;
import com.panfeng.service.RoleService;

/**
 * 角色相关
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class RoleController extends BaseController {

	@Autowired
	private final RoleService service = null;
	
	@RequestMapping("/role-list")
	public ModelAndView view(){
		
		return new ModelAndView("role-list");
	}
	
	@RequestMapping(value = "/role/tree")
	public List<Tree> tree(){
		
		final List<Tree> trees = service.tree();
		return trees;
	}
	
	@RequestMapping(value = "/role/tree2")
	public List<Tree> tree_2(){
		
		final List<Tree> trees = service.tree_2();
		return trees;
	}
	
	@RequestMapping(value = "/role/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public DataGrid<Role> list(RoleView view,PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		DataGrid<Role> dataGrid = new DataGrid<Role>();
		final List<Role> list = service.listWithPagination(view);
		
		dataGrid.setRows(list);
		final long total = service.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping("/role/update")
	public long update(final Role role){
		
		final long ret = service.update(role);
		return ret;
	}
	
	@RequestMapping("/role/add")
	public long save(final Role role){
		
		final long ret = service.save(role);
		return ret;
	}
	
	@RequestMapping("/role/delete")
	public long delete(final long[] ids){
		
		final long ret = service.delete(ids);
		return ret;
	}
	
	@RequestMapping("/role/grant")
	public long grant(final Long roleId,final long[] resourceIds){
		
		if(roleId != null){
			
			final long ret = service.grant(roleId,resourceIds);
			return ret;
		}
		
		return 0l;
	}
	
	@RequestMapping("/role/getRights/{roleId}")
	public List<Long> getRightsByRole(@PathVariable("roleId") final Long roleId){
		
		List<Long> rights = new ArrayList<Long>();
		if(roleId != null){
			
			rights = service.getRightsByRole(roleId);
		}
		return rights;
	}
}
