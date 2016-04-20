package com.panfeng.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class HttpUtil {

	public static HttpClientContext context = null;
	public static CookieStore cookieStore = null;
	public static RequestConfig requestConfig = null;

	private static CloseableHttpClient getClient(
			final HttpServletRequest request) {

		context = null;
		cookieStore = null;
		requestConfig = null;
		cookieStore = null;

		context = HttpClientContext.create();
		cookieStore = new BasicCookieStore();
		//addCookie("JSESSIONID", request.getSession().getId(), GlobalConstant.COOKIES_SCOPE, "/");
		// 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
		requestConfig = RequestConfig.custom().setConnectTimeout(120000)
				.setSocketTimeout(60000).setConnectionRequestTimeout(60000)
				.build();
		// 设置默认跳转以及存储cookie
		CloseableHttpClient client = HttpClientBuilder.create()
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.setRedirectStrategy(new DefaultRedirectStrategy())
				.setDefaultRequestConfig(requestConfig)
				.setDefaultCookieStore(cookieStore).build();

		return client;
	}


	

	public static String httpPost(final String url, final Object obj,
			final HttpServletRequest request) {
		CloseableHttpClient client = getClient(request);
		HttpPost httpPost = new HttpPost(url);
		Gson gson = new Gson();
		String param = gson.toJson(obj);
		String result = null;
		CloseableHttpResponse response = null;
		try {
			StringEntity s = new StringEntity(param.toString(), "utf-8");
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			httpPost.setEntity(s);
			response = client.execute(httpPost, context);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity());// 返回json格式
				if(result.contains("<!DOCTYPE html>"))
					result="";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void httpPostFileForm(final String url,
			final MultipartEntityBuilder multipartEntityBuilder,
			String outputPath) {
		CloseableHttpClient client = getClient(null);
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;

		try {
			httpPost.setEntity(multipartEntityBuilder.build());
			response = client.execute(httpPost, context);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream=response.getEntity().getContent();
				OutputStream outputStream=new FileOutputStream(new File(outputPath));
				saveTo(inputStream, outputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public static void saveTo(InputStream in, OutputStream out)
			throws Exception {
		byte[] data = new byte[1024];
		int index = 0;
		while ((index = in.read(data)) != -1) {
			out.write(data, 0, index);
		}
		out.flush();
		in.close();
		out.close();
	}

}
