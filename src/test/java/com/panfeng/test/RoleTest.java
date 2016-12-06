package com.panfeng.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.panfeng.persist.RoleMapper;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.model.EmployeeRoleLink;
import com.panfeng.resource.model.Role;
import com.panfeng.resource.view.RoleView;
import com.panfeng.service.RoleService;

public class RoleTest extends BaseTest {

	@Autowired
	private RoleMapper mapper = null;
	
	@Autowired
	private RoleService service = null;
	
	@Test
	public void save(){
		final Role r = new Role();
		r.setRoleName("用户");
		mapper.save(r);
		System.err.println(r.getRoleId());
	}
	
	@Test
	public void update(){
		final Role r = new Role();
		r.setRoleId(2);
		r.setRoleName("客户");
		mapper.update(r);
	}
	
	@Test
	public void list(){
		final RoleView view = new RoleView();
		final List<Role> roles = mapper.listWithPagination(view);
		final long size = mapper.maxSize(view);
		System.err.println(size);
		System.out.println(roles.size());
	}
	
	@Test
	public void saveRelation(){
		final EmployeeRoleLink link = new EmployeeRoleLink();
		final Employee e =  new Employee();
		e.setEmployeeId(2);
		link.setEmployee(e);
		
		final Role role = new Role();
		role.setRoleId(2);
		link.setRole(role);
		// mapper.saveRelativity(link);
	}
	
	@Test
	public void findEmployerWithRole(){
		final Role r = mapper.findRoleById(1l);
		System.err.println(r);
	}
	
	@Test
	public void findAll(){
		final List<Role> list= service.all();
		System.err.println(list.size());
		
	}
	
	@Test
	public void delete(){
		final long ret = service.delete(new long[]{2l});
		System.err.println(ret);
	}
}
