package com.panfeng.util;

import java.io.UnsupportedEncodingException;
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
	
	public static String addHtm1(String tmp) {
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

	public static String addHtml(String tmp) {
		/*String str = " "+
				"<!DOCTYPE html>                                                                                                                                    "+     
				"<html class='no-js'>                                                                                                                               "+
				"<head>                                                                                                                                             "+
				"    <meta charset='utf-8'>                                                                                                                         "+
				"    <meta http-equiv='X-UA-Compatible' content='IE=9,chrome=1'>                                                                                    "+
				"    <meta name='viewport' content='width=device-width, initial-scale=1.0'>                                                                         "+
				"    <meta name='keywords' content='拍片网登录'>                                                                                                    "+
				"    <meta name='description' content=''>                                                                                                           "+
				"    <meta name='baidu-site-verification' content='dMz6jZpIwd' />                                                                                   "+
				"    <title>拍片网－pMail</title>                                                                                                                   "+
				"    <link rel='stylesheet' href='localhost/resources/css/pMail.css'>                                                                                         "+
				"</head>                                                                                                                                            "+
				"<body>                                                                                                                                             "+
				"    <div class='page'>                                                                                                                             "+
				"        <div class='mailTop'>                                                                                                                      "+
				"            <img src='resources/images/index/test1.png'>                                                                                           "+
				"            <span>Hi,小鱼儿</span>                                                                                                                 "+
				"        </div>                                                                                                                                     "+
				"        <div class='contentArea'>                                                                                                                  "+
				"            <div class='contentTop'>                                                                                                               "+
				"                <a href='http://www.apaipian.com/'><div class='logo'></div></a>                                                                    "+
				"                <div class='title'>专业商业视频服务平台</div>                                                                                      "+
				"                <a href='http://www.apaipian.com/'><div class='toPai'><span>进入官网>></span></div></a>                                            "+
				"            </div>                                                                                                                                 "+
				"            <div class='contentTag'>                                                                                                               "+
				"                <ul>                                                                                                                               "+
				"                    <li><a href='http://www.apaipian.com/about-us.html'><span>了解我们</span></a></li>                                             "+
				"                    <li><a href='http://www.apaipian.com/company-service.html'><span>服务流程</span></a></li>                                      "+
				"                    <li><a href='http://www.apaipian.com/company-activity.html'><span>服务协议</span></a></li>                                     "+
				"                    <li><a href='http://www.apaipian.com/member.html'><span>在线联系我们</span></a></li>                                           "+
				"                </ul>                                                                                                                              "+
				"            </div>                                                                                                                                 "+
				"            <div class='content'>                                                                                                                  "+
									tmp		+	                                                                                                                                        
				"            </div>                                                                                                                                 "+
				"        </div>                                                                                                                                     "+
				"        <div class='foot'>                                                                                                                         "+
				"        </div>                                                                                                                                     "+
				"        <div class='letter'>                                                                                                                       "+
				"            <div class='logoLetter'></div>                                                                                                         "+
				"            <div>                                                                                                                                  "+
				"                <label>感谢您一直以来对<a href='http://www.apaipian.com/'><span>拍片网</span></a>支持，我们将竭诚为您提供更多优质的服务</label>    "+
				"                <label>本邮件由系统自动发送，请勿直接回复！</label>                                                                                "+
				"                <label>了解更多请登录官方网站<a href='http://www.apaipian.com/'><span>http://www.apaipian.com/</span></a></label>                  "+
				"                <label class='info'>咨询方式</label>                                                                                               "+
				"                <ul class='linkInfo'>                                                                                                              "+
				"                    <li>                                                                                                                           "+
				"                        <a href='tencent://message/?uin=2640178216&Site=qq&Menu=no'>                                                               "+
				"                            <div class='linkLogo'></div>                                                                                           "+
				"                            <div class='linkTitle'>客户客服</div>                                                                                  "+
				"                        </a>                                                                                                                       "+
				"                    </li>                                                                                                                          "+
				"                    <li>                                                                                                                           "+
				"                         <a href='tencent://message/?uin=3299894058&Site=qq&Menu=no'>                                                              "+
				"                            <div class='linkLogoPro'></div>                                                                                        "+
				"                            <div class='linkTitle'>导演客服</div>                                                                                  "+
				"                        </a>                                                                                                                       "+
				"                    </li>                                                                                                                          "+
				"                    <li>                                                                                                                           "+
				"                        <a href='mailto:bdmarket@paipianwang.cn'>                                                                                  "+
				"                            <div class='linkLogoMail'></div>                                                                                       "+
				"                            <div class='linkTitle'>bdmarket@paipianwang.cn</div>                                                                   "+
				"                        </a>                                                                                                                       "+
				"                    </li>                                                                                                                          "+
				"                </ul>                                                                                                                              "+
				"                <h1><a href='tel:4006609728'><span class='telInfo'>400-660-9728</span></a></h1>                                                    "+
				"                <div class='workTime'>工作时间&nbsp&nbsp9:00-18:00&nbsp&nbsp(周一至周五)</div>                                                     "+
				"            </div>                                                                                                                                 "+
				"        </div>                                                                                                                                     "+
				"    </div>                                                                                                                                         "+
				"    <img src=''>                                                                                                                                   "+
				"    <script type='text/javascript' src='resources/lib/jquery/jquery-2.0.3.min.js'></script>                                                        "+
				"</body>                                                                                                                                            "+
				"</html>                                                                                                                                            "*/;
				String str = " "+
						"<html>                                                                                                                               "+
						"<head>                                                                                                                                             "+
						"    <title>拍片网－pMail</title>                                                                                                                   "+
						"    <link rel='stylesheet' href='localhost/resources/css/pMail.css'>   "+
						"<style type='text/css'>"+
					    "body {background-color: yellow}"+
					    "</style>                        "       +                                                       
						"</head>                                                                                                                                            "+
						"<body>                                                                                                                                             "+
						"    <div class='page' style='background:red'>                                                                                                                             "+
						"        <div class='mailTop'>                                                                                                                      "+
						"            <img src='resources/images/index/test1.png'>                                                                                           "+
						"            <span>Hi,小鱼儿</span>                                                                                                                 "+
						"        </div>                                                                                                                                     "+
						"        <div class='contentArea'>                                                                                                                  "+
						"            <div class='contentTop'>                                                                                                               "+
						"                <a href='http://www.apaipian.com/'><div class='logo'></div></a>                                                                    "+
						"                <div class='title'>专业商业视频服务平台</div>                                                                                      "+
						"                <a href='http://www.apaipian.com/'><div class='toPai'><span>进入官网>></span></div></a>                                            "+
						"            </div>                                                                                                                                 "+
						"            <div class='contentTag'>                                                                                                               "+
						"                <ul>                                                                                                                               "+
						"                    <li><a href='http://www.apaipian.com/about-us.html'><span>了解我们</span></a></li>                                             "+
						"                    <li><a href='http://www.apaipian.com/company-service.html'><span>服务流程</span></a></li>                                      "+
						"                    <li><a href='http://www.apaipian.com/company-activity.html'><span>服务协议</span></a></li>                                     "+
						"                    <li><a href='http://www.apaipian.com/member.html'><span>在线联系我们</span></a></li>                                           "+
						"                </ul>                                                                                                                              "+
						"            </div>                                                                                                                                 "+
						"            <div class='content'>                                                                                                                  "+
											tmp		+	                                                                                                                                        
						"            </div>                                                                                                                                 "+
						"        </div>                                                                                                                                     "+
						"        <div class='foot'>                                                                                                                         "+
						"        </div>                                                                                                                                     "+
						"        <div class='letter'>                                                                                                                       "+
						"            <div class='logoLetter'></div>                                                                                                         "+
						"            <div>                                                                                                                                  "+
						"                <label>感谢您一直以来对<a href='http://www.apaipian.com/'><span>拍片网</span></a>支持，我们将竭诚为您提供更多优质的服务</label>    "+
						"                <label>本邮件由系统自动发送，请勿直接回复！</label>                                                                                "+
						"                <label>了解更多请登录官方网站<a href='http://www.apaipian.com/'><span>http://www.apaipian.com/</span></a></label>                  "+
						"                <label class='info'>咨询方式</label>                                                                                               "+
						"                <ul class='linkInfo'>                                                                                                              "+
						"                    <li>                                                                                                                           "+
						"                        <a href='tencent://message/?uin=2640178216&Site=qq&Menu=no'>                                                               "+
						"                            <div class='linkLogo'></div>                                                                                           "+
						"                            <div class='linkTitle'>客户客服</div>                                                                                  "+
						"                        </a>                                                                                                                       "+
						"                    </li>                                                                                                                          "+
						"                    <li>                                                                                                                           "+
						"                         <a href='tencent://message/?uin=3299894058&Site=qq&Menu=no'>                                                              "+
						"                            <div class='linkLogoPro'></div>                                                                                        "+
						"                            <div class='linkTitle'>导演客服</div>                                                                                  "+
						"                        </a>                                                                                                                       "+
						"                    </li>                                                                                                                          "+
						"                    <li>                                                                                                                           "+
						"                        <a href='mailto:bdmarket@paipianwang.cn'>                                                                                  "+
						"                            <div class='linkLogoMail'></div>                                                                                       "+
						"                            <div class='linkTitle'>bdmarket@paipianwang.cn</div>                                                                   "+
						"                        </a>                                                                                                                       "+
						"                    </li>                                                                                                                          "+
						"                </ul>                                                                                                                              "+
						"                <h1><a href='tel:4006609728'><span class='telInfo'>400-660-9728</span></a></h1>                                                    "+
						"                <div class='workTime'>工作时间&nbsp&nbsp9:00-18:00&nbsp&nbsp(周一至周五)</div>                                                     "+
						"            </div>                                                                                                                                 "+
						"        </div>                                                                                                                                     "+
						"    </div>                                                                                                                                         "+
						"    <img src=''>                                                                                                                                   "+
						"    <script type='text/javascript' src='resources/lib/jquery/jquery-2.0.3.min.js'></script>                                                        "+
						"</body>                                                                                                                                            "+
						"</html>                    ";
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
	
	
	
	public static  String decorate(String[] str, String content) {
		for(int i=1;i<=str.length;i++){
			content = content.replaceAll("\\{"+ i +"\\}", str[i-1]);
		}
		return content;
	}
	/*
	public static <T> String decorate(T t, String content) {
		try {
			//1.替换信息
			Field[] fs = t.getClass().getDeclaredFields();
			for(Field f : fs){
				f.setAccessible(true); //设置属性是可以访问的
				String name = f.getName();//获取属性
				Object val = f.get(t);//获取属性值
				content = content.replaceAll("\\$\\{"+name+"\\}", null == val?"":String.valueOf(val));
			};
		} catch (SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return content;
	}*/

	public static String addImgHost(String content) {
		return content.replaceAll("@.@", "http://resource.apaipian.com/resource/");
	}
	
	
}