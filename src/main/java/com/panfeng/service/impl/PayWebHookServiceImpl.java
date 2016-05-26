package com.panfeng.service.impl;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import cn.beecloud.BCCache;

import com.panfeng.resource.model.PayWebHook;
import com.panfeng.service.PayWebHookService;

@Service
public class PayWebHookServiceImpl implements PayWebHookService {

	public static String SUCCESS = "success";

	public static String FAIL = "fail";

	/**
	 * 1.认证签名<br>
	 * 2.消息去重复<br>
	 * 3.认证价格<br>
	 * 4.处理定订单相关逻辑&&返回结果<br>
	 */
	public String hookInfoProcessing(PayWebHook payWebHook) {
		// step 1
		boolean status = verify(payWebHook.getSign(), BCCache.getAppID()
				+ BCCache.getAppSecret(), payWebHook.getTimestamp(), "UTF-8");
		if (!status)
			return FAIL;// 签名认证失败

		// step 2 && step 3
		boolean duplicateMsg = filterAndAuthMsg(payWebHook);// 重复数据返回true
		if (duplicateMsg)
			return SUCCESS; // 重复消息

		// step 4
		logicProcess(payWebHook);

		return SUCCESS;
	}

	private boolean filterAndAuthMsg(PayWebHook payWebHook) {
		return true;
	}

	private void logicProcess(PayWebHook payWebHook) {

	}

	private boolean verify(String sign, String text, Long key,
			String input_charset) {
		text = text + key;
		String mysign = DigestUtils
				.md5Hex(getContentBytes(text, input_charset));

		long timeDifference = System.currentTimeMillis() - key;
		// 5分钟时差，
		if (mysign.equals(sign) && timeDifference <= 300000) {
			return true;
		} else {
			return false;
		}
	}

	private byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
					+ charset);
		}
	}
}
