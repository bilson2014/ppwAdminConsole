package com.panfeng.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.persist.UserMapper;
import com.panfeng.resource.model.User;
import com.panfeng.util.DataUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class UserTest {

	@Autowired
	private UserMapper mapper = null;
	
	@Test
	public void testSaveUser(){
		final User user = new User();
		user.setPassword(DataUtil.md5("123456"));
		user.setUserName("test3");
		user.setTelephone("13001264062");
		final long ret = mapper.save(user);
		System.err.println(ret);
		System.err.println(user.getId());
	}
}
