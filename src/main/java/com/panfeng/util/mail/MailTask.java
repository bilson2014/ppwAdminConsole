package com.panfeng.util.mail;

/**
 * 邮件属性,发送信息实体类
 * @author wanglc
 * 2016-9-28 15:40:46
 */
public class MailTask {
	private String receiver;//接受者
	private String subject;//标题
	private String content;//内容
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
