package com.panfeng.persist;

import java.util.List;

import com.panfeng.resource.model.Mail;
import com.panfeng.resource.view.MailView;

public interface MailMapper {

	public List<Mail> listWithPagination(MailView view);

	public long maxSize(MailView view);

}
