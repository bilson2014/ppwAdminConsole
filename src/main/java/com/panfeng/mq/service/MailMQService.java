package com.panfeng.mq.service;

import java.util.Map;

public interface MailMQService {

	// 向默认队列发送消息
	public void sendMessage(final String to, final String subject, final String content);

	public void sendMailsByType(String mailType, Map<String, String[]> map);
}
