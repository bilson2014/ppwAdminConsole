package com.panfeng.listener;

import java.util.Map;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;

import com.panfeng.dao.RightDao;
import com.panfeng.resource.model.Right;
import com.panfeng.service.RightService;

@Component
@SuppressWarnings("rawtypes")
public class RightListener implements ApplicationListener {

	@Autowired
	private final RightService rightService = null;
	
	@Autowired
	private final RightDao dao = null;
	
	@Autowired
	private final JedisPool pool = null;

	public void onApplicationEvent(final ApplicationEvent event) {
		
		// 是否是上下文刷新事件
		if(event instanceof ContextRefreshedEvent){
			
			final Map<String,Right> map = rightService.getRights();
			
			dao.resetRightFromRedis(map);
		}

	}

}
