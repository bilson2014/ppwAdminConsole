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
import com.panfeng.mq.service.SmsMQService;

@Service
public class SmsMQServiceImpl implements SmsMQService {

	@Autowired
	private final JmsTemplate smsJmsTemplate = null;
	
	@Override
	public void sendMessage(final String smsTemplateID, final String telephone, final String[] content) {
		smsJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				final Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("smsTemplateID", smsTemplateID);
				resultMap.put("telephone", telephone);
				resultMap.put("content", content);
				return session.createTextMessage(JSON.toJSONString(resultMap));
			}
		});
	}

}
