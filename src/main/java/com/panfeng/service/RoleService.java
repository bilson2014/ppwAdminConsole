package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Role;
import com.panfeng.resource.model.Tree;
import com.panfeng.resource.view.RoleView;

public interface RoleService {

	public List<Role> all();
	
	public List<Role> listWithPagination(final RoleView view);
	
	public Role findRoleById(final long roleId);
	
	public long save(final Role role);
	
	public long update(final Role role);
	
	public long delete(final long[] ids);
	
	public long maxSize(final RoleView view);
	
	public List<Tree> tree();
	
	public List<Tree> tree_2();

	public long grant(final Long roleId, final long[] resourceIds);

	public List<Long> getRightsByRole(final long roleId);
}
