package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.MailMapper;
import com.panfeng.resource.model.Mail;
import com.panfeng.resource.view.MailView;
import com.panfeng.service.MailService;

@Service
public class MailServiceImpl implements MailService{

	@Autowired
	private MailMapper mailMapper;
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

}
