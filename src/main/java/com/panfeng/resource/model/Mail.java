package com.panfeng.resource.model;


import java.io.Serializable;
/**
 * mail模板
 */
public class Mail implements Serializable {

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
	 * 收件人
	 */
	private String receiver;
	private String receiverRole;
	/**
	 * 抄送人
	 */
	private String bcc;
	private String bccRole;
	/**
	 * 发件人
	 */
	private String sender;//邮箱-以逗号分隔
	private String senderRole;
	/**
	 * 附件
	 */
	private String mailFile;
	
    
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
	
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getBccRole() {
		return bccRole;
	}
	public void setBccRole(String bccRole) {
		this.bccRole = bccRole;
	}
	
	public String getMailFile() {
		return mailFile;
	}
	public void setMailFile(String mailFile) {
		this.mailFile = mailFile;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiverRole() {
		return receiverRole;
	}
	public void setReceiverRole(String receiverRole) {
		this.receiverRole = receiverRole;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSenderRole() {
		return senderRole;
	}
	public void setSenderRole(String senderRole) {
		this.senderRole = senderRole;
	}
	
}
