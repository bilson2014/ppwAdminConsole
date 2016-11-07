package com.panfeng.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.panfeng.resource.model.Mail;
import com.panfeng.resource.view.MailView;

public interface MailService {

	List<Mail> listWithPagination(MailView view);

	long maxSize(MailView view);

	void save(Mail mail);

	void update(Mail mail);

	long delete(final int[] ids);

	Mail getTemplateById(int mailId);
	
	Mail getTemplateByType(String type);

	void sendMail(Mail mail,HttpServletRequest request);
	
	void sendMails(List<Mail> mail,HttpServletRequest request);

	void decorateMails(List<Mail> list,String type);

	

}
