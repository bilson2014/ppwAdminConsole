package com.panfeng.mq.service;

public interface SmsMQService {

	// 相对列插入数据
	public void sendMessage(final String smsTemplateID,final String telephone, final String[] content);
}
