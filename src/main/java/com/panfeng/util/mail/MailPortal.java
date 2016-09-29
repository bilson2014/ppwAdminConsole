package com.panfeng.util.mail;


public class MailPortal {

	public static void main(String[] args) {
		MailTask mk = new MailTask();
		mk.setContent(MailTemplateFactory.getRegistMailTpl());
		mk.setReceiver("1061942069@qq.com");
		mk.setSubject("我爱你");
		MailConf mc = MailConf.getInstance();
		new SmtpAgent(mc).send(mk);
	}
}
