package com.panfeng.mq.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.panfeng.mq.service.MailMQService;
import com.panfeng.resource.model.MailParam;

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
	public void sendMessage(final MailParam mail) {
		mailJmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(JSONObject.toJSONString(mail));
			}
		});
	}
}
