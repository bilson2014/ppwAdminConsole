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
	public static String FILE_PROFIX; // 文件前缀

	public static String EMPLOYEE_IMAGE_PATH; // 内部人员图片路径

	public static String STAFF_IMAGE_PATH; // 人员图片路径

	public static String PAY_APPID;// 支付平台支付id

	public static String PAY_APPSECRET;

	public static String PAY_TESTSECRET;
	
	public static String PAY_MASTERSECRET;

	private static GlobalConstant GLOBALCONSTANT = new GlobalConstant();

	static {
		if (!ValidateUtil.isValid(FILE_PROFIX)) {
			final InputStream is = GLOBALCONSTANT.getClass().getClassLoader()
					.getResourceAsStream("jdbc.properties");
			try {
				Properties propertis = new Properties();
				propertis.load(is);
				FILE_PROFIX = propertis.getProperty("file.prefix");
				EMPLOYEE_IMAGE_PATH = propertis
						.getProperty("upload.server.employee.image");
				STAFF_IMAGE_PATH = propertis
						.getProperty("upload.server.staff.image");
				
				//pay begin
				PAY_APPID=propertis.getProperty("pay.appid");
				PAY_APPSECRET=propertis.getProperty("pay.appsecret");
				PAY_TESTSECRET=propertis.getProperty("pay.testsecret");
				PAY_MASTERSECRET=propertis.getProperty("pay.mastersecret");
				//pay end
				
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
