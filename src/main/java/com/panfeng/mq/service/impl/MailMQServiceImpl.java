package com.panfeng.mq.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.panfeng.dao.MailDao;
import com.panfeng.mq.service.MailMQService;
import com.panfeng.persist.MailMapper;
import com.panfeng.resource.model.Mail;
import com.panfeng.util.Log;
import com.panfeng.util.MailTemplateFactory;
/**
 * 邮件队列服务
 * @author Jack
 *
 */
@Service
public class MailMQServiceImpl implements MailMQService {

	@Autowired
	private final JmsTemplate mailJmsTemplate = null;
	@Autowired
	private final MailDao mailDao = null;
	@Autowired
	private final MailMapper mailMapper = null;
	
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

	@Override
	public void sendMailsByType(String mailType, Map<String, String[]> map) {
			Mail m = mailDao.getMailFromRedis(mailType);
			if(null == m)m = mailMapper.getTemplateByType(mailType);
			if(null != m){
				try {
					String content = m.getContent();
					//1.转码
					content = new String(Base64.getDecoder().decode(content),"utf-8");
					//2.添加首尾模板
					content = MailTemplateFactory.addHtml(content);
					Iterator<Entry<String, String[]>> i = map.entrySet().iterator(); 
					while(i.hasNext()){
						Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>)i.next();
						String email = entry.getKey().toString();
						String[] str = entry.getValue();
						String c = MailTemplateFactory.decorate(str,content);
						// 发送邮件
						sendMessage(email,m.getSubject(),c);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else{
				Log.error("the mail type" + mailType + " is not exist",null);
			}
	}
}
