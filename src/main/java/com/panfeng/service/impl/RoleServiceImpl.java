package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.RoleMapper;
import com.panfeng.resource.model.Role;
import com.panfeng.resource.model.Tree;
import com.panfeng.resource.view.RoleView;
import com.panfeng.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private final RoleMapper mapper= null;
	
	public List<Role> all() {
		
		final List<Role> list = mapper.all();
		return list;
	}

	public List<Role> listWithPagination(final RoleView view) {
		
		final List<Role> list = mapper.listWithPagination(view);
		return list;
	}

	public Role findRoleById(final long roleId) {
		
		final Role role = mapper.findRoleById(roleId);
		return role;
	}

	public long save(final Role role) {
		
		final long ret = mapper.save(role);
		return ret;
	}

	public long update(final Role role) {
		
		final long ret = mapper.update(role);
		return ret;
	}

	@Transactional
	public long delete(final long[] ids) {
		
		if(ids != null && ids.length > 0){
			for (final long id : ids) {
				mapper.deleteRoleRightLink(id);
				mapper.deleteEmployeeRoleLink(id);
				mapper.delete(id);
			}
			return 1l;
		}
		return 0l;
	}

	public long maxSize(final RoleView view) {
		
		final long ret = mapper.maxSize(view);
		return ret;
	}

	public List<Tree> tree() {
		
		List<Role> list = mapper.all();
		List<Tree> tree = new ArrayList<Tree>();
		
		for (final Role role : list) {
			Tree t = new Tree();
			t.setId(role.getRoleId() + "");
			t.setText(role.getRoleName());
			
			tree.add(t);
		}
		return tree;
	}

	@Transactional
	public long grant(final Long roleId, final long[] resourceIds) {
		if(roleId != null){
			mapper.deleteRoleRightLink(roleId);
			final long ret = mapper.grant(roleId,resourceIds);
			return ret;
		}
		return 0l;
	}

	public List<Long> getRightsByRole(final long roleId) {
		 
		final List<Long> rights = mapper.getRightsByRole(roleId);
		return rights;
	}

}
