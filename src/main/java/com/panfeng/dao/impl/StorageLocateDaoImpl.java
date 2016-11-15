package com.panfeng.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.panfeng.dao.StorageLocateDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.util.ValidateUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Repository("storageLocateDao")
public class StorageLocateDaoImpl implements StorageLocateDao {

	@Autowired
	final JedisPool pool = null;
	
	@Override
	public Map<String, String> getStorageFromRedis(final String id) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Map<String,String> nodeMap = jedis.hgetAll(GlobalConstant.STORAGE_NODE_RELATIONSHIP);
			if(ValidateUtil.isValid(nodeMap)){
				return nodeMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
		
		return null;
	}

	@Override
	public void resetStorageFromRedis(final Map<String, String> nodes) {
		if(ValidateUtil.isValid(nodes)){
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				Transaction tx = jedis.multi();
				tx.del(GlobalConstant.STORAGE_NODE_RELATIONSHIP);
				tx.hmset(GlobalConstant.STORAGE_NODE_RELATIONSHIP, nodes);
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
