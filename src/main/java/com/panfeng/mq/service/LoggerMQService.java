package com.panfeng.mq.service;

import com.panfeng.resource.model.LoggerParam;

public interface LoggerMQService {

	// 向队列发送消息
	public void sendMessage(final LoggerParam log);
}
