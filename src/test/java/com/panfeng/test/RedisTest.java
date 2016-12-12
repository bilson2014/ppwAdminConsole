package com.panfeng.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.panfeng.resource.model.Right;
import com.panfeng.util.ValidateUtil;

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
		
		
		
		/*Map<String,Right> map = mapper.getRights();
		Map<String,String> stringMap = new HashMap<String, String>();
		
		for (Map.Entry<String, Right> entry : map.entrySet()) {
			Right right = entry.getValue();
			Gson gson = new Gson();
			String str = gson.toJson(right);
			stringMap.put(entry.getKey(), str);
		}
		
		jedis.hmset("right_map", stringMap);
		*/
		Map<String,Right> maps = new HashMap<String, Right>();
		Map<String,String> mapss = jedis.hgetAll("right_map");
		for (Map.Entry<String, String> entry : mapss.entrySet()) {
			final String value = entry.getValue();
			if(ValidateUtil.isValid(value)){
				Gson gson = new Gson();
				final Right right = gson.fromJson(value, Right.class);
				maps.put(entry.getKey(), right);
			}
		}
		
		System.err.println(maps.size() + " ------ " + maps.get("/portal/team-list").getCode());
		
		jedis.close();
		
	}

}
