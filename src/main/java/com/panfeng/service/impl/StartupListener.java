package com.panfeng.service.impl;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.beecloud.BeeCloud;

import com.panfeng.domain.GlobalConstant;


public class StartupListener implements
		ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent evt) {
		if (evt.getApplicationContext().getParent() == null) {
			registerBeeCloudService();
		}
	}

	/**
	 * 容器启动完成注册 BeeCloudService
	 */
	private void registerBeeCloudService() {
		BeeCloud.registerApp(
				GlobalConstant.PAY_APPID,
				GlobalConstant.PAY_TESTSECRET,
				GlobalConstant.PAY_APPSECRET,
				GlobalConstant.PAY_MASTERSECRET);
	}
}
