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
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class HttpUtil {

	public static HttpClientContext context = null;
	public static CookieStore cookieStore = null;
	public static RequestConfig requestConfig = null;

	private static CloseableHttpClient getClient(final HttpServletRequest request) {

		context = null;
		cookieStore = null;
		requestConfig = null;
		cookieStore = null;

		context = HttpClientContext.create();
		cookieStore = new BasicCookieStore();
		// addCookie("JSESSIONID", request.getSession().getId(),
		// GlobalConstant.COOKIES_SCOPE, "/");
		// 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
		requestConfig = RequestConfig.custom().setConnectTimeout(1000*60*60).setSocketTimeout(1000*60*60)
				.setConnectionRequestTimeout(1000*60*60).build();
		// 设置默认跳转以及存储cookie
		CloseableHttpClient client = HttpClientBuilder.create()
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
				.setDefaultCookieStore(cookieStore).build();

		return client;
	}

	public static String httpPost(final String url, final Object obj, final HttpServletRequest request) {
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
				if (result.contains("<!DOCTYPE html>"))
					result = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// modify by laowang 2016.5.17 12:15 begin
	public static boolean httpPostFileForm(final String url, final MultipartEntityBuilder multipartEntityBuilder,
			String outputPath) {
		CloseableHttpClient client = getClient(null);
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;

		try {
			httpPost.setEntity(multipartEntityBuilder.build());
			response = client.execute(httpPost, context);
			// add 检测http返回状态判断转换是否完成
			// *****等待添加转服服务器返回转换状态识别
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				OutputStream outputStream = new FileOutputStream(new File(outputPath));
				saveTo(inputStream, outputStream);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	// modify by laowang 2016.5.17 12:15 end

	public static void saveTo(InputStream in, OutputStream out) throws Exception {
		byte[] data = new byte[1024];
		int index = 0;
		while ((index = in.read(data)) != -1) {
			out.write(data, 0, index);
		}
		out.flush();
		in.close();
		out.close();
	}

	/**
	 * 下载文件进行临时中转（内部存取转发到外网）
	 * 
	 * @param url
	 * @param request
	 * @return
	 */
	public static Object[] httpGetFile(final String url, final HttpServletRequest request) {
		CloseableHttpClient client = getClient(request);
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		Object[] objArray = new Object[2];
		try {
			response = client.execute(httpGet, context);
			String filename = null;
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// filename = response.getFirstHeader("Content-Disposition")
				// .getValue();
				HttpEntity httpEntity = response.getEntity();
				objArray[0] = filename;
				InputStream is = httpEntity.getContent();
				String tempNamt = PathFormatUtils.parse("{time}{rand:6}");
				File tempFile = new File(tempNamt);
				OutputStream os = new FileOutputStream(tempFile);
				saveTo(is, os);
				objArray[1] = tempFile;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objArray;
	}

}
