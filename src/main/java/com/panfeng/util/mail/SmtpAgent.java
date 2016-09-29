package com.panfeng.util.mail;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
/**
 * send mail action
 * @author wanglc
 *2016-9-28 17:44:18
 */
public class SmtpAgent {
    private static final String DEF_CHARSET = "UTF-8";
    
    private static ExecutorService es = Executors.newCachedThreadPool();
    public static ExecutorService getExecutorService(){
    	return es;
    };
    private String host = "";                //发送主机
    
    private String sender = "";            //用户名
    private String password = "";       //密码
    
    private String from = "";               //发件人邮箱
    private String name = "";             //发件人显示名
    
    private String charset = DEF_CHARSET;//字符编码
    public SmtpAgent(MailConf conf) {
    	this(conf.getHost(), conf.getSender(), conf.getPassword(), conf.getSender());
    }
    public SmtpAgent(String host, String sender, String password, String name) {
        this(host, sender, password, sender, name);
    }
    public SmtpAgent(String host, String sender, String password, String from, String name){
        this(host, sender, password, from, name, DEF_CHARSET);
    }
    public SmtpAgent(String host, String sender, String password, String from, String name,String charset){
        this.host = host;
        this.sender = sender;
        this.password = password;
        this.from = from;
        this.name = name;
        this.charset = charset;
    }

    public void setHost(String host){
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public String getSender()  {
        return sender;
    }

    public void setPassword(String password)  {
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public String getCharset(){
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void send(MailTask mk) {
		getExecutorService().submit(new Runnable() {
			@Override
			public void run() {
				try {
					send(mk.getReceiver(),mk.getSubject(),mk.getContent(),null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		new Thread().start();
    }
    
    public void send(String receiver, String subject, String content, Map<String, String> attachments)throws Exception{

		HtmlEmail email = new HtmlEmail();
		email.setHostName(getHost());
		email.setCharset(getCharset());
		email.setAuthentication(getSender(), getPassword());    
		
		email.setFrom(getFrom(), getName());
		email.addTo(receiver);
		email.setSubject(subject);
		email.setCharset(getCharset());
		email.setHtmlMsg(content);
		
		if (attachments != null) 
		{
		    Set<Map.Entry<String, String>> entrySet = attachments.entrySet();
		    for (Map.Entry<String, String> entry : entrySet) 
		    {
		        String name = entry.getKey();
		        String path = entry.getValue();
		        
		        EmailAttachment ea = new EmailAttachment();
		        ea.setName(MimeUtility.encodeText(name));
		        ea.setDescription(name);
		        ea.setPath(path);
		        ea.setDisposition(EmailAttachment.ATTACHMENT);
		        email.attach(ea);
		    }
		}
		email.send();
    }
	
}
