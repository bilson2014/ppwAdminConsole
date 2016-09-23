package com.panfeng.dao.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.panfeng.dao.SessionInfoDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.util.DataUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Repository("sessionInfoDao")
public class SessionInfoDaoImpl implements SessionInfoDao {

	@Autowired
	private final JedisPool pool = null;

	public boolean addSession(final HttpServletRequest request, Map<String, String> map) {
		Jedis jedis = null;
		try {
			final String sessionId = request.getSession().getId();
			jedis = pool.getResource();
			Transaction tx = jedis.multi();
			tx.hmset(DataUtil.md5(sessionId), map);
			tx.expire(DataUtil.md5(sessionId), GlobalConstant.SESSION_EXPIRE_TIME);
			tx.exec();
			return true;
		} catch (Exception e) {
			// do something for logger
		} finally {
			if (jedis != null) {
				jedis.disconnect();
				jedis.close();
			}
		}

		return false;
	}

	public void removeSession(final HttpServletRequest request) {

		Jedis jedis = null;
		try {
			final String sessionId = request.getSession().getId();
			jedis = pool.getResource();
			Transaction tx = jedis.multi();
			tx.hdel(DataUtil.md5(sessionId), GlobalConstant.SESSION_INFO);
			tx.exec();
		} catch (Exception e) {
			// do something for logger
		} finally {
			if (jedis != null) {
				jedis.disconnect();
				jedis.close();
			}
		}
	}

	public void updateSession(final HttpServletRequest request, final String field, final String value) {
		Jedis jedis = null;
		try {
			final String sessionId = request.getSession().getId();
			jedis = pool.getResource();
			Transaction tx = jedis.multi();
			tx.hset(DataUtil.md5(sessionId), field, value);
			tx.exec();
		} catch (Exception e) {
			// do something for logger
		} finally {
			if (jedis != null) {
				jedis.disconnect();
				jedis.close();
			}
		}

	}

	public Map<String, String> getSessionWithAllFields(final HttpServletRequest request) {
		Jedis jedis = null;
		try {
			final String sessionId = request.getSession().getId();
			jedis = pool.getResource();
			Map<String, String> map = jedis.hgetAll(DataUtil.md5(sessionId));
			return map;
		} catch (Exception e) {
			// do something for logger
		} finally {
			if (jedis != null) {
				jedis.disconnect();
				jedis.close();
			}
		}

		return null;
	}

	public String getSessionWithField(final HttpServletRequest request, final String field) {

		Jedis jedis = null;
		try {
			final String sessionId = request.getSession().getId();
			jedis = pool.getResource();
			final String str = jedis.hget(DataUtil.md5(sessionId), field);
			return str;
		} catch (Exception e) {
			// do something for logger
		} finally {
			if (jedis != null) {
				jedis.disconnect();
				jedis.close();
			}
		}
		return null;
	}

	public String getSessionWithSessionId(final String sessionId, final String field) {

		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			final String str = jedis.hget(DataUtil.md5(sessionId), field);
			return str;
		} catch (Exception e) {
			// do something for logger
		} finally {
			if (jedis != null) {
				jedis.disconnect();
				jedis.close();
			}
		}
		return null;
	}

	public boolean exitSession(final HttpServletRequest request) {

		Jedis jedis = null;
		try {
			final String sessionId = request.getSession().getId();
			jedis = pool.getResource();
			final boolean ret = jedis.hexists(DataUtil.md5(sessionId), GlobalConstant.SESSION_INFO);
			return ret;
		} catch (Exception e) {
			// do something for logger
		} finally {
			if (jedis != null) {
				jedis.disconnect();
				jedis.close();
			}
		}
		return false;
	}

}
