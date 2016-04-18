package com.panfeng.util;

import org.apache.commons.lang.StringUtils;

/**
 * URL工具类
 * @author Jack
 *
 */

public class UrlResourceUtils {

	public static String URLResolver(String url,final String contextPath){
		
		// 如果字符串有作用
		if(ValidateUtil.isValid(url)){
			// 去掉前缀
			if(ValidateUtil.isValid(contextPath)){
				if(!"/".equals(contextPath)){
					url = url.replace(contextPath, "");
				}
			}
			
			// 替换数字部分
			String[] reg = url.split("/");
			if(ValidateUtil.isValid(reg)){
				for (final String str : reg) {
					if(ValidateUtil.isValid(str)){
						if(StringUtils.isNumeric(str)){
							url = url.replace(str, "{param}");
						}
					}
				}
			}
		}
		
		return url;
	}
	
}
