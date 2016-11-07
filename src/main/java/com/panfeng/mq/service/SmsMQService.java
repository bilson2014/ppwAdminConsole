package com.panfeng.mq.service;

import com.panfeng.resource.model.SmsParam;

public interface SmsMQService {

	// 相对列插入数据
	public void sendMessage(final SmsParam sms);
}
