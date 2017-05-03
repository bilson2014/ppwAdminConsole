package com.panfeng.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.service.PmsRoleFacade;
import com.panfeng.resource.model.EmployeeRoleLink;

public class RoleTest extends BaseTest {

	@Autowired
	private PmsRoleFacade pmsRoleFacade = null;
	
	@Test
	public void save(){
		final PmsRole r = new PmsRole();
		r.setRoleName("用户");
		pmsRoleFacade.save(r);
		System.err.println(r.getRoleId());
	}
	
	@Test
	public void update(){
		final PmsRole r = new PmsRole();
		r.setRoleId(2);
		r.setRoleName("客户");
		pmsRoleFacade.update(r);
	}
	
	@Test
	public void saveRelation(){
		final EmployeeRoleLink link = new EmployeeRoleLink();
		final PmsEmployee e =  new PmsEmployee();
		e.setEmployeeId(2);
		link.setEmployee(e);
		
		final PmsRole role = new PmsRole();
		role.setRoleId(2);
		link.setRole(role);
		// mapper.saveRelativity(link);
	}
	
	@Test
	public void findEmployerWithRole(){
		final PmsRole r = pmsRoleFacade.findRoleById(1l);
		System.err.println(r);
	}
	
	@Test
	public void findAll(){
		final List<PmsRole> list= pmsRoleFacade.findAllRoles();
		System.err.println(list.size());
	}
	
	@Test
	public void delete(){
		final long ret = pmsRoleFacade.deleteByIds(new long[]{2l});
		System.err.println(ret);
	}
}
