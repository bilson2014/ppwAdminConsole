package com.panfeng.service;

import java.util.Map;

import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.panfeng.domain.BaseMsg;

public interface EmployeeService {

	/**
	 * 三方登陆绑定
	 * 
	 * @param employee
	 * @return
	 */
	public BaseMsg bind(PmsEmployee employee);
	
	/**
	 * 获取员工集合
	 * 	以员工ID作为Key值，员工实体作为 value
	 * @return
	 */
	public Map<Long, PmsEmployee> getEmployeeMap();

}
