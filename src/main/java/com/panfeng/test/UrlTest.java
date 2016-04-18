package com.panfeng.test;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.panfeng.util.DataUtil;
import com.panfeng.util.UrlResourceUtils;

public class UrlTest {
	
	@Test
	public void urlTest() throws Exception{
		final String url = "127.0.0.1:8081/Film/login/wechat/callback.do";
		System.err.println(URLEncoder.encode(url, "UTF-8"));
	}
	
	@Test
	public void test2(){
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(new Date());
		System.err.println(time);
	}
	
	@Test
	public void test3(){
		final String str = "13001264061";
		System.err.println(str.substring(5));
	}
	
	@Test
	public void test4(){
		final String url = "/opt/portal/product/video/product2-20150627145349282.mp4";
		System.err.println(url.split("/opt")[1]);
	}
	
	@Test
	public void test5(){
		final String url = "/Pat4/portal/team/static/data/123";
		System.err.println(UrlResourceUtils.URLResolver(url, "/Pat4"));
	}
	
	@Test
	public void test6(){
		List<Long> list = new ArrayList<Long>();
		list.add(null);
		list.add(1l);
		list.add(2l);
		list.add(3l);
		list.add(3l);
		list.add(2l);
		System.err.println(list);
		
		Set<Long> set = new HashSet<Long>(list);
		System.err.println(set);
		set.add(3l);
		set.add(4l);
		set.add(2l);
		System.err.println(set);
	}
	
	@Test
	public void test7(){
		System.err.println(DataUtil.md5("123456"));
	}
	
	@Test
	public void test8(){
		System.err.println(2305843009211596781l & 16l);
	}
}
