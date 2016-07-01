package com.panfeng.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.panfeng.domain.GlobalConstant;

public class HttpsUtils {
	// 静态httpclient对象，保证不需要每次重新创建https
	public static CloseableHttpClient httpclient;
	static int TIME_OUT = 50000;

	// 获得池化得HttpClient
	static {
		SSLContext sslcontext = null;
		try {
			// 设置truststore
			//KeyStore trustStore = KeyStore.getInstance(GlobalConstant.KEY_STORE_TYPE);
			KeyStore trustStore = KeyStore.getInstance(GlobalConstant.KEY_STORE_TYPE);
			// 设置个人证书
			KeyStore keyStore = KeyStore.getInstance(GlobalConstant.KEY_STORE_TYPE_P12);
			InputStream ksIn = new FileInputStream(GlobalConstant.KEY_STORE_CLIENT_PATH);
			InputStream tsIn = new FileInputStream(new File(GlobalConstant.KEY_STORE_TRUST_PATH));
			try {
				keyStore.load(ksIn, GlobalConstant.KEY_STORE_PASSWORD.toCharArray());
				trustStore.load(tsIn, GlobalConstant.KEY_STORE_TRUST_PASSWORD.toCharArray());
			} finally {
				try {
					ksIn.close();
					tsIn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
					.loadKeyMaterial(keyStore, GlobalConstant.KEY_STORE_PASSWORD.toCharArray()).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 客户端支持TLSV1，TLSV2,TLSV3这三个版本
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
				new String[] { "TLSv1", "TLSv2", "TLSv3" }, null,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());// 客户端验证服务器身份的策略

		// Create a registry of custom connection socket factories for supported
		// protocol schemes.
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				// .register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext)).build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		// Configure total max or per route limits for persistent connections
		// that can be kept in the pool or leased by the connection manager.
		connManager.setMaxTotal(100);
		connManager.setDefaultMaxPerRoute(30);
		httpclient = HttpClients.custom().setConnectionManager(connManager).setSSLSocketFactory(sslsf).build();
	}

	public static String httpsPost(String url, Object obj, HttpServletRequest request,boolean keepLogin) {

		String result = null;
		HttpClientContext context = HttpClientContext.create();
		HttpPost httpPost = new HttpPost(url);
		if(keepLogin){
			context.setCookieStore(buildCookieStore(request));
		}
		// 设置请求的配置
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT)
				.setConnectionRequestTimeout(TIME_OUT).build();
		httpPost.setConfig(requestConfig);
		Gson gson = new Gson();
		String param = gson.toJson(obj);
		try {
			StringEntity stringEntity = new StringEntity(param.toString(), "utf-8");
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");// 发送json数据需要设置contentType
			httpPost.setEntity(stringEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost, context);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity());
			}
			EntityUtils.consume(response.getEntity());
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String httpsGet(String url,HttpServletRequest request,boolean keepLogin) {
		String result = null;
		HttpClientContext context = HttpClientContext.create();
		HttpGet httpget = new HttpGet(url);
		if(keepLogin){
			context.setCookieStore(buildCookieStore(request));
		}
		// 设置请求的配置
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT)
				.setConnectionRequestTimeout(TIME_OUT).build();
		httpget.setConfig(requestConfig);
		try {

			CloseableHttpResponse response = httpclient.execute(httpget, context);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity());
			}
			EntityUtils.consume(response.getEntity());
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// ----------------------------------------------cookie-----------------------------------------------------------------

	public static CookieStore buildCookieStore(HttpServletRequest request){
		CookieStore	cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", request.getSession().getId());
		cookie.setDomain(GlobalConstant.COOKIES_SCOPE);
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return cookieStore;
	}
}
