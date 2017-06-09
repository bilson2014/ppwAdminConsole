package com.panfeng.biz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.entity.PmsTree;
import com.paipianwang.pat.facade.right.service.PmsRoleFacade;

@Service("pmsRoleControllerBiz")
public class PmsRoleControllerBiz {

	@Autowired
	private final PmsRoleFacade pmsRoleFacade = null;

	/**
	 * 加载除了超级管理员、客户、供应商之外的所有角色树
	 * @return 角色树集合
	 */
	public List<PmsTree> GetRoleTree() {

		List<PmsRole> list = pmsRoleFacade.findAllRoles();
		List<PmsTree> tree = new ArrayList<PmsTree>();

		for (final PmsRole role : list) {
			final String roleName = role.getRoleName();
			if (!"超级管理员".equals(roleName) && !"客户".equals(roleName) && !"供应商".equals(roleName)) {
				PmsTree t = new PmsTree();
				t.setId(role.getRoleId() + "");
				t.setText(role.getRoleName());
				t.setDesc(role.getRoleDescription());
				tree.add(t);
			}
		}
		return tree;
	}

	/**
	 * 加载除了超级管理员之外的所有角色树集合
	 * @return 角色树集合
	 */
	public List<PmsTree> GetRoleTreeWithoutAdmin() {

		List<PmsRole> list = pmsRoleFacade.findAllRoles();
		List<PmsTree> tree = new ArrayList<PmsTree>();

		for (final PmsRole role : list) {
			final String roleName = role.getRoleName();
			if (!"超级管理员".equals(roleName)) {
				PmsTree t = new PmsTree();
				t.setId(role.getRoleId() + "");
				t.setText(role.getRoleName());

				tree.add(t);
			}
		}
		return tree;
	}
}
