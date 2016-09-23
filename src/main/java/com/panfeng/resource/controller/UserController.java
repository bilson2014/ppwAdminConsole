package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Role;
import com.panfeng.resource.model.ThirdBind;
import com.panfeng.resource.model.User;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.UserView;
import com.panfeng.service.RightService;
import com.panfeng.service.RoleService;
import com.panfeng.service.SessionInfoService;
import com.panfeng.service.UserService;
import com.panfeng.util.Constants.loginType;
import com.panfeng.util.DataUtil;
import com.panfeng.util.DateUtils;
import com.panfeng.util.Log;
import com.panfeng.util.ValidateUtil;

/**
 * 用户相关
 * 
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class UserController extends BaseController {

	@Autowired
	private final UserService userService = null;

	@Autowired
	private final SessionInfoService sessionService = null;

	@Autowired
	private final RoleService roleService = null;

	@Autowired
	private final RightService rightService = null;

//	private static Logger logger = LoggerFactory.getLogger("error");

	private static String INIT_PASSWORD;
	public UserController() {
		if (INIT_PASSWORD == null || "".equals(INIT_PASSWORD)) {
			final InputStream is = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
			try {
				Properties propertis = new Properties();
				propertis.load(is);
				INIT_PASSWORD = propertis.getProperty("initPassw0rd");
			} catch (IOException e) {
				Log.error("load Properties fail ...",null);
				e.printStackTrace();
			}
		}
	}

	@RequestMapping("/user-list")
	public ModelAndView view(final HttpServletRequest request, final ModelMap map) {

		return new ModelAndView("user-list", map);
	}

	@RequestMapping(value = "/user/list", method = RequestMethod.POST, produces = "application/json; chartset=UTF-8")
	public DataGrid<User> list(final UserView view, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);

		final List<User> list = userService.listWithPagination(view);
		final long total = userService.maxSize(view);
		final DataGrid<User> dataGrid = new DataGrid<User>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}

	@RequestMapping(value = "/user/update", method = RequestMethod.POST, produces = "application/json; chartset=UTF-8")
	public long update(final User user,HttpServletRequest request) {

		final long ret = userService.update(user);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("user update ...",sessionInfo);
		return ret;
	}

	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	public long delete(final long[] ids,HttpServletRequest request) {

		if (ids.length > 0) {

			final long ret = userService.delete(ids);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("delete user...",sessionInfo);
			return ret;
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("Delete User error ...",sessionInfo);
			throw new RuntimeException("Delete User error ...");
		}
	}

	@RequestMapping(value = "/user/save", method = RequestMethod.POST)
	public long save(final User user,HttpServletRequest request) {

		user.setPassword(DataUtil.md5(INIT_PASSWORD));
		user.setUpdateTime(DateUtils.nowTime());
		final long ret = userService.save(user);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save user...",sessionInfo);
		return ret;
	}

	/**
	 * 获取新注册的用户数量 用来提示客服
	 * 
	 * @return
	 */
	@RequestMapping("/user/getUnLevelUserNotice")
	public long findUnlevelUsers() {

		final long count = userService.findUnlevelUsers();
		return count;
	}

	/**
	 * 前台登录验证操作
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/user/encipherment")
	public boolean encryption(@RequestBody final User user, final HttpServletRequest request,
			final HttpServletResponse response) {
		User orignUser = null;
		if (user != null) {
			// modify by wanglc 2016-7-13 14:36:39 添加用户名密码登录begin
			// orignUser = userService.findUserByAttr(user);
			// 不为空
			if (user.getLoginType().equals(loginType.phone.getKey())) {
				orignUser = userService.findUserByAttr(user);
			} else if (user.getLoginType().equals(loginType.account.getKey())) {
				orignUser = userService.findUserByLoginNameAndPwd(user);
			}
			// modify by wanglc 2016-7-13 14:36:39 添加用户名密码登录begin
			if (orignUser != null) {
				// 清空当前session
				sessionService.removeSession(request);
				return initSessionInfo(orignUser, request);
			}
		}
		return false;
	}
	@RequestMapping("/user/checkPwd")
	public boolean chcekLoginNameAndPwd(@RequestBody final User user) {
		if (user == null)
			return false;
		User orignUser = userService.findUserByLoginNameAndPwd(user);
		return orignUser == null ? false : true;
	}

	/**
	 * 前台登录验证操作
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/user/loginout")
	public boolean loginout(final HttpServletRequest request) {
		sessionService.removeSession(request);
		return true;
	}

	/**
	 * 验证手机号是否被注册
	 * 
	 * @param telephone
	 *            手机号码
	 * @return 注册数量
	 */
	@RequestMapping("/user/valication/phone/{telephone}")
	public boolean validation(@PathVariable("telephone") final String telephone) {
		final int count = userService.validationPhone(telephone, null);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 验证用户名
	 * 
	 * @param telephone
	 *            手机号码
	 * @return 注册数量
	 */
	@RequestMapping("/user/valication/loginname")
	public boolean validationLoginName(@RequestBody Map<String, String> loginName) {
		if (!ValidateUtil.isValid(loginName)) {
			return false;
		}
		final int count = userService.validationPhone(null, loginName.get("loginName"));
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 用户注册
	 */
	@RequestMapping("/user/register")
	public boolean register(@RequestBody final User user, final HttpServletRequest request) {

		if (user != null) {
			final User result = userService.register(user);

			// 清空当前session
			sessionService.removeSession(request);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("save user...",sessionInfo);
			// 新增session
			return initSessionInfo(result, request);
		}

		return false;
	}

	/**
	 * 密码重置
	 */
	@RequestMapping("/user/recover")
	public boolean recover(@RequestBody final User user, final HttpServletRequest request) {
		// 获取 原用户信息
		if (user != null) {
			// 更新用户信息
			if (user.getUserName() != null || !"".equals(user.getUserName())) {

				userService.recover(user);
			}

			return true;
		}

		return false;
	}

	/**
	 * 用户信息-修改用户基本信息(昵称、性别、真实姓名、电子邮件、QQ)
	 */
	@RequestMapping("/user/modify/info")
	public boolean modifyUserInfo(@RequestBody final User user,HttpServletRequest request) {

		boolean flag = true;
		try {
			if (user != null) {
				user.setEmail(URLDecoder.decode(user.getEmail(), "UTF-8"));
				user.setQq(URLDecoder.decode(user.getQq(), "UTF-8"));
				user.setWeChat(URLDecoder.decode(user.getWeChat(), "UTF-8"));
				user.setRealName(URLDecoder.decode(user.getRealName(), "UTF-8"));
				user.setUserName(URLDecoder.decode(user.getUserName(), "UTF-8"));

				if (user.getId() != 0){
					userService.modifyUserInfo(user);
					//add by wanglc 修改个人资料后,更新缓存 2016-7-26 19:27:47 begin
					sessionService.removeSession(request);
					initSessionInfo(user, request);
					//add by wanglc 修改个人资料后,更新缓存 2016-7-26 19:27:47 end
					
				}
				else
					flag = false;

			} else {
				flag = false;
			}

		} catch (UnsupportedEncodingException e) {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("User modify information decode error ...",sessionInfo);
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 用户信息-登录名，密码
	 */
	@RequestMapping("/user/modify/loginName")
	public boolean modifyLoginName(@RequestBody final User user,HttpServletRequest request) {
		if (user != null) {
			if (user.getId() != 0)
				return userService.modifyUserLoginName(user) > 0 ? true : false;

		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("User is null ..",sessionInfo);
		}
		return false;
	}

	/**
	 * 用户信息-修改用户密码
	 */
	@RequestMapping("/user/modify/password")
	public boolean modifyUserPassword(@RequestBody final User user) {

		boolean flag = true;
		if (user != null) {

			userService.modifyUserPassword(user);
		} else {
			flag = false;
		}

		return flag;
	}

	/**
	 * 用户信息-修改用户手机号码
	 */
	@RequestMapping("/user/modify/phone")
	public boolean modifyUserPhone(@RequestBody final User user) {

		boolean result = false;
		final long ret = userService.modifyUserPhone(user);
		if (ret > 0) {
			result = true;
		}

		return result;
	}

	/**
	 * 用户信息-修改用户头像
	 */
	@RequestMapping("/user/modify/photo")
	public boolean modifyUserPhoto(@RequestBody final User user) {

		boolean result = false;
		final long ret = userService.modifyUserPhoto(user);
		if (ret > 0) {
			result = true;
		}

		return result;
	}

	/**
	 * 根据用户ID 获取用户信息
	 */
	@RequestMapping("/user/info/{userId}")
	public User getUserById(@PathVariable("userId") final Long userId) {

		if (userId != null) {
			final User user = userService.findUserById(userId);
			return user;
		}
		return null;
	}

	/**
	 * 查询三方登录的用户是否存在 如果存在，则返回 如果不存在，则创建
	 */
	@RequestMapping("/user/thirdLogin/isExist")
	public Map<String, Object> verificationUserExist(@RequestBody final User user, final HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (user != null) {
			final List<User> users = userService.verificationUserExistByThirdLogin(user);
			if (users.size() < 1) { // 用户不存在
				map.put("code", "0");
				map.put("msg", "用户不存在");
			} else {
				final User u = users.get(0);
				if (null != u.getTelephone() && !"".equals(u.getTelephone())) {// 手机号存在,直接登录
					// 清除当前session
					sessionService.removeSession(request);
					// 存入session中
					initSessionInfo(u, request);
					map.put("code", "2");
					map.put("msg", "用户可直接登录");
				} else {// 手机号不存在,绑定页
					map.put("code", "1");
					map.put("msg", "用户未绑定手机号");
					map.put("userId", Long.toString(u.getId()));
				}
			}
		}
		return map;
	}
	/**
	 * 查询三方登录的用户是否存在 如果存在，则返回 如果不存在，则创建
	 */
	/*
	 * @RequestMapping("/user/thirdLogin/isExist") public boolean
	 * verificationUserExist(@RequestBody final User user,final
	 * HttpServletRequest request){
	 * 
	 * if(user != null){ final List<User> users =
	 * userService.verificationUserExistByThirdLogin(user); if(users.size() <
	 * 1){ // 用户不存在 try { // 创建新用户 User createUser = user;
	 * createUser.setUserName(URLDecoder.decode(user.getUserName(), "UTF-8"));
	 * 
	 * userService.saveByThirdLogin(createUser); return true; } catch
	 * (UnsupportedEncodingException e) { logger.error(
	 * "Decode UserName ON LOGIN PROCESS BY JUDGE THIRD LOGIN USER EXIST ... ");
	 * e.printStackTrace(); } }else if(users.size() > 1){ // 用户存在多个 // 返回错误信息
	 * logger.error("Existing More Users Problem By User Login ,telephone=" +
	 * user.getTelephone() + ",password=" + user.getPassword()); return false;
	 * }else if(users.size() == 1){ // 有且仅有一个 // 清除当前session
	 * sessionService.removeSession(request); final User u = users.get(0); //
	 * 存入session中 return initSessionInfo(u, request);
	 * 
	 * } }
	 * 
	 * return false; }
	 */

	/**
	 * 根据客户名搜索客户
	 */
	@RequestMapping("/user/search/info")
	public List<User> getUserByName(@RequestBody final User user) {
		List<User> users = userService.findUserByName(user);
		return users != null ? users : new ArrayList<User>();
	}

	/**
	 * 添加简单客户
	 */
	@RequestMapping("/user/save/simple")
	public long addSimpleUser(@RequestBody final User user) {
		return userService.simpleSave(user);
		
	}

	/**
	 * 获取全部客户
	 * 
	 * @return list
	 */
	@RequestMapping("/user/all")
	public List<User> all() {
		List<User> list = userService.all();
		return list;
	}

	/**
	 * 初始化 sessionInfo 信息
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	public boolean initSessionInfo(final User user, HttpServletRequest request) {
		// 存入session中
		final String sessionId = request.getSession().getId();
		final SessionInfo info = new SessionInfo();
		info.setLoginName(user.getLoginName());
		info.setRealName(user.getRealName());
		info.setSessionType(GlobalConstant.ROLE_CUSTOMER);
		//info.setSuperAdmin(false);
		info.setToken(DataUtil.md5(sessionId));
		info.setReqiureId(user.getId());
		info.setClientLevel(user.getClientLevel()); // 客户级别
		info.setTelephone(user.getTelephone());

		final Role role = roleService.findRoleById(3l); // 获取用户角色
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		user.setRoles(roles);
		// 计算权限码总和
		final long maxPos = rightService.getMaxPos();
		final long[] rightSum = new long[(int) (maxPos + 1)];
		user.setRightSum(rightSum);
		user.calculateRightSum();
		info.setSum(user.getRightSum());
		info.setEmail(user.getEmail());
		info.setPhoto(user.getImgUrl());
		info.setSuperAdmin(user.isSuperAdmin()); // 判断是否是超级管理员

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GlobalConstant.SESSION_INFO, info);
		return sessionService.addSessionSeveralTime(request, map,60*60*24*7);//登陆用户存放七天
	}

	// add by wanglc 2016-7-6 15:13:47 第三方登录绑定页面验证手机号码 begin
	/**
	 * 第三方登录验证手机号码 register:0未注册||1注册 qq:0未绑定||1绑定 wechat: wb:
	 */
	@RequestMapping("/user/threeLogin/phone/{telephone}")
	public Map<String, Object> threeLoginPhone(@PathVariable("telephone") final String telephone) {
		Map<String, Object> map = new HashMap<String, Object>();
		final User user = userService.threeLoginPhone(telephone);
		map.put("qq", "0");
		map.put("wechat", "0");
		map.put("wb", "0");
		if (null != user) {
			map.put("register", "1");// 注册过
			if (null != user.getQqUnique() && !"".equals(user.getQqUnique())) {
				map.put("qq", "1");
			}
			if (null != user.getWechatUnique() && !"".equals(user.getWechatUnique())) {
				map.put("wechat", "1");
			}
			if (null != user.getWbUnique() && !"".equals(user.getWbUnique())) {
				map.put("wb", "1");
			}
		} else {
			map.put("register", "0");// 未注册过
		}
		return map;
	}
	// add by wanglc 2016-7-6 15:13:47 第三方登录绑定页面验证手机号码end

	// add by wanglc 2016-7-6 17:48:22 第三方 绑定 begin
	/**
	 * 三方用户不存在 code:0 1.手机号没注册过 phoneStatus 2.手机号注册过,但是未绑定第三方 thirdStatus
	 * 3.手机号注册过,且绑定了第三方 三方用户已经存在 code:1,但是未绑定手机 4.手机号没注册过 5.手机号注册过,但是未绑定第三方
	 * 6.手机号注册了,也绑定了第三方
	 */
	@RequestMapping("/user/bindthird")
	public Map<String, Object> bindThird(@RequestBody final ThirdBind bind, HttpServletRequest request) {
		Map<String, Object> map = userService.bindThird(bind);
		if (map.containsKey("user")) {
			User user = (User) map.get("user");
			if (user.getId() != 0) {
				initSessionInfo(user, request);
			}
		}
	
		return map;
	}
	
	/**
	 * 查询第三方绑定状态
	 */
	@RequestMapping("/user/third/status")
	public Map<String, Object> thirdStatus(@RequestBody final User user, HttpServletRequest request) {
		Map<String, Object> map = userService.thirdStatus(user);
		return map;
	}
	
	/**
	 * 用户资料页面绑定第三方
	 */
	@RequestMapping("/user/info/bind")
	public boolean userInfoBind(@RequestBody final User user, HttpServletRequest request) {
		return userService.userInfoBind(user);
	}
	/**
	 * 用户资料页面解除绑定第三方
	 */
	@RequestMapping("/user/info/unbind")
	public boolean userInfoUnBind(@RequestBody final User user, HttpServletRequest request) {
		return userService.userInfoUnBind(user);
	}
	/**
	 * 验证用户名昵称唯一性
	 * true 可用
	 * false 不可用
	 */
	@RequestMapping(value="/user/unique/username",method = RequestMethod.POST)
	public boolean uniqueUserName(@RequestBody final User user, HttpServletRequest request) {
		return userService.uniqueUserName(user);
	}
}