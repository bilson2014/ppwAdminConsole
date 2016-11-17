package com.panfeng.util;

import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Base64Utils;

public class JsoupUtil {

	public static String base64delHostImg(String content) {
		try {
			byte[] b = content.getBytes("UTF-8");
			content = new String(Base64Utils.decode(b),"UTF-8");
			Document document = Jsoup.parse(content);
			Elements elements = document.select("img");
			for(Element e : elements){
				String src = e.attr("src");
				//去掉host
				String re="((?:http|https)(?::\\/{2}[\\w]+).*?/)";
				src = src.replaceFirst(re, "@.@");
				e.attr("src", src);
			}
			b = Base64Utils.encode(document.toString().getBytes("UTF-8"));
			content = new String(b,"UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return content;
	}
}
