package com.panfeng.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.paipianwang.pat.common.util.ValidateUtil;
import com.panfeng.dao.DataCacheDao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 工具数据缓存操作 reids db 1
 * @author rui
 *
 */
@Repository(value = "dataCacheDao")
public class DataCacheDaoImpl implements DataCacheDao {
	@Autowired
	private final JedisPool pool = null;

	/**
	 * 保存缓存数据
	 */
	@Override
	public void setCacheData(String key, String type, String data) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(1);
			if(ValidateUtil.isValid(data)){
				/*Transaction t = jedis.multi();
				t.hset(key, type, data);
//				t.lset(key, Long.parseLong(type), data);
				t.exec();*/
				jedis.hset(key, type, data);
			}
		} catch (Exception e) {
			// do something for logger
			throw new RuntimeException(e);
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}

	}
	/**
	 * 获取缓存数据
	 */
	@Override
	public String getCacheData(String key, String type){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(1);
			String str = jedis.hget(key, type);
			return str;
		} catch (Exception e) {
			// do something for logger
			throw new RuntimeException(e);
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
	}
	
	/**
	 * 设置过期时间
	 */
	@Override
	public void setExpire(String key,int expireSeconds) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(1);
			jedis.expire(key, expireSeconds);
			
		} catch (Exception e) {
			// do something for logger
			throw new RuntimeException(e);
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
	}

}
