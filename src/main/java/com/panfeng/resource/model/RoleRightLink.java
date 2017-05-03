package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;
import com.paipianwang.pat.facade.right.entity.PmsRight;
import com.paipianwang.pat.facade.right.entity.PmsRole;

public class RoleRightLink extends BaseEntity {

	private static final long serialVersionUID = -8896384111671453432L;

	private PmsRole role = null;
	
	private PmsRight right = null;

	public PmsRole getRole() {
		return role;
	}

	public void setRole(PmsRole role) {
		this.role = role;
	}

	public PmsRight getRight() {
		return right;
	}

	public void setRight(PmsRight right) {
		this.right = right;
	}
	
}
