package com.panfeng.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;

public class EmployeeTest extends BaseTest {

	@Autowired
	private final PmsEmployeeFacade pmsEmployeeFacade = null;
	
	@Test
	public void save(){
		/*final PmsEmployee e = new PmsEmployee();
		e.setEmployeeLoginName("Jack");
		e.setEmployeePassword("12345678");
		e.setEmployeeRealName("Jackyang");
		e.setEmployeeDescription("The First Infomartion");
		mapper.save(e);
		System.err.println(e.getEmployeeId());
		
		final EmployeeRoleLink link = new EmployeeRoleLink();
		link.setEmployee(e);
		
		final PmsRole role = new PmsRole();
		role.setRoleId(1);
		link.setRole(role);
		mapper.saveRelativity(link);*/
		
		for (int i = 0; i < 20; i++) {
			final PmsEmployee e = new PmsEmployee();
			e.setEmployeeLoginName("用戶" + i);
			e.setEmployeePassword("12345678");
			e.setEmployeeRealName(i + "");
			pmsEmployeeFacade.save(e);
		}
	}
	
	@Test
	public void findEmployeeWithRole(){
		final PmsEmployee employer = pmsEmployeeFacade.findEmployeeById(3l);
		System.err.println(employer);
	}
	
	@Test
	public void findAll(){
		final List<PmsEmployee> list= pmsEmployeeFacade.findEmployeeList();
		System.err.println(list);
	}
	
	@Test
	public void update(){
		PmsEmployee e = new PmsEmployee();
		e.setEmployeeId(3);
		e.setEmployeeLoginName("Rose");
		final long ret = pmsEmployeeFacade.update(e);
		System.err.println(ret);
	}
}
