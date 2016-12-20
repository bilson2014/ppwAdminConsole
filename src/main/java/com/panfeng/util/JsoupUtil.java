package com.panfeng.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Base64Utils;

public class JsoupUtil {

	/**
	 * 获取html中的img的src
	 */
	public static List<String> getImgSrc(String content) {
		List<String> list = new ArrayList<>();
		try {
			byte[] b = content.getBytes("UTF-8");
			content = new String(Base64Utils.decode(b),"UTF-8");
			Document document = Jsoup.parse(content);
			Elements elements = document.select("img");
			for(Element e : elements){
				String src = e.attr("src");
				if(src.contains("@.@")){
					src = src.replace("@.@", "");
				}
				list.add(src);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return list;
	}
}
