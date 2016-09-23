package com.panfeng.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author dawn 常量类
 */
public final class Constants {

	public static String FILE_PROFIX;
	public static String PRODUCT_DESCRIPTION_IMAGE_PATH;
	public static String PRODUCT_DESCRIPTION_IMAGE_URL;
	public static String INDENT_RESOURCE_PATH;
	public static String PROJECT_PDF;
	public static String PROJECT_DOC;
	public static String PDF2HTML;
	public static String OFFICEHOME;
	public static String COOKIES_SCOPE;
	static Constants CONSTANTS = new Constants();

	// state Constants
	public final static String FAIL = "FAIL";
	public final static String SUCCESS = "SUCCESS";

	public final static int MSG_FAIL = 1;
	public final static int MSG_SUCCESS = 0;

	public final static int ENABLED = 0;
	public final static int DISABLED = 1;
	
	public final static int INVOICE_STATUS_OK = 1;
	public final static int INVOICE_STATUS_NO = 2;

	private Constants() {
		load();
	}

	public void reLoadConstants() {
		load();
	}

	private static void load() {
		InputStream is = Constants.class.getClassLoader().getResourceAsStream(
				"jdbc.properties");
		try {
			Properties propertis = new Properties();
			propertis.load(is);
			full(propertis);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private static void full(Properties properties) {
		FILE_PROFIX = properties.getProperty("file.prefix");
		PRODUCT_DESCRIPTION_IMAGE_PATH = properties
				.getProperty("upload.server.product.description.image");
		PRODUCT_DESCRIPTION_IMAGE_URL = properties
				.getProperty("upload.server.product.description.image.url");
		INDENT_RESOURCE_PATH = properties.getProperty("indent.resource.path");
		PROJECT_PDF = properties.getProperty("project.pdf");
		PROJECT_DOC = properties.getProperty("project.doc");
		PDF2HTML = properties.getProperty("pdf2html");
		OFFICEHOME = properties.getProperty("officehome");
		COOKIES_SCOPE = properties.getProperty("cookies_scope");
	}
	/**
	 * 登录方式
	 */
	public enum loginType {
		phone("phone", 1), // 手机登录
		account("loginName", 2);// 账号登录
		// 成员变量
		private String key;
		private int value;

		// 构造方法
		private loginType(String key, int value) {
			this.key = key;
			this.value = value;
		}

		// get set 方法
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
	}


}
