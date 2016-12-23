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
import com.panfeng.mq.service.FileConvertMQService;

@Service
public class FileConvertMQServiceImpl implements FileConvertMQService {

	@Autowired
	private final JmsTemplate fileConverJmsTemplate = null;
	
	@Override
	public void sendMessage(final long productId, final String convertFileId) {
		fileConverJmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				final Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("productId", productId);
				resultMap.put("convertFileId", convertFileId);
				return session.createTextMessage(JSON.toJSONString(resultMap));
			}
		});
	}

}
