package com.panfeng.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.panfeng.dao.MailDao;
import com.panfeng.persist.MailMapper;
import com.panfeng.resource.model.Mail;
import com.panfeng.resource.view.MailView;
import com.panfeng.service.MailService;
@Service
public class MailServiceImpl implements MailService{
	@Autowired
	private MailMapper mailMapper;
	//@Autowired
   // private JavaMailSenderImpl emailTemplate;
	@Autowired
    private MailDao mailDao;

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
		//保存到数据库
		mailMapper.save(mail);
		//保存到redis
		mailDao.addMailByRedis(mail);
	}

	@Override
	public void update(Mail mail) {
		mailMapper.update(mail);
		//保存到redis
		mailDao.addMailByRedis(mail);
	}

	@Override
	public long delete(final int[] ids) {
		if(ids.length>0){
			for(int id : ids){
				Mail mail = mailMapper.getTemplateById(id);
				mailDao.removeMailFromRedis(mail.getMailType());
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
	/*@Override
	public void sendMail(Mail mail,HttpServletRequest request) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					send(mail);
				}
			}).start();
	}*/
	/**
	 * 一次性发送若干邮件
	 * 线程池
	 */
	/*public void sendMails(List<Mail> list,HttpServletRequest request) {
		ExecutorService es = Executors.newFixedThreadPool(3);
		for(Mail mail : list){
			es.submit(new Runnable() {
				public void run() {
					send(mail);
				}
			});
			new Thread().start();
		}
	}*/
	/*private void send(Mail mail) {
		MimeMessage mailMessage = emailTemplate.createMimeMessage(); 
	    try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true);
			messageHelper.setTo(mail.getReceiver()); 
			messageHelper.setFrom(PropertiesUtils.getProp("mail.sender")); 
			messageHelper.setSubject(mail.getSubject()); 
			messageHelper.setText(mail.getContent(),true);
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
	    emailTemplate.send(mailMessage);
	}*/
}
