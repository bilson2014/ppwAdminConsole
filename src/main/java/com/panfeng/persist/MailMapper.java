package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Mail;
import com.panfeng.resource.view.MailView;

public interface MailMapper {

	public List<Mail> listWithPagination(MailView view);

	public long maxSize(MailView view);

	public void save(Mail mail);

	public void update(Mail mail);

	public long delete(@Param("id") final int id);

	public Mail getTemplateById(@Param("mailId") final int mailId);

	public Mail getTemplateByType(@Param("type")final String type);

}
