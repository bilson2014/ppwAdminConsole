package com.panfeng.resource.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.BaseEntity;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.service.PmsRightFacade;
import com.paipianwang.pat.facade.right.service.PmsRoleFacade;
import com.paipianwang.pat.facade.user.entity.PmsUser;
import com.paipianwang.pat.facade.user.entity.ThirdBind;
import com.paipianwang.pat.facade.user.service.PmsUserFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.view.UserView;
import com.panfeng.util.DataUtil;
import com.panfeng.util.DateUtils;
import com.panfeng.util.Log;

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
	private final PmsRoleFacade pmsRoleFacade = null;

	@Autowired
	private final PmsRightFacade pmsRightFacade = null;

	@Autowired
	private final PmsUserFacade pmsUserFacade = null;

	@RequestMapping("/user-list")
	public ModelAndView view(final HttpServletRequest request, final ModelMap map) {

		return new ModelAndView("user-list", map);
	}

	@RequestMapping(value = "/user/list", method = RequestMethod.POST, produces = "application/json; chartset=UTF-8")
	public DataGrid<PmsUser> list(final UserView view, final PageParam pageParam) {

		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);

		// 封装查询参数
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userName", view.getUserName());
		paramMap.put("clientLevel", view.getClientLevel());
		paramMap.put("beginTime", view.getBeginTime());
		paramMap.put("endTime", view.getEndTime());
		paramMap.put("telephone", view.getTelephone());
		final DataGrid<PmsUser> dataGrid = pmsUserFacade.listWithPagination(pageParam, paramMap);
		return dataGrid;
	}

	@RequestMapping(value = "/user/update", method = RequestMethod.POST, produces = "application/json; chartset=UTF-8")
	public long update(final PmsUser user, HttpServletRequest request) {

		// final long ret = userService.update(user);
		final long ret = pmsUserFacade.update(user);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("user update ...", sessionInfo);
		return ret;
	}

	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	public long delete(final long[] ids, HttpServletRequest request) {

		if (ids.length > 0) {

			// final long ret = userService.delete(ids);
			final long ret = pmsUserFacade.delete(ids);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("delete user...", sessionInfo);
			return ret;
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("Delete User error ...", sessionInfo);
			throw new RuntimeException("Delete User error ...");
		}
	}

	@RequestMapping(value = "/user/save", method = RequestMethod.POST)
	public long save(final PmsUser user, HttpServletRequest request) {

		user.setPassword(DataUtil.md5(PublicConfig.INIT_PASSWORD));
		user.setBirthday(DateUtils.nowDate());
		user.setUpdateTime(DateUtils.nowTime());
		Map<String, Object> save = pmsUserFacade.save(user);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save user...", sessionInfo);
		Object object = save.get(BaseEntity.SAVE_MAP_ROWS);
		if(object != null){
			String rowStr = object.toString();
			Long row = Long.valueOf(rowStr);
			return row;
		}
		return 0;
	}

	/**
	 * 获取新注册的用户数量 用来提示客服
	 * 
	 * @return
	 */
	@RequestMapping("/user/getUnLevelUserNotice")
	public long findUnlevelUsers() {

		// final long count = userService.findUnlevelUsers();
		final long count = pmsUserFacade.findUnlevelUsers();
		return count;
	}

	/**
	 * 前台登录验证操作
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/user/loginout")
	public boolean loginout(final HttpServletRequest request) {
		// sessionService.removeSession(request);
		request.getSession().removeAttribute(PmsConstant.SESSION_INFO);
		return true;
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
		// final int count = userService.validationPhone(null,
		// loginName.get("loginName"));
		final int count = pmsUserFacade.validationPhone(null, loginName.get("loginName"));
		if (count > 0) {
			return true;
		}
		return false;
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
		final int count = pmsUserFacade.validationPhone(telephone, null);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 用户信息-修改用户基本信息(昵称、性别、真实姓名、电子邮件、QQ)
	 */
	@RequestMapping("/user/modify/info")
	public boolean modifyUserInfo(@RequestBody final PmsUser user, HttpServletRequest request) {

		boolean flag = true;
		try {
			if (user != null) {
				user.setEmail(URLDecoder.decode(user.getEmail(), "UTF-8"));
				user.setQq(URLDecoder.decode(user.getQq(), "UTF-8"));
				user.setWeChat(URLDecoder.decode(user.getWeChat(), "UTF-8"));
				user.setRealName(URLDecoder.decode(user.getRealName(), "UTF-8"));
				user.setUserName(URLDecoder.decode(user.getUserName(), "UTF-8"));

				if (user.getId() != 0) {
					// userService.modifyUserInfo(user);
					pmsUserFacade.modifyUserInfo(user);
					// add by wanglc 修改个人资料后,更新缓存 2016-7-26 19:27:47 begin
					// sessionService.removeSession(request);

					Gson gson = new Gson();
					String json = gson.toJson(user);
					initSessionInfo(gson.fromJson(json, PmsUser.class), request);
					// add by wanglc 修改个人资料后,更新缓存 2016-7-26 19:27:47 end

				} else
					flag = false;

			} else {
				flag = false;
			}

		} catch (UnsupportedEncodingException e) {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("User modify information decode error ...", sessionInfo);
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 用户信息-登录名，密码
	 */
	@RequestMapping("/user/modify/loginName")
	public boolean modifyLoginName(@RequestBody final PmsUser user, HttpServletRequest request) {
		if (user != null) {
			if (user.getId() != 0)
				// return userService.modifyUserLoginName(user) > 0 ? true :
				// false;
				return pmsUserFacade.modifyUserLoginName(user) > 0 ? true : false;

		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("User is null ..", sessionInfo);
		}
		return false;
	}

	/**
	 * 用户信息-修改用户密码
	 */
	@RequestMapping("/user/modify/password")
	public boolean modifyUserPassword(@RequestBody final PmsUser user) {

		boolean flag = true;
		if (user != null) {
			// userService.modifyUserPassword(user);
			pmsUserFacade.modifyUserPassword(user);
		} else {
			flag = false;
		}

		return flag;
	}

	/**
	 * 用户信息-修改用户手机号码
	 */
	/*
	 * @RequestMapping("/user/modify/phone") public boolean
	 * modifyUserPhone(@RequestBody final User user) {
	 * 
	 * boolean result = false; final long ret =
	 * userService.modifyUserPhone(user); if (ret > 0) { result = true; }
	 * 
	 * return result; }
	 */

	/**
	 * 用户信息-修改用户头像
	 */
	@RequestMapping("/user/modify/photo")
	public boolean modifyUserPhoto(@RequestBody final PmsUser user) {

		boolean result = false;
		// final long ret = userService.modifyUserPhoto(user);
		final long ret = pmsUserFacade.modifyUserPhoto(user);
		if (ret > 0) {
			result = true;
		}

		return result;
	}

	/**
	 * 根据用户ID 获取用户信息
	 */
	@RequestMapping("/user/info/{userId}")
	public PmsUser getUserById(@PathVariable("userId") final Long userId) {

		if (userId != null) {
			final PmsUser user = pmsUserFacade.findUserById(userId);
			return user;
		}
		return null;
	}

	/**
	 * 查询三方登录的用户是否存在 如果存在，则返回 如果不存在，则创建
	 */
	@RequestMapping("/user/thirdLogin/isExist")
	public Map<String, Object> verificationUserExist(@RequestBody final PmsUser user,
			final HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (user != null) {
			final List<PmsUser> users = pmsUserFacade.verificationUserExistByThirdLogin(user);
			if (users.size() < 1) { // 用户不存在
				map.put("code", "0");
				map.put("msg", "用户不存在");
			} else {
				final PmsUser u = users.get(0);
				if (null != u.getTelephone() && !"".equals(u.getTelephone())) {// 手机号存在,直接登录
					// 清除当前session
					Gson gson = new Gson();
					String json = gson.toJson(u);
					PmsUser user1 = gson.fromJson(json, PmsUser.class);
					// 存入session中
					initSessionInfo(user1, request);
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
	 * 根据客户名搜索客户
	 */
	@RequestMapping("/user/search/info")
	public List<PmsUser> getUserByName(@RequestBody final PmsUser user) {
		// List<User> users = userService.findUserByName(user);
		List<PmsUser> users = pmsUserFacade.findUserByName(user);
		return users != null ? users : new ArrayList<PmsUser>();
	}

	/**
	 * 添加简单客户
	 */
	/*
	 * @RequestMapping("/user/save/simple") public long
	 * addSimpleUser(@RequestBody final User user) { return
	 * userService.simpleSave(user);
	 * 
	 * }
	 */

	/**
	 * 获取全部客户
	 * 
	 * @return list
	 */
	@RequestMapping("/user/all")
	public List<PmsUser> all() {
		// List<User> list = userService.all();
		List<PmsUser> list = pmsUserFacade.all();
		return list;
	}

	/**
	 * 初始化 sessionInfo 信息
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	public boolean initSessionInfo(final PmsUser user, HttpServletRequest request) {
		final HttpSession session = request.getSession();

		// 清空session
		session.removeAttribute(PmsConstant.SESSION_INFO);

		// 存入session中
		final SessionInfo info = new SessionInfo();
		info.setLoginName(user.getLoginName());
		info.setRealName(user.getRealName());
		info.setSessionType(PmsConstant.ROLE_CUSTOMER);
		// info.setSuperAdmin(false);
		info.setToken(DataUtil.md5(session.getId()));
		info.setReqiureId(user.getId());
		info.setClientLevel(user.getClientLevel()); // 客户级别
		info.setTelephone(user.getTelephone());

		final PmsRole role = pmsRoleFacade.findRoleById(3l); // 获取用户角色
		final List<PmsRole> roles = new ArrayList<PmsRole>();
		roles.add(role);
		user.setRoles(roles);
		// 计算权限码总和
		final long maxPos = pmsRightFacade.getMaxPos();
		final long[] rightSum = new long[(int) (maxPos + 1)];
		user.setRightSum(rightSum);
		user.calculateRightSum();
		info.setSum(user.getRightSum());
		info.setEmail(user.getEmail());
		info.setPhoto(user.getImgUrl());
		info.setSuperAdmin(user.isSuperAdmin()); // 判断是否是超级管理员

		session.setAttribute(PmsConstant.SESSION_INFO, info);
		return true;
	}

	// add by wanglc 2016-7-6 15:13:47 第三方登录绑定页面验证手机号码 begin
	/**
	 * 第三方登录验证手机号码 register:0未注册||1注册 qq:0未绑定||1绑定 wechat: wb:
	 */
	@RequestMapping("/user/threeLogin/phone/{telephone}")
	public Map<String, Object> threeLoginPhone(@PathVariable("telephone") final String telephone) {
		Map<String, Object> map = new HashMap<String, Object>();
		// final User user = userService.threeLoginPhone(telephone);
		final PmsUser user = pmsUserFacade.threeLoginPhone(telephone);
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
		// Map<String, Object> map = userService.bindThird(bind);
		Map<String, Object> map = pmsUserFacade.bindThird(bind);
		if (map.containsKey("user")) {
			PmsUser user = (PmsUser) map.get("user");
			if (user.getId() != 0) {
				Gson gson = new Gson();
				initSessionInfo(gson.fromJson(gson.toJson(user), PmsUser.class), request);
			}
		}

		return map;
	}

	/**
	 * 查询第三方绑定状态
	 */
	@RequestMapping("/user/third/status")
	public Map<String, Object> thirdStatus(@RequestBody final PmsUser user, HttpServletRequest request) {
		// Map<String, Object> map = userService.thirdStatus(user);
		Map<String, Object> map = pmsUserFacade.thirdStatus(user);
		return map;
	}

	/**
	 * 用户资料页面绑定第三方
	 */
	@RequestMapping("/user/info/bind")
	public boolean userInfoBind(@RequestBody final PmsUser user, HttpServletRequest request) {
		// return userService.userInfoBind(user);
		return pmsUserFacade.userInfoBind(user);
	}

	/**
	 * 用户资料页面解除绑定第三方
	 */
	@RequestMapping("/user/info/unbind")
	public boolean userInfoUnBind(@RequestBody final PmsUser user, HttpServletRequest request) {
		// return userService.userInfoUnBind(user);
		return pmsUserFacade.userInfoUnBind(user);
	}

	/**
	 * 验证用户名昵称唯一性 true 可用 false 不可用
	 */
	@RequestMapping(value = "/user/unique/username", method = RequestMethod.POST)
	public boolean uniqueUserName(@RequestBody final PmsUser user, HttpServletRequest request) {
		// return userService.uniqueUserName(user);
		return pmsUserFacade.uniqueUserName(user);
	}

	/**
	 * 验证手机号是否存在,不存在就更新
	 */
	@RequestMapping(value = "/user/update/newphone", method = RequestMethod.POST)
	public BaseMsg updateNewphone(@RequestBody final PmsUser user, HttpServletRequest request) {
		// 验证是否存在
		// final int count = userService.validationPhone(user.getTelephone(),
		// null);
		final int count = pmsUserFacade.validationPhone(user.getTelephone(), null);
		if (count > 0) {
			return new BaseMsg(2, "手机号被占用");
		}
		// 修改手机号
		// final long ret = userService.modifyUserPhone(user);
		final long ret = pmsUserFacade.modifyUserPhone(user);
		if (ret > 0) {
			return new BaseMsg(3, "success");
		}
		return new BaseMsg(0, "error");
	}
}