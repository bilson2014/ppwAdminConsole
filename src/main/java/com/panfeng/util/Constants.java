package com.panfeng.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
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
	//价格区间
	public  static  Map<String,String> PRICE_RANGE_MAP ;
	//获知渠道
	public  static  Map<String,String> INFO_RESOURCE_MAP ;
	//业务范围
	public  static  Map<String,String> BUSINESS_MAP ;
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
		
		PRICE_RANGE_MAP =  new LinkedHashMap<String,String>();
		//获知渠道
		INFO_RESOURCE_MAP =  new LinkedHashMap<String,String>();
		//业务范围
		BUSINESS_MAP =  new LinkedHashMap<String,String>();
		
		PRICE_RANGE_MAP.put("0", "看情况");
		PRICE_RANGE_MAP.put("1", "1万元及以上");
		PRICE_RANGE_MAP.put("2", "2万元及以上");
		PRICE_RANGE_MAP.put("3", "3万元及以上");
		PRICE_RANGE_MAP.put("4", "5万元及以上");
		PRICE_RANGE_MAP.put("5", "10万元及以上");
		
		
		INFO_RESOURCE_MAP.put("0", "友情推荐");
		INFO_RESOURCE_MAP.put("1", "网络搜索");
		INFO_RESOURCE_MAP.put("2", "拍片帮");
		INFO_RESOURCE_MAP.put("3", "拍片网");
		INFO_RESOURCE_MAP.put("4", "电销");
		
		BUSINESS_MAP.put("0", "广告");
		BUSINESS_MAP.put("1", "宣传片");
		BUSINESS_MAP.put("2", "真人秀");
		BUSINESS_MAP.put("3", "纪录片");
		BUSINESS_MAP.put("4", "病毒视频");
		BUSINESS_MAP.put("5", "电视栏目");
		BUSINESS_MAP.put("6", "三维动画");
		BUSINESS_MAP.put("7", "MG动画");
		BUSINESS_MAP.put("8", "体育赛事");
		BUSINESS_MAP.put("9", "专题片");
		BUSINESS_MAP.put("10", "VR拍摄");
		BUSINESS_MAP.put("11", "产品拍摄");
		BUSINESS_MAP.put("12", "微电影");
		BUSINESS_MAP.put("13", "航拍");
		BUSINESS_MAP.put("14", "活动视频");
		BUSINESS_MAP.put("15", "后期制作");
		BUSINESS_MAP.put("16", "TVC");
		BUSINESS_MAP.put("17", "MV");
		BUSINESS_MAP.put("18", "包装");
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

	public enum mailType {
		DINNER,
		MEETING,
		REGESTER;
	}
}
