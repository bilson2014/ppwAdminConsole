package com.panfeng.mq.service;

import java.util.Map;

import com.panfeng.resource.model.MailParam;

public interface MailMQService {

	// 向默认队列发送消息
	public void sendMessage(final MailParam mail);

	public void sendMailsByType(String mailType, Map<String, String[]> map);
}
