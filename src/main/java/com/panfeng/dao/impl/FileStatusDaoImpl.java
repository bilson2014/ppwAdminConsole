package com.panfeng.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.panfeng.dao.FileStatusDao;

@Repository
public class FileStatusDaoImpl implements FileStatusDao {
	
	@Autowired
	private final JedisPool pool = null;

	public void save(final String key, final String field,final String value) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Transaction tx = jedis.multi();
			tx.hset(key, field, value);
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

	public void update(final String key,final String field,
			final String content) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Transaction tx = jedis.multi();
			tx.hset(key, field, content);
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


	public void delete(final String key,final String field) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Transaction tx = jedis.multi();
			tx.hdel(key, field);
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

	public String find(final String key,final String field) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String str = jedis.hget(key, field);
			return str;
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

	public List<String> find( final String key,final String... fields) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			List<String> list = jedis.hmget(key, fields);
			return list;
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


}
