package com.panfeng.test;

import org.junit.Test;

import com.panfeng.util.HttpsUtils;

public class HttpsTest extends BaseTest {
	@Test
	public void testGet(){
		String str= HttpsUtils.httpsGet("https://192.168.0.101:8443", null, false);
		System.out.println(str);
	}
	
	@Test
	public void testPost(){
		String str= HttpsUtils.httpsPost("https://192.168.0.101:8443","", null, false);
		System.out.println(str);
	}
}
