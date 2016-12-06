package com.panfeng.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.persist.UserMapper;
import com.panfeng.resource.model.User;
import com.panfeng.service.UserService;
import com.panfeng.util.DataUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UsersTest {

	@Autowired
	private final UserMapper mapper = null;
	
	@Autowired
	private final UserService service = null;
	
	@Test
	public void testUser(){
		for (User user : mapper.all()) {
			System.err.println(user);
		}
	}
	
	@Test
	public void testUserService(){
		for (User user : service.all()) {
			System.out.println(user.getUserName());
		}
	}
	
	@Test
	public void test2(){
		final String str = "123456";
		System.err.println(DataUtil.md5(str));
	}
	
}
