package com.panfeng.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.panfeng.persist.EmployeeMapper;
import com.panfeng.resource.model.Employee;

public class EmployeeTest extends BaseTest {

	@Autowired
	private EmployeeMapper mapper = null;
	
	@Test
	public void save(){
		/*final Employee e = new Employee();
		e.setEmployeeLoginName("Jack");
		e.setEmployeePassword("12345678");
		e.setEmployeeRealName("Jackyang");
		e.setEmployeeDescription("The First Infomartion");
		mapper.save(e);
		System.err.println(e.getEmployeeId());
		
		final EmployeeRoleLink link = new EmployeeRoleLink();
		link.setEmployee(e);
		
		final Role role = new Role();
		role.setRoleId(1);
		link.setRole(role);
		mapper.saveRelativity(link);*/
		
		for (int i = 0; i < 20; i++) {
			final Employee e = new Employee();
			e.setEmployeeLoginName("用戶" + i);
			e.setEmployeePassword("12345678");
			e.setEmployeeRealName(i + "");
			mapper.save(e);
		}
	}
	
	@Test
	public void saveRelation(){
		final Employee e =  new Employee();
		e.setEmployeeId(20);
		final List<Long> roleIds = new ArrayList<Long>();
		roleIds.add(1l);
		roleIds.add(3l);
		e.setRoleIds(roleIds);
		
		if(e.getRoleIds() != null && e.getRoleIds().size() > 0){
			
			mapper.saveRelativity(e);
		}
	}
	
	@Test
	public void findEmployeeWithRole(){
		final Employee employer = mapper.findEmployeeById(3l);
		System.err.println(employer);
	}
	
	@Test
	public void findAll(){
		final List<Employee> list= mapper.all();
		System.err.println(list);
	}
	
	@Test
	public void update(){
		Employee e = new Employee();
		e.setEmployeeId(3);
		e.setEmployeeLoginName("Rose");
		final long ret = mapper.update(e);
		System.err.println(ret);
	}
}
