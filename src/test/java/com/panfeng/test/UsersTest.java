package com.panfeng.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.mq.service.MailMQService;
import com.panfeng.persist.UserMapper;
import com.panfeng.resource.model.User;
import com.panfeng.util.DataUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UsersTest {

	@Autowired
	private final UserMapper mapper = null;
	
	@Autowired
	private final MailMQService mailMQService = null;
	
	
	
	@Test
	public void testUser(){
		for (User user : mapper.all()) {
			System.err.println(user);
		}
	}
	
	/*@Test
	public void testUserService(){
		for (User user : service.all()) {
			System.out.println(user.getUserName());
		}
	}*/
	
	@Test
	public void test2(){
		final String str = "123456";
		System.err.println(DataUtil.md5(str));
	}
	
	@Test
	public void sendMail(){
		Map<String, String[]> map =  new HashMap<>();
		//map.put("609615907@qq.com", new String[]{"郭阳"});
		//map.put("1061942069@qq.com", new String[]{"王留成"});
		map.put("1300792971@qq.com", new String[]{"卢涛"});
		mailMQService.sendMailsByType("qianduan", map);
		
	}
}
