package com.panfeng.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Base64;
/**
 * 提供mail模板
 * 2016-10-12 15:27:38
 */
public class MailTemplateFactory {

	public static String getRegistMailTpl(){
		String str = " "+
		"<!DOCTYPE html>"+
		"<html lang='en'>"+
		"<head>"+
		"    <meta charset='utf-8'>"+
		"    <title>Swiper demo</title>"+
		"    <style>"+
		"    body {"+
		"        background: #eee;"+
		"        font-family: Helvetica Neue, Helvetica, Arial, sans-serif;"+
		"        font-size: 14px;"+
		"        color:#000;"+
		"        margin: 0;"+
		"        padding: 0;"+
		"    }"+
		"    img{"+
		"    	height: 200px;"+
		"    	width: 200px;"+
		"    }"+
		"    </style>"+
		"</head>"+
		"<body>"+
		"   <a href='http://www.baidu.com'><img src='http://apaipian.com/product/img/product659-20160818142295282.jpg'></a>"+
		"   <a href='http://www.baidu.com'><img src='http://apaipian.com/product/img/product560-201608181355288132.jpg'></a>"+
		"   <a href='http://www.baidu.com'><img src='http://apaipian.com/product/img/product1785-201609081853488952.JPG'></a>"+
		"</body>"+
		"</html>";
		return str;
	}
	
	public static String addHtml(String tmp) {
		String str = " "+
				"<html>"+
				"<head>"+
				"</head>"+
				"<body>"+
				tmp+
				"</body>"+
				"</html>";
			return str;
	}

	/**
	 * 1.转码
	 * 2.添加首尾html
	 */
	public static <T> String decorate(String content) {
			try {
				//1.转码
				content = new String(Base64.getDecoder().decode(content),"utf-8");
				//2.添加首尾模板
				content = addHtml(content);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		return content;
	}
	/**
	 * 1.转码
	 * 2.反射替换对象信息 ${userName}→wanglc
	 * 3.添加首尾html
	 */
	public static <T> String decorate(T t, String content) {
		try {
			//1.转码
			content = new String(Base64.getDecoder().decode(content),"utf-8");
			//2.替换对象信息
			Field[] fs = t.getClass().getDeclaredFields();
			for(Field f : fs){
				f.setAccessible(true); //设置属性是可以访问的
				String name = f.getName();//获取属性
				Object val = f.get(t);//获取属性值
				content = content.replaceAll("\\$\\{"+name+"\\}", null == val?"":String.valueOf(val));
			};
			//3.添加首尾模板
			content = addHtml(content);
		} catch (UnsupportedEncodingException | SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return content;
	}
}