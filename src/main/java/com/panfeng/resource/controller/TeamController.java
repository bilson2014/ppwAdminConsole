package com.panfeng.resource.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Role;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.TeamView;
import com.panfeng.service.RightService;
import com.panfeng.service.RoleService;
import com.panfeng.service.SessionInfoService;
import com.panfeng.service.TeamService;
import com.panfeng.util.DataUtil;
import com.panfeng.util.FileUtils;
import com.panfeng.util.ValidateUtil;

/**
 * 团队管理控制类
 * 
 * @author Jack
 */
@RestController
@RequestMapping("/portal")
public class TeamController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger("error");

	private static String INIT_PASSWORD;

	private static String FILE_PROFIX = null; // 文件前缀

	private static String TEAM_IMAGE_PATH = null; // 产品图片路径

	public TeamController() {
		if (INIT_PASSWORD == null || "".equals(INIT_PASSWORD)) {
			final InputStream is = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
			try {
				Properties propertis = new Properties();
				propertis.load(is);
				INIT_PASSWORD = propertis.getProperty("initPassw0rd");
				FILE_PROFIX = propertis.getProperty("file.prefix");
				TEAM_IMAGE_PATH = propertis.getProperty("upload.server.team.image");
			} catch (IOException e) {
				logger.error("load Properties fail ...");
				e.printStackTrace();
			}
		}
	}

	@Autowired
	private final TeamService service = null;

	@Autowired
	private final RoleService roleService = null;

	@Autowired
	private final RightService rightService = null;

	@Autowired
	private final SessionInfoService sessionService = null;

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
	public DataGrid<Team> listWithPagination(final TeamView view, PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);

		DataGrid<Team> dataGrid = new DataGrid<Team>();
		final List<Team> list = service.listWithPagination(view);
		final long total = service.maxSize(view);
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}

	@RequestMapping(value = "/team/save", method = RequestMethod.POST)
	public void save(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("file") final MultipartFile file, final Team team) {
		response.setContentType("text/html;charset=UTF-8");
		// 先保存获取ID，然后更新
		team.setPassword(DataUtil.md5(INIT_PASSWORD));
		final long id = service.save(team);

		// 团队logo全路径
		final String imagePath = FILE_PROFIX + TEAM_IMAGE_PATH;

		// save file
		File imageDir = new File(imagePath);
		if (!imageDir.exists())
			imageDir.mkdir();
		try {
			StringBuffer fileName = new StringBuffer();
			if (!file.isEmpty()) {
				final String extName = FileUtils.getExtName(file.getOriginalFilename(), ".");
				fileName.append("team" + id);
				fileName.append("-");
				final Calendar calendar = new GregorianCalendar();
				fileName.append(calendar.get(Calendar.YEAR));
				fileName.append((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1)
						: (calendar.get(Calendar.MONTH) + 1));
				fileName.append(calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH)
						: calendar.get(Calendar.DAY_OF_MONTH));
				fileName.append(calendar.get(Calendar.HOUR_OF_DAY));
				fileName.append(calendar.get(Calendar.MINUTE));
				fileName.append(calendar.get(Calendar.SECOND));
				fileName.append(calendar.get(Calendar.MILLISECOND));
				fileName.append(".");
				fileName.append(extName);
				// get file path
				final String path = TEAM_IMAGE_PATH + "/" + fileName.toString();
				File imageFile = new File(FILE_PROFIX + path);
				file.transferTo(imageFile);

				team.setTeamPhotoUrl(path);
				// save photo path
				service.saveTeamPhotoUrl(team);
			}
		} catch (IOException e) {
			logger.error("TeamController method:save() upload team logo error ...");
			e.printStackTrace();
			throw new RuntimeException("Team Image upload error ...", e);
		}
	}

	@RequestMapping(value = "/team/update", method = RequestMethod.POST)
	public void update(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("file") final MultipartFile file, final Team team) throws Exception {

		response.setContentType("text/html;charset=UTF-8");

		// 如果上传文件不为空时，更新 url;反之亦然
		if (!file.isEmpty()) {
			// 团队logo全路径
			final String imagePath = FILE_PROFIX + TEAM_IMAGE_PATH;

			// save file
			File imageDir = new File(imagePath);
			if (!imageDir.exists())
				imageDir.mkdir();
			StringBuffer fileName = new StringBuffer();
			final String extName = FileUtils.getExtName(file.getOriginalFilename(), ".");
			fileName.append("team" + team.getTeamId());
			fileName.append("-");
			final Calendar calendar = new GregorianCalendar();
			fileName.append(calendar.get(Calendar.YEAR));
			fileName.append((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1)
					: (calendar.get(Calendar.MONTH) + 1));
			fileName.append(calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH)
					: calendar.get(Calendar.DAY_OF_MONTH));
			fileName.append(calendar.get(Calendar.HOUR_OF_DAY));
			fileName.append(calendar.get(Calendar.MINUTE));
			fileName.append(calendar.get(Calendar.SECOND));
			fileName.append(calendar.get(Calendar.MILLISECOND));
			fileName.append(".");
			fileName.append(extName);
			// get file path
			final String path = TEAM_IMAGE_PATH + "/" + fileName;
			File imageFile = new File(FILE_PROFIX + path);
			file.transferTo(imageFile);

			// 删除 原文件
			final Team originalTeam = service.findTeamById(team.getTeamId());
			if (originalTeam != null) {
				final String originalPath = originalTeam.getTeamPhotoUrl();
				FileUtils.deleteFile(FILE_PROFIX + originalPath);
			}
			team.setTeamPhotoUrl(path);
			// save photo path
			service.saveTeamPhotoUrl(team);
		}

		service.update(team);
	}

	@RequestMapping(value = "/team/delete", method = RequestMethod.POST)
	public long delete(final long[] ids) {

		if (ids.length > 0) {
			final List<Team> list = service.delete(ids);
			// delete file in disk
			if (!list.isEmpty() && list.size() > 0) {
				for (final Team team : list) {
					if (team.getTeamPhotoUrl() != null && !"".equals(team.getTeamPhotoUrl())) {
						final String imagePath = FILE_PROFIX + team.getTeamPhotoUrl();
						File file = new File(imagePath);
						if (file.isFile() && file.exists()) {
							file.delete();
						}
					}
				}
			}
			return 0l;
		} else {
			logger.error("Team ids is null");
			new RuntimeException("Team ids is null");
		}
		return 0l;
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
	public Team loadData(@PathVariable("teamId") final Long teamId) {

		final Team team = service.findTeamById(teamId);
		team.setPassword(null);
		return team;
	}

	/**
	 * 更新团队图片路径
	 * 
	 * @param team
	 *            包含(团队ID 和 团队头像路径)
	 * @return 是否更新成功
	 */
	@RequestMapping("/team/static/data/updateTeamPhotoPath")
	public boolean updateTeamPhotoPath(@RequestBody final Team team) {

		final long ret = service.saveTeamPhotoUrl(team);
		if (ret > 0)
			return true;
		else
			return false;
	}

	/**
	 * 更新供应商基础信息
	 * 
	 * @param team
	 *            包含(供应商名称、简介、地址、邮箱等)
	 * @return 结果
	 */
	@RequestMapping("/team/static/data/updateTeamInformation")
	public boolean updateTeamInformation(@RequestBody final Team team) {
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

				final long ret = service.updateTeamInfomation(team);
				if (ret == 1) {
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				logger.error("Provider Infomation Decode error On Provider updateTeamInformation ...");
				e.printStackTrace();
			}
		}
		return false;

	}

	/*
	 * @RequestMapping("/team/static/data/doLogin") public boolean
	 * doLogin(@RequestBody final Team original,final HttpServletRequest
	 * request){
	 * 
	 * try { // 转码 final String loginName =
	 * URLDecoder.decode(original.getLoginName(), "UTF-8"); final String
	 * password = URLDecoder.decode(original.getPassword(), "UTF-8");
	 * original.setLoginName(loginName); original.setPassword(password); final
	 * Team team = service.doLogin(original);
	 * 
	 * if(team != null){ // 存入session return initSessionInfo(team, request);
	 * 
	 * } } catch (UnsupportedEncodingException e) {
	 * 
	 * logger.error("Decoder LoginName Or Password Error On Provider Login ..."
	 * ); e.printStackTrace(); } return false; }
	 */

	/**
	 * 供应商登录
	 * 
	 * @param team
	 *            供应商登录名和密码(已加密)
	 * @return 供应商信息
	 */
	@RequestMapping("/team/static/data/doLogin")
	public boolean doLogin(@RequestBody final Team original, final HttpServletRequest request) {
		Team team = null;
		if(null != original && null != original.getPhoneNumber() && !"".equals(original.getPhoneNumber())){
			team = service.doLogin(original.getPhoneNumber());
		}else{
			team = service.findTeamByLoginNameAndPwd(original);
		}
		if (team != null) {
			// 存入session
			return initSessionInfo(team, request);
		}
		return false;
	}

	@RequestMapping("/team/static/data/checkPwd")
	public boolean checkPwd(@RequestBody final Team original, final HttpServletRequest request) {

		// try {
		// // 转码
		// final String loginName = URLDecoder.decode(original.getLoginName(),
		// "UTF-8");
		// final String password = URLDecoder.decode(original.getPassword(),
		// "UTF-8");
		// original.setLoginName(loginName);
		// original.setPassword(password);
		// final Team team = service.doLogin(original);
		//
		// if (team != null) {
		// // 存入session
		// return true;
		//
		// }
		// } catch (UnsupportedEncodingException e) {
		//
		// logger.error("Decoder LoginName Or Password Error On Provider Login
		// ...");
		// e.printStackTrace();
		// }
		return false;
	}

	/**
	 * 检查 注册电话 的唯一性 或者 用户名的唯一性
	 * 
	 * @param team
	 *            包含用户名或手机号码
	 * @return 如果唯一则返回 true ；反之则返回 false
	 */
	@RequestMapping("/team/static/checkIsExist")
	public boolean checkExist(@RequestBody final Team team) {

		try {
			if (team.getLoginName() != null && !"".equals(team.getLoginName())) {
				// 转码
				final String loginName = URLDecoder.decode(team.getLoginName(), "UTF-8");
				team.setLoginName(loginName);
			}

			final long count = service.checkExist(team);
			if (count == 0)
				return true;

		} catch (UnsupportedEncodingException e) {

			logger.error("Decoder LoginName Error On Provider CheckIsExist ...");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * @RequestMapping("/team/static/register") public boolean
	 * register(@RequestBody final Team original,final HttpServletRequest
	 * request){
	 * 
	 * try { if(original != null){ // 转码 final String loginName =
	 * URLDecoder.decode(original.getLoginName(), "UTF-8"); final String
	 * password = URLDecoder.decode(original.getPassword(), "UTF-8");
	 * original.setLoginName(loginName); original.setPassword(password);
	 * 
	 * final Team team = service.register(original); return
	 * initSessionInfo(team, request); } } catch (UnsupportedEncodingException
	 * e) {
	 * 
	 * logger.error(
	 * "Decoder LoginName Or Password Error On Provider Register ...");
	 * e.printStackTrace(); } return false; }
	 */

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
	public boolean recoverPassword(@RequestBody final Team team) {

		try {
			// 转码
			final String password = URLDecoder.decode(team.getPassword(), "UTF-8");
			team.setPassword(password);
			final long ret = service.recover(team);
			if (ret == 1) {
				return true;
			} else {
				logger.error("Recover Provider Password error ...,LoginName=" + team.getLoginName());
			}
		} catch (UnsupportedEncodingException e) {

			logger.error("Decoder Password Error On Provider RecoverPassword ...");
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping("/team/static/updatePasswordByLoginName")
	public boolean recoverPasswordByLoginName(@RequestBody final Team team) {
		if (team != null && team.getLoginName() != null && !"".equals(team.getLoginName()) && team.getPassword() != null
				&& !"".equals(team.getPassword())) {
			// 转码
			try {
				final String loginName = URLDecoder.decode(team.getLoginName(), "UTF-8");
				final String password = URLDecoder.decode(team.getPassword(), "UTF-8");
				team.setLoginName(loginName);
				team.setPassword(password);
				final long ret = service.updatePasswordByLoginName(team);
				if (ret > 0)
					return true;
				else {
					logger.info(
							"Update Password By LoginName On Provider recoverPasswordByLoginName ,result=0 infected ...");
					return false;
				}
			} catch (UnsupportedEncodingException e) {
				logger.error("Decoder Password And LoginName Error On Provider recoverPasswordByLoginName ...");
				e.printStackTrace();
			}
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
	public boolean updateStatus(@RequestBody final Team team) {

		if (team != null) {
			final Long id = team.getTeamId();
			if (id != null && !"".equals(id)) {
				final long ret = service.updateTeamStatus(id);
				if (ret == 1)
					return true;
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
					sessionService.removeSession(request);
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
		if (baseMsg.getErrorCode().equals(BaseMsg.NORMAL) || baseMsg.getErrorCode().equals(BaseMsg.WARNING)) {
			boolean login = initSessionInfo((Team) baseMsg.getResult(), request);
			if (!login) {
				return new BaseMsg(BaseMsg.ERROR, "绑定成功，登录失败", baseMsg.getResult());
			}
		}
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
		sessionService.removeSession(request);
		// 存入session中
		final String sessionId = request.getSession().getId();
		final SessionInfo info = new SessionInfo();
		info.setLoginName(team.getLoginName());
		info.setRealName(team.getTeamName());
		info.setSessionType(GlobalConstant.ROLE_PROVIDER);
		info.setSuperAdmin(false);
		info.setToken(DataUtil.md5(sessionId));
		info.setReqiureId(team.getTeamId());
		info.setPhoto(team.getTeamPhotoUrl());

		final Role role = roleService.findRoleById(2l); // 获取用户角色
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		team.setRoles(roles);
		// 计算权限码总和
		final long maxPos = rightService.getMaxPos();
		final long[] rightSum = new long[(int) (maxPos + 1)];
		team.setRightSum(rightSum);
		team.calculateRightSum();
		info.setSum(team.getRightSum());
		info.setEmail(team.getEmail());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GlobalConstant.SESSION_INFO, info);
		return sessionService.addSession(request, map);
	}
}
