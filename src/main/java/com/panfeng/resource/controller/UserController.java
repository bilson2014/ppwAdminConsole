package com.panfeng.resource.controller;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import com.paipianwang.pat.facade.user.entity.Grade.Option;
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
		paramMap.put("realName", view.getRealName());
		paramMap.put("userCompany", view.getUserCompany());
		paramMap.put("clientLevel", view.getClientLevel());
		paramMap.put("beginTime", view.getBeginTime());
		paramMap.put("endTime", view.getEndTime());
		paramMap.put("telephone", view.getTelephone());
		paramMap.put("officialSite", view.getOfficialSite());
		paramMap.put("position", view.getPosition());
		paramMap.put("customerType", view.getCustomerType());
		paramMap.put("purchaseFrequency", view.getPurchaseFrequency());
		paramMap.put("purchasePrice", view.getPurchasePrice());
		paramMap.put("customerSize", view.getCustomerSize());
		paramMap.put("endorse", view.getEndorse());
		final DataGrid<PmsUser> dataGrid = pmsUserFacade.listWithPagination(pageParam, paramMap);
		return dataGrid;
	}

	@RequestMapping(value = "/user/update", method = RequestMethod.POST, produces = "application/json; chartset=UTF-8")
	public long update(final PmsUser user, HttpServletRequest request) {
		if(user.getCustomerType()==null || user.getCustomerType()!=PmsUser.TYPE_OFFLINE_DIRECT_SELLING){
			user.setReferrerId(null);
		}
		Integer computeScore = pmsUserFacade.computeScore(user);
		user.setClientLevel(computeScore);
		final long ret = pmsUserFacade.update(user);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("user update ...", sessionInfo);
		return ret;
	}

	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	public long delete(final long[] ids, HttpServletRequest request) {

		if (ids.length > 0) {

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
		if(user.getCustomerType()==null || user.getCustomerType()!=PmsUser.TYPE_OFFLINE_DIRECT_SELLING){
			user.setReferrerId(null);
		}
		user.setPassword(DataUtil.md5(PublicConfig.INIT_PASSWORD));
		user.setBirthday(DateUtils.nowDate());
		user.setUpdateTime(DateUtils.nowTime());
		Integer computeScore = pmsUserFacade.computeScore(user);
		user.setClientLevel(computeScore);
		Map<String, Object> save = pmsUserFacade.save(user);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save user...", sessionInfo);
		Object object = save.get(BaseEntity.SAVE_MAP_ROWS);
		if (object != null) {
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
		request.getSession().removeAttribute(PmsConstant.SESSION_INFO);
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
		final int count = pmsUserFacade.validationPhone(telephone, null);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 用户信息-登录名，密码
	 */
	@RequestMapping("/user/modify/loginName")
	public boolean modifyLoginName(@RequestBody final PmsUser user, HttpServletRequest request) {
		if (user != null) {
			if (user.getId() != 0)
				return pmsUserFacade.modifyUserLoginName(user) > 0 ? true : false;

		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("User is null ..", sessionInfo);
		}
		return false;
	}

	/**
	 * 根据客户名搜索客户
	 */
	@RequestMapping("/user/search/info")
	public List<PmsUser> getUserByName(@RequestBody final PmsUser user) {
		List<PmsUser> users = pmsUserFacade.findUserByName(user);
		return users != null ? users : new ArrayList<PmsUser>();
	}

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
	 * 获取全部客户
	 * 
	 * @return list
	 */
	@RequestMapping(value = "/user/droplist",method = RequestMethod.POST,
	produces = "application/json; charset=UTF-8")
	public String getDroplist() {
		List<PmsUser> list = pmsUserFacade.getDroplist();
		JsonArray jaArray = new JsonArray();
		if(ValidateUtil.isValid(list)){
			if(ValidateUtil.isValid(list)){
				for (PmsUser user : list) {
					JsonObject jo = new JsonObject();
					jo.addProperty("id", user.getId());
					jo.addProperty("userName", user.getUserName());
					jaArray.add(jo);
				}
			}
		}
		return jaArray.toString();
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

	/**
	 * 验证用户名昵称唯一性 true 可用 false 不可用
	 */
	@RequestMapping(value = "/user/unique/username", method = RequestMethod.POST)
	public boolean uniqueUserName(@RequestBody final PmsUser user, HttpServletRequest request) {
		return pmsUserFacade.uniqueUserName(user);
	}

	/**
	 * 获取评级选项
	 * @return
	 */
	@RequestMapping("/user/option")
	public BaseMsg getSelectOption() {
		Map<String, Option[]> selectOption = pmsUserFacade.getSelectOption();
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(selectOption);
		return baseMsg;
	}

}