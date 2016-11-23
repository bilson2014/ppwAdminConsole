package com.panfeng.service;

import java.util.List;
import com.panfeng.resource.model.Mail;
import com.panfeng.resource.view.MailView;

public interface MailService {

	List<Mail> getAll();
	
	List<Mail> listWithPagination(MailView view);

	long maxSize(MailView view);

	void save(Mail mail);

	void update(Mail mail);

	long delete(final int[] ids);

	Mail getTemplateById(int mailId);
	
	Mail getTemplateByType(String type);

}
