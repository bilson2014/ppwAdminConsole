package com.panfeng.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.paipianwang.pat.facade.right.entity.PmsRight;
import com.paipianwang.pat.facade.right.service.PmsRightFacade;

@Component
@SuppressWarnings("rawtypes")
public class RightListener implements ApplicationListener {

	@Autowired
	private final PmsRightFacade pmsRightFacade = null;

	public void onApplicationEvent(final ApplicationEvent event) {

		// 是否是上下文刷新事件
		if (event instanceof ContextRefreshedEvent) {
			final Map<String, PmsRight> map = pmsRightFacade.getRightsMergeMap();
			pmsRightFacade.resetRightOnRedis(map);
		}

	}

}
