package com.panfeng.resource.model;

import java.util.List;

import com.paipianwang.pat.common.entity.BaseEntity;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsRight;
import com.paipianwang.pat.facade.right.entity.PmsRole;

public class Customer extends BaseEntity {

	private static final long serialVersionUID = 6521681631221853496L;

	private List<PmsRole> roles = null;
	
	private boolean superAdmin = false;
	
	private long[] rightSum;
	
	public void calculateRightSum(){
		int pos = 0;
		long code = 0;
		if(ValidateUtil.isValid(roles)){
			for (final PmsRole role : roles) {
				if(role != null){
					// 判断是否为超级管理员
					if("-1".equals(role.getRoleValue())){
						this.superAdmin = true;
						roles = null;
						return ;
					}
					
					for (PmsRight right : role.getRights()) {
						pos = right.getPos();
						code = right.getCode();
						rightSum[pos] = rightSum[pos] | code;
					}
				}
			}
		}
		roles = null;
	}

	public List<PmsRole> getRoles() {
		return roles;
	}

	public void setRoles(List<PmsRole> roles) {
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
	public boolean hasRight(final PmsRight right){
		int pos = right.getPos();
		long code = right.getCode();
		long ret = rightSum[pos] & code;
		return !(ret == 0);
	}
}
