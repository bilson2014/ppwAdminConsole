package com.panfeng.resource.model;


import java.io.File;

import com.panfeng.domain.BaseObject;
public class Mail extends BaseObject{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	/**
     * 邮件主题
     */
	private String subject = null;
	/**
     * Email发送的内容
     */
	private String content = null;
	private String mailType = null;
	private String createTime = null;
	private String updateTime = null;
	
	
	/**
     * 邮件接收者
     */
	private String receiver = null;//接收人
	/**
     * 邮件发送者
     */
	private String sender = ""; // 发送者
	 /**
     * 邮件附件
     */
    private File [] attachFile;
    
    
    //===========邮件内容替换属性===============
    private String userName;
    
    
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public File[] getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(File[] attachFile) {
		this.attachFile = attachFile;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getMailType() {
		return mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
}
