package com.panfeng.mq.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.panfeng.mq.service.MailMQService;

/**
 * 邮件队列服务
 * @author Jack
 *
 */
@Service
public class MailMQServiceImpl implements MailMQService {

	@Autowired
	private final JmsTemplate mailJmsTemplate = null;
	
	@Override
	public void sendMessage(final String to, final String subject, final String content) {
		mailJmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				final Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("to", to);
				resultMap.put("subject", subject);
				resultMap.put("content", content);
				return session.createTextMessage(JSON.toJSONString(resultMap));
			}
		});
	}
}
