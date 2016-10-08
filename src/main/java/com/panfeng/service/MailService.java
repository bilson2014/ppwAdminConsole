package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Mail;
import com.panfeng.resource.view.MailView;

public interface MailService {

	List<Mail> listWithPagination(MailView view);

	long maxSize(MailView view);

}
