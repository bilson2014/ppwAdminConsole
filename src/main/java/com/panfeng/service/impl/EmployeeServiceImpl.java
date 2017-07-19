package com.panfeng.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.Team;
import com.panfeng.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private final PmsEmployeeFacade pmsEmployeeFacade = null;

	@Override
	public BaseMsg bind(PmsEmployee employee) {
		BaseMsg baseMsg = null;
		List<PmsEmployee> employees = pmsEmployeeFacade.verificationEmployeeExist(employee);
		if (ValidateUtil.isValid(employees)) {
			// 不存在情况 --->只存在重复绑定
			for (PmsEmployee empl : employees) {
				if (employee.getThirdLoginType().equals(PmsEmployee.LTYPE_QQ)) {
					if (empl.getQqUnique().equals(employee.getQqUnique())) {
						return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
					}
				} else if (employee.getThirdLoginType().equals(PmsEmployee.LTYPE_WECHAT)) {
					if (empl.getWechatUnique().equals(employee.getWechatUnique())) {
						return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
					}
				} else if (employee.getThirdLoginType().equals(PmsEmployee.LTYPE_WEIBO)) {
					if (empl.getWbUnique().equals(employee.getWbUnique())) {
						return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
					}
				}
			}
		} else {
			// 新建
			String phoneNumber = employee.getPhoneNumber();
			List<PmsEmployee> empls = pmsEmployeeFacade.findEmployeesByPhoneNumber(phoneNumber);
			if (empls != null && empls.size() > 0) {
				if (empls.size() == 1) {
					PmsEmployee empl = empls.get(0);
					switch (employee.getThirdLoginType()) {
					case Team.LTYPE_QQ:
						if (!ValidateUtil.isValid(empl.getQqUnique())) {
							// 绑定
							empl.setQqUnique(employee.getQqUnique());
							pmsEmployeeFacade.updateUniqueId(empl);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else if (empl.getQqUnique().equals(employee.getQqUnique())) {
							// 重复绑定
							return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else {
							// 手机号已经绑定过非当前QQ了
							baseMsg = new BaseMsg(BaseMsg.ERROR, "该手机号已经被注册", null);
						}
						break;
					case Team.LTYPE_WECHAT:
						if (!ValidateUtil.isValid(empl.getWechatUnique())) {
							// 绑定
							empl.setWechatUnique(employee.getWechatUnique());
							pmsEmployeeFacade.updateUniqueId(empl);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else if (empl.getWechatUnique().equals(employee.getWechatUnique())) {
							// 重复绑定
							return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else {
							// 手机号已经绑定过非当前QQ了
							baseMsg = new BaseMsg(BaseMsg.ERROR, "该手机号已经被注册", null);
						}
						break;
					case Team.LTYPE_WEIBO:
						if (!ValidateUtil.isValid(empl.getWbUnique())) {
							// 绑定
							empl.setWbUnique(employee.getWbUnique());
							pmsEmployeeFacade.updateUniqueId(empl);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else if (empl.getWbUnique().equals(employee.getWbUnique())) {
							// 重复绑定
							return new BaseMsg(BaseMsg.NORMAL, "绑定成功", empl);
						} else {
							// 手机号已经绑定过非当前QQ了
							baseMsg = new BaseMsg(BaseMsg.ERROR, "该手机号已经被注册", null);
						}
						break;
					}

				} else {
					baseMsg = new BaseMsg(BaseMsg.ERROR, "手机号码重复注册，请联系人事。", null);
				}
			} else {
				// 不存在 ，必须先注册
				baseMsg = new BaseMsg(BaseMsg.ERROR, "手机号必须先注册，请联系人事注册手机号。", null);
			}
		}
		return baseMsg;
	}

	@Override
	public Map<Long, PmsEmployee> getEmployeeMap() {
		final List<PmsEmployee> list = pmsEmployeeFacade.findEmployeeList();
		final Map<Long, PmsEmployee> map = new HashMap<Long, PmsEmployee>();
		if (ValidateUtil.isValid(list)) {
			for (final PmsEmployee employee : list) {
				map.put(employee.getEmployeeId(), employee);
			}
		}
		return map;
	}
	
	@Override
	public Map<Long, PmsEmployee> getAllEmployeeMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("flag", 1);
		List<PmsEmployee> employeeList = pmsEmployeeFacade.findEmployeeByCondition(paramMap);
		final Map<Long, PmsEmployee> map = new HashMap<Long, PmsEmployee>();
		if (ValidateUtil.isValid(employeeList)) {
			for (final PmsEmployee employee : employeeList) {
				map.put(employee.getEmployeeId(), employee);
			}
		}
		return map;
	}

}
