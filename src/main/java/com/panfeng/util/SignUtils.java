package com.panfeng.util;

import com.panfeng.resource.model.Sign;

public class SignUtils {
	/**
	 * 认证签名
	 * 
	 * @param signStr
	 * @param sign
	 * @return
	 */
	static boolean authSign(String signStr, Sign sign) {
		if (signStr != null && !signStr.isEmpty() && sign != null) {
			String orignal = sign.getTimestamp() + sign.getKey() + sign.getRandom();
			String encryptStr = SHA256Utils.SHA256Encrypt(orignal);
			return encryptStr.equals(signStr) ? true : false;
		} else
			return false;
	}

	/**
	 * 签名
	 * 
	 * @param sign
	 * @return
	 */
	static String generateSign(Sign sign) {
		String orignal = sign.getTimestamp() + sign.getKey() + sign.getRandom();
		String encryptStr = SHA256Utils.SHA256Encrypt(orignal);
		return encryptStr;
	}

}
