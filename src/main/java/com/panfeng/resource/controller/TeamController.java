package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.product.entity.ChanPinLineTree;
import com.paipianwang.pat.facade.product.service.PmsChanPinConfigurationFacade;
import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.service.PmsRightFacade;
import com.paipianwang.pat.facade.right.service.PmsRoleFacade;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;
import com.panfeng.domain.BaseMsg;
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
	private final PmsTeamFacade pmsTeamFacade = null;

	@Autowired
	private final TeamService teamService = null;
	
	@Autowired
	private PmsChanPinConfigurationFacade pmsChanPinConfigurationFacade;

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
		paramMap.put("skill", view.getSkill());
		paramMap.put("productLine", view.getProductLine());
		paramMap.put("teamNature", view.getTeamNature());
		final DataGrid<PmsTeam> dataGrid = pmsTeamFacade.listWithPagination(pageParam, paramMap);
		//设置回显产品线名称		
		setProductLineName(dataGrid.getRows());
		return dataGrid;
	}
	
	/**
	 * 设置产品线名称
	 * @param teamList
	 */
	private void setProductLineName(List<PmsTeam> teamList){
		List<ChanPinLineTree> productLine=pmsChanPinConfigurationFacade.getConfigurationTree();
		Map<String,String> lineMap=new HashMap<>();
		for(ChanPinLineTree parent:productLine){
			for(ChanPinLineTree child:parent.getChildren()){
				lineMap.put(child.getId(), child.getParentText()+"_"+child.getText());
			}
		}
		teamList.stream().forEach(team->{
			String lines=team.getProductLine();
			StringBuffer lineName=new StringBuffer();
			if(ValidateUtil.isValid(lines)){
				String[] lineArray=lines.split(",");
				for(String line:lineArray){
					lineName.append(",").append(lineMap.get(line));
				}
				team.setProductLineName(lineName.toString().substring(1));
			}
			
		});
		
	}

	/**
	 * 验证供应商是否存在
	 * 
	 * @param view
	 *            电话号码 登录名
	 * @return true 不存在 false 存在
	 */
	@RequestMapping(value="/team/isExist", method = RequestMethod.POST)
	public boolean isTeamExist(@RequestBody final TeamView view) {
		if(!ValidateUtil.isValid(view.getPhoneNumber()) && !ValidateUtil.isValid(view.getLoginName())){
			return true;//空不做重复校验
		}
		PmsTeam team = new PmsTeam();
		team.setPhoneNumber(view.getPhoneNumber());
		team.setLoginName(view.getLoginName());
		long ret = pmsTeamFacade.checkExist(team);
		return ret > 0 ? false : true;
	}

	@RequestMapping(value = "/team/save", method = RequestMethod.POST)
	public BaseMsg save(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("displayFile") final MultipartFile displayFile,
			final PmsTeam team) throws Exception {
		BaseMsg baseMsg = new BaseMsg();
		response.setContentType("text/html;charset=UTF-8");
		// 先保存获取ID，然后更新
		team.setPassword(DataUtil.md5(PublicConfig.INIT_PASSWORD));
		updateFile(displayFile,team,null,5);
		
		long teamId = pmsTeamFacade.save(team);
		team.setTeamId(teamId);
		
//		pmsTeamFacade.saveTeamPhotoUrl(team);
		
		
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save team ...", sessionInfo);
		baseMsg.setErrorCode(BaseMsg.NORMAL);
		baseMsg.setErrorMsg("保存成功！");
		return baseMsg;
	}
	
