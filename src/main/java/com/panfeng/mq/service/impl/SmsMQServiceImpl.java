package com.panfeng.mq.service.impl;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.panfeng.mq.service.SmsMQService;
import com.panfeng.resource.model.SmsParam;

@Service
public class SmsMQServiceImpl implements SmsMQService {

	@Autowired
	private final JmsTemplate smsJmsTemplate = null;
	
	@Override
	public void sendMessage(final SmsParam sms) {
		smsJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(JSONObject.toJSONString(sms));
			}
		});
	}

}
