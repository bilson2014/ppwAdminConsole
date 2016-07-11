package com.panfeng.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.panfeng.dao.PortalVideoDao;
import com.panfeng.resource.model.Product;
import com.panfeng.service.ProductService;

@SuppressWarnings("rawtypes")
@Component
public class PortalVideoListener implements ApplicationListener{

	@Autowired
	private final ProductService service = null;
	
	@Autowired
	private final PortalVideoDao dao = null; // Redis对象，用来获取首页视频
	
	public void onApplicationEvent(final ApplicationEvent event) {
		
		// 是否是上下文刷新事件
		if(event instanceof ContextRefreshedEvent){
			
			// 获取推荐值大于0的首页视频
			final Map<Long,Product> map = service.getProductByRecommend();
			
			// 重置redis中的首页视频内容
			dao.resetPortalVideo(map);
		}
	}

}
