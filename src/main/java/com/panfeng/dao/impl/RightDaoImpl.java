package com.panfeng.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsRight;
import com.panfeng.dao.RightDao;
import com.panfeng.util.RedisUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Repository(value = "rightDao")
public class RightDaoImpl implements RightDao {

	@Autowired
	private final JedisPool pool = null;
	
	public PmsRight getRightFromRedis(final String uri) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String str = jedis.hget(PmsConstant.CONTEXT_RIGHT_MAP, uri);
			final PmsRight right = RedisUtils.fromJson(str,PmsRight.class);
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

	public Map<String, PmsRight> getRightsFromRedis() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Map<String,String> map = jedis.hgetAll(PmsConstant.CONTEXT_RIGHT_MAP);
			if(ValidateUtil.isValid(map)){
				final Map<String,PmsRight> rightMap = RedisUtils.fromJson(map);
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

	public void addRightByRedis(final PmsRight right) {
		
		if(right != null){
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				final String str = RedisUtils.toJson(right);
				if(ValidateUtil.isValid(str)){
					Transaction t = jedis.multi();
					t.hset(PmsConstant.CONTEXT_RIGHT_MAP, right.getUrl(), str);
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

	public void resetRightFromRedis(final Map<String, PmsRight> map) {
		
		if(ValidateUtil.isValid(map)){
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				Transaction tx = jedis.multi();
				final Map<String,String> rightMap = RedisUtils.toJson(map);
				tx.hmset(PmsConstant.CONTEXT_RIGHT_MAP, rightMap);
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
