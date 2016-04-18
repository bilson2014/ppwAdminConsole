package com.panfeng.util;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * MD5 加密
 */
public class DataUtil {
	
	public static String md5(String src){
		try {
			StringBuffer buffer = new StringBuffer();
			char[] chars= {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] data = md.digest(src.getBytes());
			for(byte b : data){
				buffer.append(chars[(b >> 4) & 0x0F]);
				buffer.append(chars[b & 0x0F]);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	public static String getUuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
