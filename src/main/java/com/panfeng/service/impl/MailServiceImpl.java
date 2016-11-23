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
public class MailServiceImpl implements MailService {
	@Autowired
	private MailMapper mailMapper;
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
		// 保存到数据库
		mailMapper.save(mail);
		// 保存到redis
		mailDao.addMailByRedis(mail);
	}

	@Override
	public void update(Mail mail) {
		mailMapper.update(mail);
		// 保存到redis
		mailDao.addMailByRedis(mail);
	}

	@Override
	public long delete(final int[] ids) {
		if (ids.length > 0) {
			for (int id : ids) {
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

	@Override
	public List<Mail> getAll() {
		return mailMapper.getAll();
	}
}
