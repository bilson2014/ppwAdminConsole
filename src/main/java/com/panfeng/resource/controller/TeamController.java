package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.service.PmsRightFacade;
import com.paipianwang.pat.facade.right.service.PmsRoleFacade;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.mq.service.SmsMQService;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.view.TeamView;
import com.panfeng.service.TeamService;
import com.panfeng.util.DataUtil;
import com.panfeng.util.Log;

/**
 * 团队管理控制类
 * 
 * @author Jack
 */
@RestController
@RequestMapping("/portal")
public class TeamController extends BaseController {

	@Autowired
	private final TeamService service = null;

	@Autowired
	private final PmsRoleFacade pmsRoleFacade = null;

	@Autowired
	private final PmsRightFacade pmsRightFacade = null;

	@Autowired
	private final SmsMQService smsMQService = null;

	@Autowired
	private final PmsTeamFacade pmsTeamFacade = null;

	@Autowired
	private final TeamService teamService = null;

	@RequestMapping("team-list")
	public ModelAndView view(final HttpServletRequest request, final ModelMap model) {

		model.addAttribute("param", "team-list");
		return new ModelAndView("team-list", model);
	}

	/**
	 * 分页检索 team
	 * 
	 * @param view
	 *            team-条件视图
	 */
	@RequestMapping(value = "/team/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsTeam> listWithPagination(final TeamView view, PageParam pageParam) {
		// 封装分页参数
		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);
		// 封装查询参数
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("teamId", view.getTeamId());
		paramMap.put("flag", view.getFlag());
		paramMap.put("phoneNumber", view.getPhoneNumber());
		paramMap.put("loginName", view.getLoginName());
		paramMap.put("priceRange", view.getPriceRange());
		paramMap.put("business", view.getBusiness());
		paramMap.put("teamName", view.getTeamName());
		paramMap.put("recommend", view.isRecommend());
		paramMap.put("linkman", view.getLinkman());
		paramMap.put("cityID", view.getCityID());
		paramMap.put("provinceID", view.getProvinceID());
		final DataGrid<PmsTeam> dataGrid = pmsTeamFacade.listWithPagination(pageParam, paramMap);
		return dataGrid;
	}

	@RequestMapping(value = "/team/save", method = RequestMethod.POST)
	public BaseMsg save(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("file") final MultipartFile file, final PmsTeam team) {
		BaseMsg baseMsg = new BaseMsg();
		response.setContentType("text/html;charset=UTF-8");
		// 先保存获取ID，然后更新
		team.setPassword(DataUtil.md5(PublicConfig.INIT_PASSWORD));
		// service.save(team);
		long teamId = pmsTeamFacade.save(team);
		team.setTeamId(teamId);
		try {
			if (!file.isEmpty()) {
				String path = FastDFSClient.uploadFile(file);
				team.setTeamPhotoUrl(path);
			}
			// service.saveTeamPhotoUrl(team);
			pmsTeamFacade.saveTeamPhotoUrl(team);
		} catch (Exception e) {
			baseMsg.setErrorCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("更新logo失败！");
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("TeamController method:save() upload team logo error ...", sessionInfo);
			e.printStackTrace();
			throw new RuntimeException("Team Image upload error ...", e);
		}
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save team ...", sessionInfo);
		baseMsg.setErrorCode(BaseMsg.NORMAL);
		baseMsg.setErrorMsg("保存成功！");
		return baseMsg;
	}

	@RequestMapping(value = "/team/update", method = RequestMethod.POST)
	public BaseMsg update(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("file") final MultipartFile file, final PmsTeam team) throws Exception {
		BaseMsg baseMsg = new BaseMsg();
		response.setContentType("text/html;charset=UTF-8");

		// 如果上传文件不为空时，更新 url;反之亦然
		if (!file.isEmpty()) {
			String path = FastDFSClient.uploadFile(file);
			// 删除 原文件
			// final Team originalTeam = service.findTeamById(team.getTeamId());
			final PmsTeam originalTeam = pmsTeamFacade.findTeamById(team.getTeamId());
			if (originalTeam != null) {
				final String originalPath = originalTeam.getTeamPhotoUrl();
				FastDFSClient.deleteFile(originalPath);
			}
			team.setTeamPhotoUrl(path);
			// save photo path
			// service.saveTeamPhotoUrl(team);
			pmsTeamFacade.saveTeamPhotoUrl(team);
		}

		// long ret = service.update(team);
		long ret = pmsTeamFacade.update(team);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update team ...", sessionInfo);
		if (ret > 0) {
			baseMsg.setErrorCode(BaseMsg.NORMAL);
			baseMsg.setErrorMsg("更新成功！");
		} else {
			baseMsg.setErrorCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("更新失败！");
		}
		return baseMsg;
	}

	@RequestMapping(value = "/team/delete", method = RequestMethod.POST)
	public long delete(final long[] ids, HttpServletRequest request) {

		if (ids.length > 0) {
			// final List<Team> list = service.delete(ids);
			final List<PmsTeam> list = pmsTeamFacade.delete(ids);
			// delete file in disk
			if (!list.isEmpty() && list.size() > 0) {
				for (final PmsTeam team : list) {
					if (team.getTeamPhotoUrl() != null && !"".equals(team.getTeamPhotoUrl())) {
						FastDFSClient.deleteFile(team.getTeamPhotoUrl());
					}
				}
			}
			return 0l;
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("Team ids is null ...", sessionInfo);
			new RuntimeException("Team ids is null");
		}
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete teams ... ids" + ids.toString(), sessionInfo);
		return 0l;
	}
	
	/**
	 * 供应商数据导出
	 * 
	 * @param view
	 * @param response
	 */
	@RequestMapping(value = "/team/export", method = RequestMethod.POST)
	public void export(final TeamView view, final HttpServletResponse response) {
		OutputStream outputStream = null;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			String dateString = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
			String filename = URLEncoder.encode("供应商列表" + dateString + ".xlsx", "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"\r\n");
			outputStream = response.getOutputStream();

			// 封装查询参数
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("teamId", view.getTeamId());
			paramMap.put("flag", view.getFlag());
			paramMap.put("phoneNumber", view.getPhoneNumber());
			paramMap.put("priceRange", view.getPriceRange());
			paramMap.put("business", view.getBusiness());
			paramMap.put("teamName", view.getTeamName());
			paramMap.put("recommend", view.isRecommend());
			paramMap.put("linkman", view.getLinkman());
			paramMap.put("cityID", view.getCityID());
			paramMap.put("provinceID", view.getProvinceID());

			List<PmsTeam> teamList = pmsTeamFacade.listWithParam(paramMap);

			// 报表导出
			teamService.generateReport(teamList, outputStream);

			outputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// --------------------------------以下是前端展示内容 ----------------------------

	/**
	 * 根据 团队ID 获取团队信息
	 * 
	 * @param teamId
	 *            团队唯一编号
	 * @return 团队信息
	 */
	@RequestMapping("/team/static/data/{teamId}")
	public PmsTeam loadData(@PathVariable("teamId") final Long teamId) {
		final PmsTeam team = pmsTeamFacade.findTeamById(teamId);
		team.setPassword(null);
		return team;
	}

	/**
	 * 更新供应商基础信息
	 * 
	 * @param team
	 *            包含(供应商名称、简介、地址、邮箱等)
	 * @return 结果
	 */
	@RequestMapping("/team/static/data/updateTeamInformation")
	public boolean updateTeamInformation(@RequestBody final PmsTeam team, HttpServletRequest request) {
		if (team != null) {
			try {
				// 解码
				final String teamName = team.getTeamName();
				final String teamDesc = team.getTeamDescription();
				final String address = team.getAddress();
				final String email = team.getEmail();
				final String linkman = team.getLinkman();
				final String webchat = team.getWebchat();
				final String officialSite = team.getOfficialSite();
				final String scale = team.getScale();
				final String businessDesc = team.getBusinessDesc();
				final String demand = team.getDemand();
				final String description = team.getDescription();

				if (teamName != null && !"".equals(teamName)) {
					team.setTeamName(URLDecoder.decode(teamName, "UTF-8"));
				}

				if (teamDesc != null && !"".equals(teamDesc)) {
					team.setTeamDescription(URLDecoder.decode(teamDesc, "UTF-8"));
				}

				if (address != null && !"".equals(address)) {
					team.setAddress(URLDecoder.decode(address, "UTF-8"));
				}

				if (email != null && !"".equals(email)) {
					team.setEmail(URLDecoder.decode(email, "UTF-8"));
				}

				if (linkman != null && !"".equals(linkman)) {
					team.setLinkman(URLDecoder.decode(linkman, "UTF-8"));
				}

				if (webchat != null && !"".equals(webchat)) {
					team.setWebchat(URLDecoder.decode(webchat, "UTF-8"));
				}

				if (officialSite != null && !"".equals(officialSite)) {
					team.setOfficialSite(URLDecoder.decode(officialSite, "UTF-8"));
				}

				if (scale != null && !"".equals(scale)) {
					team.setScale(URLDecoder.decode(scale, "UTF-8"));
				}

				if (businessDesc != null && !"".equals(businessDesc)) {
					team.setBusinessDesc(URLDecoder.decode(businessDesc, "UTF-8"));
				}

				if (demand != null && !"".equals(demand)) {
					team.setDemand(URLDecoder.decode(demand, "UTF-8"));
				}

				if (description != null && !"".equals(description)) {
					team.setDescription(URLDecoder.decode(description, "UTF-8"));
				}
				// 将状态置为审核中
				if (team.getFlag() == 2)
					team.setFlag(0);

				// final long ret = service.updateTeamInfomation(team);
				final long ret = pmsTeamFacade.updateTeamInfomation(team);
				SessionInfo sessionInfo = getCurrentInfo(request);
				Log.error("update team ...", sessionInfo);
				if (ret == 1) {
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				SessionInfo sessionInfo = getCurrentInfo(request);
				Log.error("Provider Infomation Decode error On Provider updateTeamInformation ...", sessionInfo);
				e.printStackTrace();
			}
		}

		return false;

	}

	/**
	 * 注册供应商
	 * 
	 * @param team
	 *            包含(供应商名称、简介、地址、邮箱等)
	 * @return 结果
	 */
	@RequestMapping("/team/static/data/registerteam")
	public boolean registerTeam(@RequestBody final PmsTeam team, HttpServletRequest request) {
		if (team != null) {
			try {
				// 解码
				final String teamName = team.getTeamName();
				final String teamDesc = team.getTeamDescription();
				final String address = team.getAddress();
				final String email = team.getEmail();
				final String linkman = team.getLinkman();
				final String webchat = team.getWebchat();
				final String officialSite = team.getOfficialSite();
				final String scale = team.getScale();
				final String businessDesc = team.getBusinessDesc();
				final String demand = team.getDemand();
				final String description = team.getDescription();

				if (teamName != null && !"".equals(teamName)) {
					team.setTeamName(URLDecoder.decode(teamName, "UTF-8"));
				}

				if (teamDesc != null && !"".equals(teamDesc)) {
					team.setTeamDescription(URLDecoder.decode(teamDesc, "UTF-8"));
				}

				if (address != null && !"".equals(address)) {
					team.setAddress(URLDecoder.decode(address, "UTF-8"));
				}

				if (email != null && !"".equals(email)) {
					team.setEmail(URLDecoder.decode(email, "UTF-8"));
				}

				if (linkman != null && !"".equals(linkman)) {
					team.setLinkman(URLDecoder.decode(linkman, "UTF-8"));
				}

				if (webchat != null && !"".equals(webchat)) {
					team.setWebchat(URLDecoder.decode(webchat, "UTF-8"));
				}

				if (officialSite != null && !"".equals(officialSite)) {
					team.setOfficialSite(URLDecoder.decode(officialSite, "UTF-8"));
				}

				if (scale != null && !"".equals(scale)) {
					team.setScale(URLDecoder.decode(scale, "UTF-8"));
				}

				if (businessDesc != null && !"".equals(businessDesc)) {
					team.setBusinessDesc(URLDecoder.decode(businessDesc, "UTF-8"));
				}

				if (demand != null && !"".equals(demand)) {
					team.setDemand(URLDecoder.decode(demand, "UTF-8"));
				}

				if (description != null && !"".equals(description)) {
					team.setDescription(URLDecoder.decode(description, "UTF-8"));
				}

				// ->modify to dubbo 2017-2-4 11:33:58 begin
				// Team dbteam = service.register(team);
				PmsTeam dbteam = pmsTeamFacade.register(team);
				// ->modify to dubbo 2017-2-4 11:33:58 end
				SessionInfo sessionInfo = getCurrentInfo(request);
				Log.error("save team ...", sessionInfo);
				if (dbteam != null && dbteam.getTeamId() > 0) {
					// add by wlc 2016-11-11 11:19:36
					// 供应商注册短信，发送短信 begin
					smsMQService.sendMessage("132269", team.getPhoneNumber(), null);
					Gson gson = new Gson();
					String json = gson.toJson(dbteam);
					return initSessionInfo(gson.fromJson(json, Team.class), request);
				}
			} catch (UnsupportedEncodingException e) {
				SessionInfo sessionInfo = getCurrentInfo(request);
				Log.error("Provider Infomation Decode error On Provider updateTeamInformation ...", sessionInfo);
				e.printStackTrace();
			}
		}
		return false;

	}

	@RequestMapping("/team/static/data/registerteamRetId")
	public Team registerTeamRetId(@RequestBody final Team team, HttpServletRequest request) {
		if (team != null) {
			try {
				// 解码
				final String teamName = team.getTeamName();
				final String teamDesc = team.getTeamDescription();
				final String address = team.getAddress();
				final String email = team.getEmail();
				final String linkman = team.getLinkman();
				final String webchat = team.getWebchat();
				final String officialSite = team.getOfficialSite();
				final String scale = team.getScale();
				final String businessDesc = team.getBusinessDesc();
				final String demand = team.getDemand();
				final String description = team.getDescription();

				if (teamName != null && !"".equals(teamName)) {
					team.setTeamName(URLDecoder.decode(teamName, "UTF-8"));
				}

				if (teamDesc != null && !"".equals(teamDesc)) {
					team.setTeamDescription(URLDecoder.decode(teamDesc, "UTF-8"));
				}

				if (address != null && !"".equals(address)) {
					team.setAddress(URLDecoder.decode(address, "UTF-8"));
				}

				if (email != null && !"".equals(email)) {
					team.setEmail(URLDecoder.decode(email, "UTF-8"));
				}

				if (linkman != null && !"".equals(linkman)) {
					team.setLinkman(URLDecoder.decode(linkman, "UTF-8"));
				}

				if (webchat != null && !"".equals(webchat)) {
					team.setWebchat(URLDecoder.decode(webchat, "UTF-8"));
				}

				if (officialSite != null && !"".equals(officialSite)) {
					team.setOfficialSite(URLDecoder.decode(officialSite, "UTF-8"));
				}

				if (scale != null && !"".equals(scale)) {
					team.setScale(URLDecoder.decode(scale, "UTF-8"));
				}

				if (businessDesc != null && !"".equals(businessDesc)) {
					team.setBusinessDesc(URLDecoder.decode(businessDesc, "UTF-8"));
				}

				if (demand != null && !"".equals(demand)) {
					team.setDemand(URLDecoder.decode(demand, "UTF-8"));
				}

				if (description != null && !"".equals(description)) {
					team.setDescription(URLDecoder.decode(description, "UTF-8"));
				}

				Team dbteam = service.register(team);
				SessionInfo sessionInfo = getCurrentInfo(request);
				Log.error("save team ...", sessionInfo);
				if (dbteam != null && dbteam.getTeamId() > 0) {
					initSessionInfo(dbteam, request);
				}
				return dbteam;
			} catch (UnsupportedEncodingException e) {
				SessionInfo sessionInfo = getCurrentInfo(request);
				Log.error("Provider Infomation Decode error On Provider updateTeamInformation ...", sessionInfo);
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * 供应商登录
	 * 
	 * @param team
	 *            供应商登录名和密码(已加密)
	 * @return 供应商信息
	 */
	@RequestMapping("/team/static/data/doLogin")
	public boolean doLogin(@RequestBody final PmsTeam original, final HttpServletRequest request) {
		PmsTeam team = null;
		if (null != original && null != original.getPhoneNumber() && !"".equals(original.getPhoneNumber())) {
			// team = service.doLogin(original.getPhoneNumber());
			team = pmsTeamFacade.doLogin(original.getPhoneNumber());
		} else {
			// team = service.findTeamByLoginNameAndPwd(original);
			team = pmsTeamFacade.findTeamByLoginNameAndPwd(original);
		}
		if (team != null) {
			// 存入session
			Gson gson = new Gson();
			String json = gson.toJson(team);
			return initSessionInfo(gson.fromJson(json, Team.class), request);
		}
		return false;
	}

	/**
	 * 供应商 注册
	 * 
	 * @param original
	 *            包含(手机号、用户名、密码(已加密))
	 * @return 保存之后的 team
	 */
	@RequestMapping("/team/static/register")
	public boolean register(@RequestBody final Team original, final HttpServletRequest request) {
		if (original != null) {
			final Team team = service.register(original);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("save team ...", sessionInfo);
			return initSessionInfo(team, request);
		}
		return false;
	}

	/**
	 * 供应商密码重置
	 * 
	 * @param provider
	 *            供应商信息(包含 手机号 和 密码)
	 * @return 成功返回 true; 失败返回false
	 */
	@RequestMapping("/team/static/recoverPassword")
	public boolean recoverPassword(@RequestBody final Team team, HttpServletRequest request) {

		try {
			// 转码
			final String password = URLDecoder.decode(team.getPassword(), "UTF-8");
			team.setPassword(password);
			final long ret = service.recover(team);
			if (ret == 1) {
				return true;
			} else {
				SessionInfo sessionInfo = getCurrentInfo(request);
				Log.error("Recover Provider Password error ...,LoginName=" + team.getLoginName(), sessionInfo);
			}
		} catch (UnsupportedEncodingException e) {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("Decoder Password Error On Provider RecoverPassword ...", sessionInfo);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 更新供应商审核状态为 审核中
	 * 
	 * @param team
	 *            包含供应商唯一编号
	 */
	@RequestMapping("/team/static/data/updateStatus")
	public boolean updateStatus(@RequestBody final Team team, HttpServletRequest request) {

		if (team != null) {
			final Long id = team.getTeamId();
			if (id != null && !"".equals(id)) {
				final long ret = service.updateTeamStatus(id);
				if (ret == 1) {
					SessionInfo sessionInfo = getCurrentInfo(request);
					Log.error("update team ...", sessionInfo);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 通过供应商公司名或者联系人搜索供应商
	 * 
	 * @param team
	 * @return
	 */
	@RequestMapping("/team/search/info")
	public List<Team> getTeamByName(@RequestBody final Team team) {
		List<Team> teams = service.findTeamByName(team);
		return teams != null ? teams : new ArrayList<Team>();
	}

	@RequestMapping("/team/thirdLogin/isExist")
	public boolean verificationTeamExist(@RequestBody final Team provider, final HttpServletRequest request) {

		final List<Team> list = service.verificationTeamExist(provider);
		if (ValidateUtil.isValid(list)) {
			if (list.size() == 1) {
				if (ValidateUtil.isValid(list.get(0).getPhoneNumber())) {
					// 绑定账户
					// 清除当前session
					// sessionService.removeSession(request);
					request.getSession().removeAttribute(PmsConstant.SESSION_INFO);
					final Team team = list.get(0);
					// 存入session中
					return initSessionInfo(team, request);
				}
			}
		}
		return false;
	}

	@RequestMapping("/team/thirdLogin/bind")
	public BaseMsg bind(@RequestBody final Team provider, final HttpServletRequest request) {
		final BaseMsg baseMsg = service.bind(provider);
		return baseMsg;
	}

	/**
	 * 初始化 sessionInfo 信息
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	public boolean initSessionInfo(final Team team, HttpServletRequest request) {

		// 清空session
		// sessionService.removeSession(request);
		final HttpSession session = request.getSession();
		session.removeAttribute(PmsConstant.SESSION_INFO);
		// 存入session中
		final String sessionId = request.getSession().getId();
		final SessionInfo info = new SessionInfo();
		info.setLoginName(team.getLoginName());
		info.setRealName(team.getTeamName());
		info.setSessionType(PmsConstant.ROLE_PROVIDER);
		// info.setSuperAdmin(false);
		info.setToken(DataUtil.md5(sessionId));
		info.setReqiureId(team.getTeamId());
		info.setPhoto(team.getTeamPhotoUrl());
		if (team.getFlag() == 1)
			info.setIsIdentification(true);

		final PmsRole role = pmsRoleFacade.findRoleById(2l); // 获取用户角色
		final List<PmsRole> roles = new ArrayList<PmsRole>();
		roles.add(role);
		team.setRoles(roles);
		// 计算权限码总和
		final long maxPos = pmsRightFacade.getMaxPos();
		final long[] rightSum = new long[(int) (maxPos + 1)];
		team.setRightSum(rightSum);
		team.calculateRightSum();
		info.setSum(team.getRightSum());
		info.setEmail(team.getEmail());
		info.setSuperAdmin(team.isSuperAdmin()); // 判断是否是超级管理员
		session.setAttribute(PmsConstant.SESSION_INFO, info);
		return true;
	}

	/**
	 * 查询第三方绑定状态
	 */
	@RequestMapping("/team/third/status")
	public Map<String, Object> thirdStatus(@RequestBody final Team team, HttpServletRequest request) {
		Map<String, Object> map = service.thirdStatus(team);
		return map;
	}

	/**
	 * 用户资料页面绑定第三方
	 */
	@RequestMapping("/team/info/bind")
	public boolean userInfoBind(@RequestBody final Team team, HttpServletRequest request) {
		return service.teamInfoBind(team);
	}

	/**
	 * 用户资料页面解除绑定第三方
	 */
	@RequestMapping("/team/info/unbind")
	public boolean userInfoUnBind(@RequestBody final PmsTeam team, HttpServletRequest request) {
		// return service.teamInfoUnBind(team);
		return pmsTeamFacade.teamInfoUnBind(team);
	}

	@RequestMapping("/team/tags")
	public List<String> getTags(@RequestBody List<Integer> ids, HttpServletRequest request) {
		if (ValidateUtil.isValid(ids)) {
			List<String> tags = pmsTeamFacade.getTags(ids);
			return tags;
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("ids is null ...", sessionInfo);
		}
		return null;
	}

	@RequestMapping("/team/update/newphone")
	public BaseMsg updateNewphone(@RequestBody PmsTeam team, HttpServletRequest request) {

		// final long count = service.checkExist(team);
		final long count = pmsTeamFacade.checkExist(team);
		if (count > 0) {
			return new BaseMsg(0, "手机号码已被占用");
		}
		// final long ret = service.modifyTeamPhone(team);
		final long ret = pmsTeamFacade.modifyTeamPhone(team);
		if (ret > 0) {
			return new BaseMsg(1, "success");
		}
		return new BaseMsg(0, "error");
	}

	/**
	 * 处理team临时表,更新team备注
	 */
	@RequestMapping("/team/deal/teamTmpAndTeamDesc")
	public boolean dealTeamTmpAndUpdateTeamDesc(@RequestBody PmsTeam team, HttpServletRequest request) {
		try {
			if (null != team) {
				String description = null == team.getDescription() ? "" : team.getDescription();
				team.setDescription(description);
				// 更新备注信息
				// service.updateTeamDescription(team);
				// service.dealTeamTmp(team);
				pmsTeamFacade.updateTeamDescription(team);
				pmsTeamFacade.dealTeamTmp(team);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 根据 团队ID 获取团队信息
	 * 
	 * @param teamId
	 *            团队唯一编号
	 * @return 团队信息
	 */
	@RequestMapping("/team/static/latest/{teamId}")
	public PmsTeam loadLatestData(@PathVariable("teamId") final Long teamId) {
		// final Team team = service.findLatestTeamById(teamId);
		final PmsTeam team = pmsTeamFacade.findLatestTeamById(teamId);
		team.setPassword(null);
		return team;
	}

	/**
	 * 首页供应商推荐排序或者删除 action 排序动作 up down del index 当前排序
	 */
	@RequestMapping("/team/recommend/sort")
	public boolean sortRecommendTeam(final String action, final String teamId) {
		boolean flag = false;
		long id = Long.valueOf(teamId);
		switch (action) {
		case "up":
			// flag = service.moveUp(id);
			flag = pmsTeamFacade.moveUp(id);
			break;
		case "down":
			// flag = service.moveDown(id);
			flag = pmsTeamFacade.moveDown(id);
			break;
		case "del":
			// flag = service.delRecommend(id);
			flag = pmsTeamFacade.delRecommend(id);
			break;
		}
		return flag;
	}

	/**
	 * 获取所有没有被推荐到首页的供应商
	 */
	@RequestMapping(value = "/team/all/norecommend", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<PmsTeam> getAllTeamNoRecommend() {
		// final List<PmsTeam> list = service.getAllNoRecommend();
		final List<PmsTeam> list = pmsTeamFacade.getAllNoRecommend();
		return list;
	}

	/**
	 * 添加一个供应商到首页
	 */
	@RequestMapping(value = "/team/addrecommend")
	public boolean addRecommend(long teamId) {
		// return service.addRecommend(teamId);
		return pmsTeamFacade.addRecommend(teamId);
	}

	/**
	 * 获取首页供应商推荐
	 */
	@RequestMapping(value = "/team/recommend", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<PmsTeam> teamRecommendList() {
		// return service.teamRecommendList();
		return pmsTeamFacade.teamRecommendList();
	}


}
