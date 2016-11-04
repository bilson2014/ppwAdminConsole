package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.panfeng.persist.MailMapper;
import com.panfeng.resource.model.Mail;
import com.panfeng.resource.view.MailView;
import com.panfeng.service.MailService;
import com.panfeng.util.PropertiesUtils;

@Service
public class MailServiceImpl implements MailService{

	@Autowired
	private MailMapper mailMapper;
	@Autowired
    private JavaMailSenderImpl emailTemplate;

	@Override
	public List<Mail> listWithPagination(MailView view) {
		final List<Mail> list = mailMapper.listWithPagination(view);
		return list;
	}

	@Override
	public long maxSize(MailView view) {
		final long total = mailMapper.maxSize(view);
		return total;
	}

	@Override
	public void save(Mail mail) {
		mailMapper.save(mail);
	}

	@Override
	public void update(Mail mail) {
		mailMapper.update(mail);
	}

	@Override
	public long delete(final int[] ids) {
		if(ids.length>0){
			for(int id : ids){
				mailMapper.delete(id);
			}
			return 1l;
		}
		return 0l;
	}

	@Override
	public Mail getTemplateById(int mailId) {
		Mail mail = mailMapper.getTemplateById(mailId);
		return mail;
	}
	@Override
	public Mail getTemplateByType(String type) {
		Mail mail = mailMapper.getTemplateByType(type);
		return mail;
	}
	/**
	 * 发送单个邮件
	 */
	@Override
	public void sendMail(Mail mail,HttpServletRequest request) {
		String domain = "http://"+request.getServerName()+":"+request.getServerPort();
			new Thread(new Runnable() {
				@Override
				public void run() {
					send(mail,domain);
				}
			}).start();
	}
	/**
	 * 一次性发送若干邮件
	 * 线程池
	 */
	public void sendMails(List<Mail> list,HttpServletRequest request) {
		String domain = "http://"+request.getServerName()+":"+request.getServerPort();
		ExecutorService es = Executors.newFixedThreadPool(3);
		for(Mail mail : list){
			es.submit(new Runnable() {
				public void run() {
					send(mail,domain);
				}
			});
			new Thread().start();
		}
	}
	private void send(Mail mail, String domain) {
		 MimeMessage mailMessage = emailTemplate.createMimeMessage(); 
		    try {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true);
				messageHelper.setTo(mail.getReceiver()); 
				messageHelper.setFrom(PropertiesUtils.getProp("mail.sender")); 
				messageHelper.setSubject(mail.getSubject()); 
				String content = mail.getContent();
				Document document = Jsoup.parse(content);
				Elements elements = document.select("img");
				List<String> list = new ArrayList<String>();
				for(Element e : elements){
					String src = e.attr("src");
					src = domain+src;
					list.add(src);
					e.attr("src",src);
				}
				messageHelper.setText(document.toString(),true);
			} catch (MessagingException e) {
				e.printStackTrace();
			} 
		    emailTemplate.send(mailMessage);
	}
}
