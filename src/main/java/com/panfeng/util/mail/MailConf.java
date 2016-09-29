package com.panfeng.util.mail;

//邮件配置信息
public class MailConf {
	
	private static MailConf mailConf = null;
	private static String host = ""; // 发送主机
	private static String sender = ""; // 用户名
	private static String password = ""; // 密码
	private static String from = ""; // 发件人邮箱
	private static String name = ""; // 发件人显示名
	
	private MailConf(){}
	
	public static MailConf getInstance(){
		if(null == mailConf){
			mailConf = new MailConf();
			host = PropertiesUtils.getProp("mail.host");
			sender = PropertiesUtils.getProp("mail.sender");
			password = PropertiesUtils.getProp("mail.password");
		}
		return mailConf;
	}

	public String getHost() {
		return host;
	}

	public String getSender() {
		return sender;
	}

	public String getPassword() {
		return password;
	}

	public String getFrom() {
		return from;
	}

	public static String getName() {
		return name;
	}

	
}
