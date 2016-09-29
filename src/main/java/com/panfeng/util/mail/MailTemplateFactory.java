package com.panfeng.util.mail;

//提供mail模板
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
	//	"    }"+
		"</head>"+
		"<body>"+
		"   <a href='http://www.baidu.com'><img src='http://apaipian.com/product/img/product659-20160818142295282.jpg'></a>"+
		"   <a href='http://www.baidu.com'><img src='http://apaipian.com/product/img/product560-201608181355288132.jpg'></a>"+
		"   <a href='http://www.baidu.com'><img src='http://apaipian.com/product/img/product1785-201609081853488952.JPG'></a>"+
		"</body>"+
		"</html>";
		return str;
	};
}
