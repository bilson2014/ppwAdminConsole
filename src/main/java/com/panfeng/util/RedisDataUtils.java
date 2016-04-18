package com.panfeng.util;

import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.resource.model.Right;

/**
 * redis 数据相关
 * @author Jack
 *
 */
public class RedisDataUtils {

	/**
	 * 获取单个权限
	 * @param pool
	 * @param uri 转换之后的uri
	 * @return 
	 */
	public static Right getRightFromRedis(final JedisPool pool,final String uri){
		Jedis jedis = null;
		try {
			System.err.println(pool);
			jedis = pool.getResource();
			String str = jedis.hget(GlobalConstant.CONTEXT_RIGHT_MAP, uri);
			final Right right = RedisUtils.fromJson(str,Right.class);
			return right;
		} catch (Exception e) {
			
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
		
		return null;
	}
	
	/**
	 * 获取全部权限
	 * @param pool
	 * @param uri 转换之后的uri
	 * @return 
	 */
	public static Map<String,Right> getRightsFromRedis(final JedisPool pool){
		Jedis jedis = null;
		try {
			System.err.println(pool);
			jedis = pool.getResource();
			Map<String,String> map = jedis.hgetAll(GlobalConstant.CONTEXT_RIGHT_MAP);
			if(ValidateUtil.isValid(map)){
				final Map<String,Right> rightMap = RedisUtils.fromJson(map);
				return rightMap;
			}
			
			return null;
		} catch (Exception e) {
			
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
		
		return null;
	}
	
	/**
	 * 新增单个权限
	 * @param pool
	 * @param right 权限实体
	 */
	public static void addRightByRedis(final JedisPool pool,final Right right){
		if(right != null){
			Jedis jedis = null;
			try {
				System.err.println(pool);
				jedis = pool.getResource();
				final String str = RedisUtils.toJson(right);
				if(ValidateUtil.isValid(str)){
					Transaction t = jedis.multi();
					jedis.hset(GlobalConstant.CONTEXT_RIGHT_MAP, right.getUrl(), str);
					t.exec();
				}
			} catch (Exception e) {
				
			} finally {
				if(jedis != null){
					jedis.disconnect();
					jedis.close();
				}
			}
		}
	}
	
	public static void resetRightFromRedis(final JedisPool pool,final Map<String,Right> map){
		if(ValidateUtil.isValid(map)){
			Jedis jedis = null;
			try {
				System.err.println(pool);
				jedis = pool.getResource();
				Transaction tx = jedis.multi();
				final Map<String,String> rightMap = RedisUtils.toJson(map);
				jedis.hmset(GlobalConstant.CONTEXT_RIGHT_MAP, rightMap);
				tx.exec();
			} catch (Exception e) {
				
			} finally {
				if(jedis != null){
					jedis.disconnect();
					jedis.close();
				}
			}
			
		}
	}
}
