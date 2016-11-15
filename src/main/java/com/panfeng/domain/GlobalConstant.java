package com.panfeng.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.panfeng.util.ValidateUtil;

/**
 * 系统常量表
 * 
 * @author Jack
 *
 */
public final class GlobalConstant extends BaseObject {

	private static final long serialVersionUID = -7702371618133614803L;

	public static final String CONTEXT_RIGHT_MAP = "context_right_map"; // 权限集合

	public static final String CONTEXT_PORTAL_VIDEO_MAP = "context_portal_video_map"; // 首页视频集合
	
	public static final String STORAGE_NODE_RELATIONSHIP = "storage_node_relationship"; // 文件系统存储节点对应关系

	public static final String SESSION_INFO = "sessionInfo"; // 当前用户

	public static final String ROLE_EMPLOYEE = "role_employee"; // 用户身份 -- 内部员工

	public static final String ROLE_CUSTOMER = "role_customer"; // 用户身份 -- 客户

	public static final String ROLE_PROVIDER = "role_provider"; // 用户身份 -- 供应商

	// public static final String ROLE_MANAGER = "role_manager"; // 用户身份 -- 视频管家

	public static final String ROLE_SYSTEM = "role_system";// 系统输出

	public static final String CURRENT_INFO = "current_info"; // 当前登陆者信息

	public static final String UNIQUE_KEY = "0102030405060708"; // AES key

	public static final String PROCESS_STATUS = "process_status"; // 上传进度

	public static final String PROJECT_USER_INIT_PASSWORD = "000000"; // 项目用户初始密码

	public static final int SESSION_EXPIRE_TIME = 3600; // redis 存储session
														// 过期时间(秒),默认30分钟

	public static final String CONVERIONHSOT = "http://123.59.75.62:8080/";
	
	public static final String MAIL_MAP = "mail_map"; // 邮件集合
	
	public static final String FILE_LOCATE_STORAGE_PATH = "file_locate_storage_path"; // 分布式文件系统storage节点地址

	public static String COOKIES_SCOPE = null;

	public static String SOLR_URL; // 通过审核的供应商及分级的客户 SOLR URL 入口

	public static String SOLR_EMPLOYEE_URL; // 内部员工SOLR URL 入口

	public static String SOLR_PORTAL_URL; // 未审核的供应商及未分级的客户的SOLR URL 入口

	public static String FILE_PROFIX; // 文件前缀

	public static String EMPLOYEE_IMAGE_PATH; // 内部人员图片路径

	public static String STAFF_IMAGE_PATH; // 人员图片路径

	// pay
	public static String PAY_SIGN_KEY;

	public static String PAY_SERVER;

	public static String PAY_RETURN_SERVER;

	// ssl
	public static String KEY_STORE_TRUST_PATH; // truststore的路径

	public static String KEY_STORE_TYPE; // truststore的类型

	public static String KEY_STORE_TRUST_PASSWORD; // truststore的密码

	public static String KEY_STORE_CLIENT_PATH;

	public static String KEY_STORE_TYPE_P12;

	public static String KEY_STORE_PASSWORD;

	// activity.product.ids
	public static String ACTIVITY_PRODUCT_IDS;
	
	public static String UPLOAD_PATH;
	
	private static GlobalConstant GLOBALCONSTANT = new GlobalConstant();

	static {
		if (!ValidateUtil.isValid(FILE_PROFIX)) {
			final InputStream is = GLOBALCONSTANT.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
			try {
				Properties propertis = new Properties();
				propertis.load(is);
				SOLR_URL = propertis.getProperty("solr.url");
				SOLR_EMPLOYEE_URL = propertis.getProperty("solr.employee.url");
				SOLR_PORTAL_URL = propertis.getProperty("solr.portal.url");
				FILE_PROFIX = propertis.getProperty("file.prefix");
				EMPLOYEE_IMAGE_PATH = propertis.getProperty("upload.server.employee.image");
				STAFF_IMAGE_PATH = propertis.getProperty("upload.server.staff.image");

				// pay begin
				PAY_SIGN_KEY = propertis.getProperty("pay.sign.key");
				PAY_SERVER = propertis.getProperty("pay.server");
				PAY_RETURN_SERVER = propertis.getProperty("pay.return.server");
				// pay end
				// ssl
				KEY_STORE_TRUST_PATH = propertis.getProperty("key.store.trust.path");
				KEY_STORE_TYPE = propertis.getProperty("key.store.type");
				KEY_STORE_TRUST_PASSWORD = propertis.getProperty("key.store.trust.password");
				KEY_STORE_CLIENT_PATH = propertis.getProperty("key.store.client.path");
				KEY_STORE_TYPE_P12 = propertis.getProperty("key.store.type.p12");
				KEY_STORE_PASSWORD = propertis.getProperty("key.store.password");
				// cookie
				COOKIES_SCOPE = propertis.getProperty("cookies.scope");

				// activity.product.ids
				ACTIVITY_PRODUCT_IDS = propertis.getProperty("activity.product.ids");
				
				UPLOAD_PATH = propertis.getProperty("upload.path");
			} catch (Exception e) {

			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
