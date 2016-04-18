package com.panfeng.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.panfeng.dao.RightDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.resource.model.Right;
import com.panfeng.util.RedisUtils;
import com.panfeng.util.ValidateUtil;

@Repository(value = "rightDao")
public class RightDaoImpl implements RightDao {

	@Autowired
	private final JedisPool pool = null;
	
	public Right getRightFromRedis(final String uri) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String str = jedis.hget(GlobalConstant.CONTEXT_RIGHT_MAP, uri);
			final Right right = RedisUtils.fromJson(str,Right.class);
			return right;
		} catch (Exception e) {
			// do something for logger
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
		
		return null;
	}

	public Map<String, Right> getRightsFromRedis() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Map<String,String> map = jedis.hgetAll(GlobalConstant.CONTEXT_RIGHT_MAP);
			if(ValidateUtil.isValid(map)){
				final Map<String,Right> rightMap = RedisUtils.fromJson(map);
				return rightMap;
			}
			
			return null;
		} catch (Exception e) {
			// do something for logger
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
		
		return null;
	}

	public void addRightByRedis(final Right right) {
		
		if(right != null){
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				final String str = RedisUtils.toJson(right);
				if(ValidateUtil.isValid(str)){
					Transaction t = jedis.multi();
					t.hset(GlobalConstant.CONTEXT_RIGHT_MAP, right.getUrl(), str);
					t.exec();
				}
			} catch (Exception e) {
				// do something for logger
			} finally {
				if(jedis != null){
					jedis.disconnect();
					jedis.close();
				}
			}
		}
	}

	public void resetRightFromRedis(final Map<String, Right> map) {
		
		if(ValidateUtil.isValid(map)){
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				Transaction tx = jedis.multi();
				final Map<String,String> rightMap = RedisUtils.toJson(map);
				tx.hmset(GlobalConstant.CONTEXT_RIGHT_MAP, rightMap);
				tx.exec();
			} catch (Exception e) {
				// do something for logger
			} finally {
				if(jedis != null){
					jedis.disconnect();
					jedis.close();
				}
			}
			
		}
	}

}
