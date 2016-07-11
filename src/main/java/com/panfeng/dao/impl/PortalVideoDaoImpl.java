package com.panfeng.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.panfeng.dao.PortalVideoDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.resource.model.Product;
import com.panfeng.util.RedisUtils;
import com.panfeng.util.ValidateUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Repository("portalVideoDao")
public class PortalVideoDaoImpl implements PortalVideoDao {
	
	@Autowired
	final JedisPool pool = null;

	@Override
	public void resetPortalVideo(final Map<Long, Product> productMap) {
		
		if(ValidateUtil.isValid(productMap)){
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				Transaction tx = jedis.multi();
				final Map<String,String> jsonMap = RedisUtils.productMaptoJson(productMap);
				tx.del(GlobalConstant.CONTEXT_PORTAL_VIDEO_MAP);
				tx.hmset(GlobalConstant.CONTEXT_PORTAL_VIDEO_MAP, jsonMap);
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

	@Override
	public Map<Long, Product> getProductsFromRedis() {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Map<String,String> map = jedis.hgetAll(GlobalConstant.CONTEXT_PORTAL_VIDEO_MAP);
			if(ValidateUtil.isValid(map)){
				final Map<Long,Product> productMap = RedisUtils.productsFromJson(map);
				return productMap;
			}
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
