package com.panfeng.dao;


import com.panfeng.resource.model.Mail;

public interface MailDao {

	public Mail getMailFromRedis(final String type);
	
	public void addMailByRedis(final Mail mail);
	
	public void removeMailFromRedis(final String type);
}