//	private BaseMsg uploadFile(HttpServletRequest request,MultipartFile file,PmsTeam team,BaseMsg baseMsg,int type,String FileName){
//		try {
//			if (!file.isEmpty()) {
//				String path = FastDFSClient.uploadFile(file);
//				
//				switch (type) {
//				case 1://上传LOGO
//					team.setTeamPhotoUrl(path);
//					break;
//				case 2://上传营业执照/身份证
//					team.setCertificateUrl(path);
//					break;
//				case 3://上传法人手持身份证正面
//					team.setIdCardfrontUrl(path);
//					break;
//				case 4://上传法人手持身份证背面
//					team.setIdCardbackUrl(path);
//					break;
//				default:
//					break;
//				}
//			}
//
//		} catch (Exception e) {
//			baseMsg.setErrorCode(BaseMsg.ERROR);
//			baseMsg.setErrorMsg("更新"+FileName+"失败！");
//			SessionInfo sessionInfo = getCurrentInfo(request);
//			Log.error("TeamController method:save() upload team "+FileName+" error ...", sessionInfo);
//			e.printStackTrace();
////			throw new RuntimeException("Team Image upload error ...", e);
//		}
//		return baseMsg;
//	}

	@RequestMapping(value = "/team/update", method = RequestMethod.POST)
	public BaseMsg update(final HttpServletRequest request, final HttpServletResponse response,
//			@RequestParam("file") final MultipartFile file, @RequestParam("certificateFile") final MultipartFile certificateFile,
//			@RequestParam("idCardfrontFile") final MultipartFile idCardfrontFile,
//			@RequestParam("idCardbackFile") final MultipartFile idCardbackFile, 
			@RequestParam("displayFile") final MultipartFile displayFile,
			final PmsTeam team) throws Exception {
		BaseMsg baseMsg = new BaseMsg();
		response.setContentType("text/html;charset=UTF-8");

		// 如果上传文件不为空时，更新 url;反之亦然
		final PmsTeam originalTeam = pmsTeamFacade.findTeamById(team.getTeamId());
		team.setCertificateUrl(originalTeam.getCertificateUrl());
		team.setIdCardfrontUrl(originalTeam.getIdCardfrontUrl());
		team.setIdCardbackUrl(originalTeam.getIdCardbackUrl());
		team.setTeamPhotoUrl(originalTeam.getTeamPhotoUrl());
		team.setDisplayImg(originalTeam.getDisplayImg());
		
//		updateFile(file,team,originalTeam,1);
//		updateFile(certificateFile,team,originalTeam,2);
//		updateFile(idCardfrontFile,team,originalTeam,3);
//		updateFile(idCardbackFile,team,originalTeam,4);
//		
//		pmsTeamFacade.saveTeamPhotoUrl(team);
		updateFile(displayFile,team,originalTeam,5);

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
	@RequestMapping(value = "/team/updateFile", method = RequestMethod.POST)
	public BaseMsg updateFile(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("file") final MultipartFile file, @RequestParam("certificateFile") final MultipartFile certificateFile,
			@RequestParam("idCardfrontFile") final MultipartFile idCardfrontFile,
			@RequestParam("idCardbackFile") final MultipartFile idCardbackFile,
//			@RequestParam("displayFile") final MultipartFile displayFile,
			final PmsTeam team
			) throws Exception {
		BaseMsg baseMsg = new BaseMsg();
		response.setContentType("text/html;charset=UTF-8");

		// 如果上传文件不为空时，更新 url;反之亦然
		final PmsTeam originalTeam = pmsTeamFacade.findTeamById(team.getTeamId());
		team.setCertificateUrl(originalTeam.getCertificateUrl());
		team.setIdCardfrontUrl(originalTeam.getIdCardfrontUrl());
		team.setIdCardbackUrl(originalTeam.getIdCardbackUrl());
		team.setTeamPhotoUrl(originalTeam.getTeamPhotoUrl());
//		team.setDisplayImg(originalTeam.getDisplayImg());
		
		
		updateFile(file,team,originalTeam,1);
		updateFile(certificateFile,team,originalTeam,2);
		updateFile(idCardfrontFile,team,originalTeam,3);
		updateFile(idCardbackFile,team,originalTeam,4);
//		updateFile(displayFile,team,originalTeam,5);
		
		Long ret=pmsTeamFacade.saveTeamPhotoUrl(team);
		
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
	private void updateFile(MultipartFile file,PmsTeam team,PmsTeam originalTeam,int type){
		if (!file.isEmpty()) {
			String path = FastDFSClient.uploadFile(file);
			// 删除 原文件
			if (originalTeam != null) {
				final String originalPath = originalTeam.getTeamPhotoUrl();
				FastDFSClient.deleteFile(originalPath);
			}
			switch (type) {
			case 1://上传LOGO
				team.setTeamPhotoUrl(path);
				break;
			case 2://上传营业执照/身份证
				team.setCertificateUrl(path);
				break;
			case 3://上传法人手持身份证正面
				team.setIdCardfrontUrl(path);
				break;
			case 4://上传法人手持身份证背面
				team.setIdCardbackUrl(path);
				break;
			case 5://上传显示图片
				team.setDisplayImg(path);
				break;
			default:
				break;
			}
		}
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
					if (team.getCertificateUrl() != null && !"".equals(team.getCertificateUrl())) {
						FastDFSClient.deleteFile(team.getCertificateUrl());
					}
					if (team.getIdCardfrontUrl() != null && !"".equals(team.getIdCardfrontUrl())) {
						FastDFSClient.deleteFile(team.getIdCardfrontUrl());
					}
					if (team.getIdCardbackUrl() != null && !"".equals(team.getIdCardbackUrl())) {
						FastDFSClient.deleteFile(team.getIdCardbackUrl());
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
	public void export(final TeamView view, HttpServletRequest request, final HttpServletResponse response) {
		OutputStream outputStream = null;
		try {			
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
//			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			String dateString = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
			String filename = "供应商列表" + dateString + ".xlsx";
			
			//---处理文件名
			String userAgent = request.getHeader("User-Agent"); 
			if (userAgent.contains("MSIE")||userAgent.contains("Trident") ||userAgent.contains("Edge")) {
				//针对IE或者以IE为内核或Microsoft Edge的浏览器：
				filename = java.net.URLEncoder.encode(filename, "UTF-8");
			} else {
			//非IE浏览器的处理：
				filename = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
			}
			
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
			paramMap.put("skill", view.getSkill());
			paramMap.put("productLine", view.getProductLine());
			paramMap.put("teamNature", view.getTeamNature());

			List<PmsTeam> teamList = pmsTeamFacade.listWithParam(paramMap);
			setProductLineName(teamList);
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
	 * 处理team临时表,更新team备注
	 */
	@RequestMapping("/team/deal/teamTmpAndTeamDesc")
	public boolean dealTeamTmpAndUpdateTeamDesc(@RequestBody PmsTeam team, HttpServletRequest request) {
		try {
			if (null != team) {
				String description = null == team.getDescription() ? "" : team.getDescription();
				team.setDescription(description);
				// 更新备注信息
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
	 * 首页供应商推荐排序或者删除 action 排序动作 up down del index 当前排序
	 */
	@RequestMapping("/team/recommend/sort")
	public boolean sortRecommendTeam(final String action, final String teamId) {
		boolean flag = false;
		long id = Long.valueOf(teamId);
		switch (action) {
		case "up":
			flag = pmsTeamFacade.moveUp(id);
			break;
		case "down":
			flag = pmsTeamFacade.moveDown(id);
			break;
		case "del":
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

	/*
	 * 获取首页供应商推荐
	 */
	@RequestMapping(value = "/team/recommend", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<PmsTeam> teamRecommendList() {
		return pmsTeamFacade.teamRecommendList();
	}

}
