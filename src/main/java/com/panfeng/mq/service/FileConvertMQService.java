package com.panfeng.mq.service;

public interface FileConvertMQService {

	/**
	 * 向默认队列发送消息
	 * @param productId 产品ID
	 * @param convertFileId 待转换文件ID
	 */
	public void sendMessage(final long productId, final String convertFileId);
}
