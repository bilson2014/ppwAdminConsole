package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.entity.PmsTree;
import com.paipianwang.pat.facade.right.service.PmsRightFacade;
import com.paipianwang.pat.facade.right.service.PmsRoleFacade;
import com.panfeng.biz.PmsRoleControllerBiz;
import com.panfeng.persist.ActivitiGroupMapper;
import com.panfeng.resource.model.ActivitiGroup;
import com.panfeng.resource.view.RoleView;

/**
 * 角色相关
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class RoleController extends BaseController {

	@Autowired
	private final PmsRoleFacade pmsRoleFacade = null;
	
	@Autowired
	private final PmsRoleControllerBiz pmsRoleControllerBiz = null;
	
	@Autowired
	private final PmsRightFacade pmsRightFacade = null;
	
	@Autowired
	private ActivitiGroupMapper activitiGroupMapper=null;
	
	@RequestMapping("/role-list")
	public ModelAndView view(){
		
		return new ModelAndView("role-list");
	}
	
	@RequestMapping(value = "/role/tree")
	public List<PmsTree> tree(){
		
		final List<PmsTree> trees = pmsRoleControllerBiz.GetRoleTree();
		return trees;
	}
	
	@RequestMapping(value = "/role/tree2")
	public List<PmsTree> tree_2(){
		
		final List<PmsTree> trees = pmsRoleControllerBiz.GetRoleTreeWithoutAdmin();
		return trees;
	}
	
	@RequestMapping(value = "/role/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public DataGrid<PmsRole> list(RoleView view, final PageParam pageParam) {

		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);

		DataGrid<PmsRole> dataGrid = pmsRoleFacade.listWithPagination(pageParam, JsonUtil.objectToMap(view));
		List<PmsRole> roles = dataGrid.getRows();

		// 追加项目角色

		Map<String, Object> paramMap = new HashMap<>();
		long count = activitiGroupMapper.count(paramMap);

		// 计算
		long begin = 0;
		long limit = 0;
		long total = page * rows;
		if (total <= dataGrid.getTotal()) {
			// 不需要
		} else if (total > dataGrid.getTotal()) {
			if ((page-1) * rows > dataGrid.getTotal()) {
				// 翻页需要
				begin = (((total - dataGrid.getTotal()) / rows) - 1) * rows + (total - dataGrid.getTotal()) % rows;
				limit = rows;
			} else {
				// 首次
				begin = 0;
				limit = rows-roles.size() ;
			}
		}

		paramMap.put("begin", begin);
		paramMap.put("limit", limit);
		paramMap.put("name",view.getRoleName());
		List<ActivitiGroup> groups = activitiGroupMapper.listWithPagination(paramMap);

		for (ActivitiGroup group : groups) {
			PmsRole role = new PmsRole();
			role.setRoleName("新版项目角色_" + group.getName());
			role.setRoleValue("project_" + group.getId());
			roles.add(role);
		}

		dataGrid.setTotal(dataGrid.getTotal() + count);

		return dataGrid;
	}
	
	
	@RequestMapping("/role/update")
	public long update(final PmsRole role){
		
		final long ret = pmsRoleFacade.update(role);
		return ret;
	}
	
	@RequestMapping("/role/add")
	public long save(final PmsRole role){
		
		final long ret = pmsRoleFacade.save(role);
		return ret;
	}
	
	@RequestMapping("/role/delete")
	public long delete(final long[] ids){
		
		final long ret = pmsRoleFacade.deleteByIds(ids);
		return ret;
	}
	
	@RequestMapping("/role/grant")
	public long grant(final Long roleId,final long[] resourceIds){
		
		if(roleId != null){
			
			final long ret = pmsRoleFacade.grant(roleId,resourceIds);
			return ret;
		}
		
		return 0l;
	}
	
	@RequestMapping("/role/getRights/{roleId}")
	public List<Long> getRightsByRole(@PathVariable("roleId") final Long roleId){
		
		List<Long> rights = new ArrayList<Long>();
		if(roleId != null){
			
			rights = pmsRightFacade.findRightsByRole(roleId);
		}
		return rights;
	}
	
	@RequestMapping("/role/findAll")
	public List<PmsRole> findAllRoles() {
		final List<PmsRole> list = pmsRoleFacade.findAllRoles();
		final List<PmsRole> roleList = new ArrayList<PmsRole>();
		for (PmsRole pmsRole : list) {
			final String roleName = pmsRole.getRoleName();
			final String roleValue = pmsRole.getRoleValue();
			if(!"-1".equals(roleValue) && !"供应商".equals(roleName) && !"客户".equals(roleName)) 
				roleList.add(pmsRole);
		}
		return roleList;
	}
}
