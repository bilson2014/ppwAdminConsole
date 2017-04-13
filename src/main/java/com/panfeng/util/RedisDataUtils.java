package com.panfeng.util;

import java.util.Map;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsRight;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

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
	public static PmsRight getRightFromRedis(final JedisPool pool,final String uri){
		Jedis jedis = null;
		try {
			System.err.println(pool);
			jedis = pool.getResource();
			String str = jedis.hget(PmsConstant.CONTEXT_RIGHT_MAP, uri);
			final PmsRight right = RedisUtils.fromJson(str,PmsRight.class);
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
	public static Map<String,PmsRight> getRightsFromRedis(final JedisPool pool){
		Jedis jedis = null;
		try {
			System.err.println(pool);
			jedis = pool.getResource();
			Map<String,String> map = jedis.hgetAll(PmsConstant.CONTEXT_RIGHT_MAP);
			if(ValidateUtil.isValid(map)){
				final Map<String,PmsRight> rightMap = RedisUtils.fromJson(map);
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
	public static void addRightByRedis(final JedisPool pool,final PmsRight right){
		if(right != null){
			Jedis jedis = null;
			try {
				System.err.println(pool);
				jedis = pool.getResource();
				final String str = RedisUtils.toJson(right);
				if(ValidateUtil.isValid(str)){
					Transaction t = jedis.multi();
					jedis.hset(PmsConstant.CONTEXT_RIGHT_MAP, right.getUrl(), str);
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
	
	public static void resetRightFromRedis(final JedisPool pool,final Map<String,PmsRight> map){
		if(ValidateUtil.isValid(map)){
			Jedis jedis = null;
			try {
				System.err.println(pool);
				jedis = pool.getResource();
				Transaction tx = jedis.multi();
				final Map<String,String> rightMap = RedisUtils.toJson(map);
				jedis.hmset(PmsConstant.CONTEXT_RIGHT_MAP, rightMap);
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
