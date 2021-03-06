package com.panfeng.resource.model;

import java.io.Serializable;

public class MailParam implements Serializable {

	private static final long serialVersionUID = -9034980767803855478L;

	private String from; // 发件人

	private String to; // 收件人

	private String subject; // 主题

	private String content; // 邮件内容
	
	

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
