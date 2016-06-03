package com.panfeng.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.resource.model.Team;
import com.panfeng.service.TeamService;
import com.panfeng.util.DataUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TeamTest {
	
	@Autowired
	private TeamService service = null;
	
	@Test
	public void testSave(){
		Team team = new Team();
		team.setTeamDescription("fifth");
		team.setTeamName("fifth");
		System.err.println(service.save(team));
		System.err.println(team.getTeamId());
	}
	
	@Test
	public void savePassword(){
		List<Team> list = service.getAll();
		if(list != null && list.size() > 0){
			for (final Team team : list) {
				final String loginName = team.getLoginName();
				if(loginName != null && !"".equals(loginName)){
					final String telephone = team.getPhoneNumber();
					if(telephone != null && !"".equals(telephone)){
						if(telephone.length() == 11){
							final String password = telephone.substring(5);
							team.setPassword(DataUtil.md5(password));
							service.recover(team);
						}
					}
					continue;
				}
			}
		}
	}
	
	@Test
	public void test4(){
		final String password = "hongjuwenhua";
		System.out.println(DataUtil.md5(password));
	}
	
}
