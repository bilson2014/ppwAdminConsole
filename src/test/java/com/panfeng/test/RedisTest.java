package com.panfeng.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsRight;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisTest extends BaseTest{

	@Autowired
	private final JedisPool pool = null;
	
	@Test
	public void test(){
		
		Jedis jedis = new Jedis("123.59.75.62",6379);
		jedis.set("name","Jack");
		String name = jedis.get("name");
		System.err.println(name);
		jedis.close();
	}
	
	@Test
	public void testJedisPool(){
		Jedis jedis = pool.getResource();
		
		Map<String,PmsRight> maps = new HashMap<String, PmsRight>();
		Map<String,String> mapss = jedis.hgetAll("right_map");
		for (Map.Entry<String, String> entry : mapss.entrySet()) {
			final String value = entry.getValue();
			if(ValidateUtil.isValid(value)){
				Gson gson = new Gson();
				final PmsRight right = gson.fromJson(value, PmsRight.class);
				maps.put(entry.getKey(), right);
			}
		}
		
		System.err.println(maps.size() + " ------ " + maps.get("/portal/team-list").getCode());
		
		jedis.close();
		
	}

}
