package com.panfeng.resource.model;

import com.panfeng.domain.BaseObject;

public class RoleRightLink extends BaseObject {

	private static final long serialVersionUID = -8896384111671453432L;

	private Role role = null;
	
	private Right right = null;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Right getRight() {
		return right;
	}

	public void setRight(Right right) {
		this.right = right;
	}
	
}
