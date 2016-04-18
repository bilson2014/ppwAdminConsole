package com.panfeng.resource.model;

import java.util.List;

import com.panfeng.domain.BaseObject;
import com.panfeng.util.ValidateUtil;

public class Customer extends BaseObject {

	private static final long serialVersionUID = 6521681631221853496L;

	private List<Role> roles = null;
	
	private boolean superAdmin = false;
	
	private long[] rightSum;
	
	public void calculateRightSum(){
		int pos = 0;
		long code = 0;
		if(ValidateUtil.isValid(roles)){
			for (final Role role : roles) {
				if(role != null){
					// 判断是否为超级管理员
					if("-1".equals(role.getRoleValue())){
						this.superAdmin = true;
						roles = null;
						return ;
					}
					
					for (Right right : role.getRights()) {
						pos = right.getPos();
						code = right.getCode();
						rightSum[pos] = rightSum[pos] | code;
					}
				}
			}
		}
		roles = null;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public long[] getRightSum() {
		return rightSum;
	}

	public void setRightSum(long[] rightSum) {
		this.rightSum = rightSum;
	}
	
	/**
	 * 判断用户是否有指定的权限
	 */
	public boolean hasRight(final Right right){
		int pos = right.getPos();
		long code = right.getCode();
		long ret = rightSum[pos] & code;
		return !(ret == 0);
	}
}
