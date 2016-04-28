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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.panfeng.resource.model.User;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.UserView;
import com.panfeng.service.RightService;
import com.panfeng.service.RoleService;
import com.panfeng.service.SessionInfoService;
import com.panfeng.service.UserService;
import com.panfeng.util.DataUtil;

/**
 * 用户相关
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

	private static Logger logger = LoggerFactory.getLogger("error");
	
	
	private static String INIT_PASSWORD;
	
	public UserController() {
		if(INIT_PASSWORD == null || "".equals(INIT_PASSWORD)){
			final InputStream is = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties"); 
			try {
				Properties propertis = new Properties();
				propertis.load(is);
				INIT_PASSWORD = propertis.getProperty("initPassw0rd");
			} catch (IOException e) {
				logger.error("load Properties fail ...");
				e.printStackTrace();
			}
		}
	}

	@RequestMapping("/user-list")
	public ModelAndView view(final HttpServletRequest request,final ModelMap map){
		
		return new ModelAndView("user-list",map);
	}
	
	@RequestMapping(value = "/user/list",method = RequestMethod.POST,
					produces = "application/json; chartset=UTF-8")
	public DataGrid<User> list(final UserView view,final PageFilter pf){
		
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
	
	@RequestMapping(value = "/user/update",method = RequestMethod.POST,produces = "application/json; chartset=UTF-8")
	public long update(final User user){
		
		final long ret = userService.update(user);
		return ret;
	}
	
	@RequestMapping(value = "/user/delete",method = RequestMethod.POST)
	public long delete(final long[] ids){
		
		if(ids.length > 0){
			
			final long ret = userService.delete(ids);
			return ret;
		}else {
			logger.error("Delete User error ...");
			throw new RuntimeException("Delete User error ...");
		}
	}
	
	@RequestMapping(value = "/user/save",method = RequestMethod.POST)
	public long save(final User user){
		
		user.setPassword(DataUtil.md5(INIT_PASSWORD));
		final long ret = userService.save(user);
		return ret;
	}
	
	/**
	 * 前台登录验证操作
	 * @param user
	 * @return
	 */
	@RequestMapping("/user/encipherment")
	public boolean encryption(@RequestBody final User user,final HttpServletRequest request){
		if(user != null){
			// 不为空
			final User orignUser = userService.findUserByAttr(user);
			
			if(orignUser != null){
				
				// 清空当前session
				sessionService.removeSession(request);
				
				return initSessionInfo(orignUser, request);
			}
			
			
		}
		return false;
	}
	
	/**
	 * 前台登录验证操作
	 * @param user
	 * @return
	 */
	@RequestMapping("/user/loginout")
	public boolean loginout(final HttpServletRequest request){
		sessionService.removeSession(request);
		return true;
	}
	
	/**
	 * 验证手机号是否被注册
	 * @param telephone 手机号码
	 * @return 注册数量
	 */
	@RequestMapping("/user/valication/phone/{telephone}")
	public boolean validation(@PathVariable("telephone") final String telephone){
		final int count = userService.validationPhone(telephone);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 用户注册
	 */
	@RequestMapping("/user/register")
	public boolean register(@RequestBody final User user,final HttpServletRequest request){
		
		if(user != null){
			final User result = userService.register(user);
			
			// 清空当前session
			sessionService.removeSession(request);
			// 新增session
			return initSessionInfo(result, request);
		}
		
		return false;
	}
	
	/**
	 * 密码重置
	 */
	@RequestMapping("/user/recover")
	public boolean recover(@RequestBody final User user,final HttpServletRequest request){
		// 获取 原用户信息
		if(user != null){
			// 更新用户信息
			if(user.getUserName() != null || !"".equals(user.getUserName())){
				
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
	public boolean modifyUserInfo(@RequestBody final User user){
		
		boolean flag = true;
		try {
			if(user != null){
				user.setEmail(URLDecoder.decode(user.getEmail(), "UTF-8"));
				user.setQq(URLDecoder.decode(user.getQq(), "UTF-8"));
				user.setRealName(URLDecoder.decode(user.getRealName(), "UTF-8"));
				user.setUserName(URLDecoder.decode(user.getUserName(), "UTF-8"));
				
				if(user.getId() != 0)
					userService.modifyUserInfo(user);
				else
					flag = false;
				
			}else {
				flag = false;
			}
			
		} catch (UnsupportedEncodingException e) {
			logger.error("User modify information decode error ...");
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * 用户信息-修改用户密码
	 */
	@RequestMapping("/user/modify/password")
	public boolean modifyUserPassword(@RequestBody final User user){
		
		boolean flag = true;
		if(user != null){
			
			userService.modifyUserPassword(user);
		}else {
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 用户信息-修改用户手机号码
	 */
	@RequestMapping("/user/modify/phone")
	public boolean modifyUserPhone(@RequestBody final User user){
		
		boolean result = false;
		final long ret = userService.modifyUserPhone(user);
		if(ret > 0){
			result = true;
		}
	
		return result;
	}
	
	/**
	 * 用户信息-修改用户头像
	 */
	@RequestMapping("/user/modify/photo")
	public boolean modifyUserPhoto(@RequestBody final User user){
		
		boolean result = false;
		final long ret = userService.modifyUserPhoto(user);
		if(ret > 0){
			result = true;
		}
	
		return result;
	}
	
	/**
	 * 根据用户ID 获取用户信息
	 */
	@RequestMapping("/user/info/{userId}")
	public User getUserById(@PathVariable("userId") final Long userId){
		
		if(userId != null){
			final User user = userService.findUserById(userId);
			return user;
		}
		return null;
	}
	
	/**
	 * 查询三方登录的用户是否存在
	 * 如果存在，则返回
	 * 如果不存在，则创建
	 */
	@RequestMapping("/user/thirdLogin/isExist")
	public boolean verificationUserExist(@RequestBody final User user,final HttpServletRequest request){
		
		if(user != null){
			final List<User> users = userService.verificationUserExistByThirdLogin(user);
			if(users.size() < 1){ // 用户不存在
				try {
					// 创建新用户
					User createUser = user;
					createUser.setUserName(URLDecoder.decode(user.getUserName(), "UTF-8"));
					
					userService.saveByThirdLogin(createUser);
					return true;
				} catch (UnsupportedEncodingException e) {
					logger.error("Decode UserName ON LOGIN PROCESS BY JUDGE THIRD LOGIN USER EXIST ... ");
					e.printStackTrace();
				}
			}else if(users.size() > 1){ // 用户存在多个
				// 返回错误信息
				logger.error("Existing More Users Problem By User Login ,telephone=" + user.getTelephone() + ",password=" + user.getPassword());
				return false;
			}else if(users.size() == 1){ // 有且仅有一个
				// 清除当前session
				sessionService.removeSession(request);
				final User u = users.get(0);
				// 存入session中
				return initSessionInfo(u, request);
				
			}
		}
		
		return false;
	}
	
	/**
	 * 根据客户名搜索客户
	 */
	@RequestMapping("/user/search/info")
	public List<User> getUserByName(@RequestBody final User user) {
		List<User> users= userService.findUserByName(user);
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
	 * 初始化 sessionInfo 信息
	 * @param user
	 * @param request
	 * @return
	 */
	public boolean initSessionInfo(final User user,HttpServletRequest request){
		// 存入session中
		final String sessionId = request.getSession().getId();
		final SessionInfo info = new SessionInfo();
		info.setLoginName(user.getUserName());
		info.setRealName(user.getRealName());
		info.setSessionType(GlobalConstant.ROLE_CUSTOMER);
		info.setSuperAdmin(false);
		info.setToken(DataUtil.md5(sessionId));
		info.setReqiureId(user.getId());
		
		
		final Role role = roleService.findRoleById(3l); // 获取用户角色
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		user.setRoles(roles);
		// 计算权限码总和
		final long maxPos = rightService.getMaxPos();
		final long[] rightSum = new long[(int) (maxPos+ 1)];
		user.setRightSum(rightSum);
		user.calculateRightSum();
		info.setSum(user.getRightSum());
		info.setEmail(user.getEmail());
		info.setPhoto(user.getImgFileName());
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put(GlobalConstant.SESSION_INFO, info);
		return sessionService.addSession(request, map);
	}
}