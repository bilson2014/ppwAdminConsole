package com.panfeng.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.panfeng.dao.MailDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.resource.model.Mail;
import com.panfeng.util.RedisUtils;
import com.panfeng.util.ValidateUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
@Repository(value = "mailDao")
public class MailDaoImpl implements MailDao{

	@Autowired
	private final JedisPool pool = null;
	
	public Mail getMailFromRedis(final String type) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String str = jedis.hget(GlobalConstant.MAIL_MAP, type);
			final Mail mail = RedisUtils.fromJson(str,Mail.class);
			return mail;
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


	public void addMailByRedis(final Mail mail) {
		
		if(mail != null){
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				final String str = RedisUtils.toJson(mail);
				if(ValidateUtil.isValid(str)){
					Transaction t = jedis.multi();
					t.hset(GlobalConstant.MAIL_MAP, mail.getMailType(), str);
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

	public void removeMailFromRedis(final String type) {
		if(ValidateUtil.isValid(type)){
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				Transaction tx = jedis.multi();
				tx.hdel(GlobalConstant.MAIL_MAP,type);
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
