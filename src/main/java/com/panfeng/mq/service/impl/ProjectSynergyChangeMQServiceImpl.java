package com.panfeng.mq.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.panfeng.mq.service.ProjectSynergyChangeMQService;

@Service
public class ProjectSynergyChangeMQServiceImpl implements ProjectSynergyChangeMQService {

	@Autowired
	private final JmsTemplate projectSynergyJmsTemplate = null;

	@Override
	public void sendMessage(final String projectId) {
		if (StringUtils.isNotBlank(projectId)) {
			projectSynergyJmsTemplate.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(projectId);
				}
			});
		}
	}

}
