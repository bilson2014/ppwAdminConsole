package com.panfeng.service.impl;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.PathFormatUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.facade.user.entity.PmsUser;
import com.paipianwang.pat.facade.user.service.PmsUserFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.mq.service.SmsMQService;
import com.panfeng.persist.DealLogMapper;
import com.panfeng.persist.FlowDateMapper;
import com.panfeng.persist.IndentFlowMapper;
import com.panfeng.persist.IndentProjectMapper;
import com.panfeng.poi.GenerateExcel;
import com.panfeng.poi.ProjectPoiAdapter;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.DealLog;
import com.panfeng.resource.model.FlowDate;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.IndentResource;
import com.panfeng.resource.model.Synergy;
import com.panfeng.resource.model.User;
import com.panfeng.resource.model.UserViewModel;
import com.panfeng.resource.view.IndentProjectView;
import com.panfeng.service.DealLogService;
import com.panfeng.service.EmployeeService;
import com.panfeng.service.IndentActivitiService;
import com.panfeng.service.IndentCommentService;
import com.panfeng.service.IndentProjectService;
import com.panfeng.service.IndentResourceService;
import com.panfeng.service.SynergyService;
import com.panfeng.service.UserTempService;

@Service
public class IndentProjectServiceImpl implements IndentProjectService {

	@Autowired
	IndentProjectMapper indentProjectMapper;

	@Autowired
	IndentActivitiService indentActivitiService;

	@Autowired
	IndentFlowMapper indentFlowMapper;

	@Autowired
	FlowDateMapper flowDateMapper;

	@Autowired
	IndentCommentService indentCommentService;

	@Autowired
	UserTempService userTempService;

	@Autowired
	SynergyService synergyService;

	@Autowired
	final PmsEmployeeFacade pmsEmployeeFacade = null;

	@Autowired
	final EmployeeService employeeService = null;

	@Autowired
	DealLogService dealLogService;

	@Autowired
	IndentResourceService indentResourceService;

	@Autowired
	PmsUserFacade pmsUserFacade;

	@Autowired
	DealLogMapper dealLogMapper;

	@Autowired
	SmsMQService smsMQService;

	private static String SHANGWU = "商务";

	@Override
	public boolean save(IndentProject indentProject, SessionInfo sessionInfo) {

		indentProjectMapper.save(indentProject);
		indentProject.setSerial(getProjectSerialID(indentProject.getId()));
		indentProjectMapper.updateSerialID(indentProject);

		// add synergy by laowang begin 2016-5-25 12:01
		List<Synergy> list = indentProject.getSynergys();
		boolean isValid = ValidateUtil.isValid(list);
		if (isValid) {
			for (Synergy synergy : list) {
				if (null == synergy.getUserName()) {// 后台添加的数据,没有视频管家名字
					String realName = pmsEmployeeFacade.findEmployeeById(synergy.getUserId()).getEmployeeRealName();
					synergy.setUserName(realName);
				}
				synergy.setProjectId(indentProject.getId());
				synergyService.save(synergy);
			}
		}
		// 解决项目重复bug
		boolean res = indentActivitiService.startProcess(indentProject, sessionInfo);
		if (!res) {
			indentProjectMapper.deleteById(indentProject.getId());
			for (Synergy synergy : list) {
				synergyService.delete(synergy.getSynergyId());
			}
		}
		return res;
	}

	@Override
	public long delete(IndentProject indentProject) {
		if (indentProject == null)
			return 0;
		return indentProjectMapper.deleteById(indentProject.getId());
	}

	@Override
	public List<IndentProject> findProjectList(IndentProject indentProject) {
		String userType = indentProject.getUserType();
		List<IndentProject> list = null;
		switch (userType) {
		// 用户身份 -- 客户
		case PmsConstant.ROLE_CUSTOMER:
			indentProject.setCustomerId(indentProject.getUserId());
			list = indentProjectMapper.findProjectByUserName(indentProject);
			break;
		// 用户身份 -- 供应商
		case PmsConstant.ROLE_PROVIDER:
			indentProject.setTeamId(indentProject.getUserId());
			list = indentProjectMapper.findProjectByUserName(indentProject);
			break;
		// 用户身份 -- 视频管家
		case PmsConstant.ROLE_EMPLOYEE:
			list = indentProjectMapper.findProjectList(indentProject);
			break;
		}
		return list;
	}

	@Override
	public IndentProject getProjectInfo(IndentProject indentProject) {
		return indentProjectMapper.findProjectInfo(indentProject);
	}

	@Override
	public IndentProject getProjectInfo(long id) {
		return indentProjectMapper.findProjectInfo2(id);
	}

	@Override
	public IndentProject getRedundantProject(IndentProject indentProject) {
		indentProject = indentProjectMapper.findProjectInfo(indentProject);
		if(indentProject != null) {
			List<IndentFlow> listDates = indentFlowMapper.findFlowDateByIndentId(indentProject);
			IndentFlow.indentProjectFillDate(indentProject, listDates);
			// add Synergys by laowang begin 2016-5-25 16:00
			indentProject.setSynergys(synergyService.findSynergyByProjectId(indentProject.getId()));
			// add Synergys by laowang end 2016-5-25 16:00
			return indentProject;
		}
		
		return null;
	}

	/**
	 * 前端更新项目
	 */
	@Override
	public boolean updateIndentProject(IndentProject indentProject, boolean isUpdateSynergy) {
		// 更新供应商和用户实际支付金额
		// update project
		long l;
		if (isUpdateSynergy) {
			l = indentProjectMapper.updateSynergy(indentProject);
		} else {
			l = indentProjectMapper.update(indentProject);
		}
		if (l > 0) {
			List<IndentFlow> listDates = indentFlowMapper.findFlowDateByIndentId(indentProject);
			List<FlowDate> dates = IndentFlow.getFlowDates(listDates);
			IndentFlow.updateFlowDates(indentProject, dates);
			for (FlowDate flowDate : dates) {
				flowDateMapper.update(flowDate);
			}
			// 获取项目的协同人 666
			Map<Long, Synergy> map = synergyService.findSynergyMapByProjectId(indentProject.getId());
			List<Synergy> sList = indentProject.getSynergys();
			if (ValidateUtil.isValid(sList)) {
				for (final Synergy synergy : sList) {
					Synergy originalSynergy = map.get(synergy.getSynergyId());
					if (originalSynergy == null) {
						synergy.setProjectId(indentProject.getId());
						synergyService.save(synergy);
					} else {
						synergyService.update(synergy);
					}
				}
			}

			indentCommentService.createSystemMsg("更新了 " + indentProject.getProjectName() + "项目", indentProject);
			return true;
		}
		return false;
	}

	@Override
	public ActivitiTask getTaskInfo(IndentProject indentProject) {
		String taskName = indentProject.getTask().getName();
		List<ActivitiTask> activitiTasks = indentActivitiService.getHistoryProcessTask(indentProject);

		ActivitiTask at = null;
		for (ActivitiTask activitiTask : activitiTasks) {
			if (activitiTask.getName().equals(taskName)) {
				at = activitiTask;
				break;
			}
		}
		if (at != null) {
			// 填充预计时间
			IndentFlow indentFlow = indentFlowMapper.findFlowDateByFlowKey(indentProject.getId(),
					at.getTaskDefinitionKey());
			at.setScheduledTime(new FlowDate(indentFlow.getFdId(), indentFlow.getFdFlowId(),
					indentFlow.getFdStartTime(), indentFlow.getFdTaskId()));
		} else {
			at = new ActivitiTask();
		}
		return at;
	}

	public boolean cancelProject(IndentProject indentProject) {
		indentProject.setState(IndentProject.PROJECT_CANCEL);
		long l = indentProjectMapper.updateState(indentProject.getId(), IndentProject.PROJECT_CANCEL,
				indentProject.getDescription());
		indentCommentService.createSystemMsg(
				"取消了" + indentProject.getProjectName() + "项目,原因：" + indentProject.getDescription(), indentProject);
		return (l > 0);
	}

	public void getReport(IndentProject indentProject, OutputStream outputStream) {
		List<IndentProject> list = indentProjectMapper.findProjectList(indentProject);

		// 为每一个项目添加协同人
		if (list != null) {
			for (IndentProject indentProject2 : list) {
				List<Synergy> Synergys = synergyService.findSynergyByProjectId(indentProject2.getId());
				indentProject2.setSynergys(Synergys);
			}
		}

		getReport(list, outputStream);
	}

	public void getReport(List<IndentProject> list, OutputStream outputStream) {
		ProjectPoiAdapter projectPoiAdapter = new ProjectPoiAdapter();
		GenerateExcel ge = new GenerateExcel();
		for (IndentProject indentProject2 : list) {
			List<IndentFlow> listDates = indentFlowMapper.findFlowDateByIndentId(indentProject2);
			IndentFlow.indentProjectFillDate(indentProject2, listDates);
			ActivitiTask at = indentActivitiService.getCurrentTask(indentProject2);
			if (at.getId().equals("")) {
				List<HistoricTaskInstance> listHistoricTaskInstances = indentActivitiService
						.getHistoryProcessTask_O(indentProject2);
				HistoricTaskInstance historicTaskInstance = listHistoricTaskInstances
						.get(listHistoricTaskInstances.size() - 1);
				at.setId("");
				at.setName("已完成");
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				at.setCreateTime(simpleDateFormat.format(historicTaskInstance.getEndTime()));

			}
			indentProject2.setTask(at);
			// 填充管家
			UserViewModel userViewModel = userTempService.getInfo(indentProject2.getUserType(),
					indentProject2.getUserId());
			indentProject2.setUserViewModel(userViewModel);
			indentProject2.setEmployeeRealName(userViewModel.getUserName());
			projectPoiAdapter.getData().add(indentProject2);
		}
		ge.generate(projectPoiAdapter, outputStream);
	}

	public void getReportForExport(List<IndentProject> list, OutputStream outputStream) {
		ProjectPoiAdapter projectPoiAdapter = new ProjectPoiAdapter();
		GenerateExcel ge = new GenerateExcel();
		projectPoiAdapter.setData(list);
		ge.generate(projectPoiAdapter, outputStream);
	}

	public List<IndentProject> listWithPagination(final IndentProjectView view) {

		List<IndentProject> list = new ArrayList<IndentProject>();
		if (null == view.getIsSynergy() || view.getIsSynergy() == 0) {
			list = indentProjectMapper.listWithPaginationAll(view);
		} else {
			list = indentProjectMapper.listWithPaginationAllAndSynergy(view);
		}
		Map<Long, PmsEmployee> eMap = employeeService.getAllEmployeeMap();
		Map<Long, List<Synergy>> sMap = synergyService.findSynergyMap();
		for (final IndentProject pro : list) {
			final PmsEmployee user = eMap.get(pro.getUserId());
			final PmsEmployee referer = eMap.get(pro.getReferrerId());
			final List<Synergy> sList = sMap.get(pro.getId());
			if (user != null) {
				PmsEmployee vv = eMap.get(pro.getUserId());
				Integer dimissionStatus = vv.getDimissionStatus();
				if (dimissionStatus == null) {
					dimissionStatus = -1;
				}
				if (dimissionStatus == PmsEmployee.DIMISSIONSTATUS_DIMISSION) {
					pro.setEmployeeRealName(vv.getEmployeeRealName() + "(离职)");
				} else {
					pro.setEmployeeRealName(vv.getEmployeeRealName());
				}

			}
			if (referer != null)
				pro.setReferrerName(eMap.get(pro.getReferrerId()).getEmployeeRealName());
			if (ValidateUtil.isValid(sList)) {
				pro.setSynergys(sList);
			}
		}
		return list;
	}

	public long maxSize(final IndentProjectView view) {

		final long total = indentProjectMapper.maxSize(view);
		return total;
	}

	@Transactional
	public long delete(final long[] ids) {

		if (ValidateUtil.isValid(ids)) {
			for (final long id : ids) {
				indentProjectMapper.deleteById(id);
			}
		}
		return 1l;
	}

	public List<IndentProject> getAllTeam() {

		final List<IndentProject> list = indentProjectMapper.getAllTeam();
		return list;
	}

	public List<IndentProject> getAllUser() {

		final List<IndentProject> list = indentProjectMapper.getAllUser();
		return list;
	}

	public List<IndentProject> getAllVersionManager() {

		final List<IndentProject> list = indentProjectMapper.getAllVersionManager();
		return list;
	}

	public List<IndentProject> getAllProject() {

		List<IndentProject> list = indentProjectMapper.getAllProject();
		return list;
	}

	@Override
	public String getProjectSerialID(Long id) {
		long count;
		if (id != null && id != 0) {
			count = id;
		} else {
			count = indentProjectMapper.getProjectCount();
		}
		String date = PathFormatUtils.parse("{yyyy}{mm}{dd}");
		String formatNum = count + "";
		if (count < 100) {
			if (count < 10) {
				formatNum = "00" + formatNum;
			} else {
				formatNum = '0' + formatNum;
			}
		}
		return date + formatNum;
	}

	@Override
	public long update(IndentProject indentProject) {
		Map<Long, Synergy> map = synergyService.findSynergyMapByProjectId(indentProject.getId());
		List<Synergy> sList = indentProject.getSynergys();
		if (ValidateUtil.isValid(sList)) {
			for (final Synergy synergy : sList) {
				PmsEmployee employee = pmsEmployeeFacade.findEmployeeById(synergy.getUserId());
				synergy.setUserName(employee.getEmployeeRealName());
				Synergy originalSynergy = map.get(synergy.getSynergyId());
				if (originalSynergy == null) {
					synergy.setProjectId(indentProject.getId());
					synergyService.save(synergy);
				} else {
					synergyService.update(synergy);
				}
			}
		}
		final long ret = indentProjectMapper.updateBack(indentProject);
		return ret;
	}

	@Override
	public long removeSynergy(long synergyId) {
		return synergyService.delete(synergyId);
	}

	@Override
	public List<IndentProject> getSynergys(IndentProject indentProject) {
		List<Synergy> synergies = synergyService.findSynergyByUserId(indentProject.getUserId());
		List<Long> ids = new ArrayList<>();
		for (Synergy ip : synergies) {
			ids.add(ip.getProjectId());
		}
		if (ids.size() > 0) {
			return indentProjectMapper.findProjectByIds(ids);
		}
		return new ArrayList<>();
	}

	// add by wanglc 2016-6-29 10:42:43 begin
	// 项目管理:查询含有协同人的数据量
	/**
	 * 项目管理:查询含有协同人的数据量
	 */
	@Override
	public long maxSizeAddSynergy(IndentProjectView view) {
		long total = indentProjectMapper.maxSizeAddSynergy(view);
		return total;
	}
	// add by wanglc 2016-6-29 10:42:57 end

	@Override
	public List<IndentProject> all() {

		final List<IndentProject> list = indentProjectMapper.all();
		return list;
	}

	@Override
	public synchronized BaseMsg verifyProjectInfo(long projectId) {
		IndentProject indentProject = new IndentProject();
		indentProject.setId(projectId);
		indentProject = indentProjectMapper.findProjectInfo(indentProject);

		if (indentProject != null) {
			long teamId = indentProject.getTeamId();
			if (teamId <= 0) {
				return new BaseMsg(BaseMsg.ERROR, "错误", "必须完善供应商信息");
			}
		}
		long count = dealLogService.notPayNumber(indentProject.getId());
		if (count <= 0) {
			return new BaseMsg(BaseMsg.NORMAL, "正常", "验证通过");
		} else {
			return new BaseMsg(BaseMsg.WARNING, "警告", "有未支付的订单");
		}
	}

	@Override
	public BaseMsg verifyIntegrity(IndentProject indentProject) {
		BuildRes buildRes = new BuildRes();
		String html = "";
		// 构造查询数据源
		List<IndentResource> fileList = indentResourceService.findIndentList(indentProject);
		IndentProject ip = getRedundantProject(indentProject);
		Boolean skipay = indentProject.getSkipPay();
		ip.setTask(indentProject.getTask());
		if (skipay != null) {
			ip.setSkipPay(skipay);
		}
		Map<String, String> pram = new HashMap<>();
		pram.put("userid", ip.getUserId() + "");
		pram.put("userType", ip.getUserType());
		pram.put("projectId", ip.getId() + "");
		List<DealLog> deals = dealLogService.getDealLogByProject(pram);
		boolean flag = false;
		boolean isSUser = isSLevel(ip);

		List<String> stepText = indentActivitiService.getBpmnNodes(indentProject);

		for (int i = (stepText.size() - 1); i > -1; i--) {
			if (flag) {
				Map<String, Boolean> r = execute(fileList, ip, deals, stepText.get(i));
				if (ValidateUtil.isValid(r)) {
					BuildRes br = buildHtml(r, 2, isSUser, indentProject, stepText);
					html += br.getHtml();
				}
			}
			if (stepText.get(i).equals(indentProject.getTask().getName())) {
				flag = true;
				Map<String, Boolean> r = execute(fileList, ip, deals, stepText.get(i));
				if (ValidateUtil.isValid(r)) {
					BuildRes br = buildHtml(r, 1, isSUser, indentProject, stepText);
					buildRes.setIsok(br.isIsok());
					html += br.getHtml();
				}
			}
		}
		buildRes.setHtml(html);
		return new BaseMsg(BaseMsg.NORMAL, "", buildRes);
	}

	/**
	 * buildType =1,输出当前阶段全部认证信息 |%%%%%| buildType =2,输出当前阶段错误信息
	 * 
	 * @param res
	 * @param buildType
	 * @return
	 */
	private BuildRes buildHtml(Map<String, Boolean> res, int buildType, boolean isSuser, IndentProject indentProject,
			List<String> stepText) {
		StringBuilder stringBuilder = new StringBuilder();
		BuildRes br = new BuildRes();
		boolean iok = true;
		switch (buildType) {
		case 1:
			int index = 1;
			for (String key : res.keySet()) {
				stringBuilder.append("<li class ='curr'>");
				stringBuilder.append("<div class = 'index'>");
				stringBuilder.append(index);
				stringBuilder.append("</div>");
				stringBuilder.append("<div class = 'info'>");
				stringBuilder.append(key);
				stringBuilder.append("</div>");
				if (res.get(key)) {
					stringBuilder.append("<div class = 'infoimgR'>");
					stringBuilder.append("√");
				} else {
					stringBuilder.append("<div class = 'infoimgG'>");
					if (SHANGWU.equals(indentProject.getTask().getName()) && key.equals("有支付完成订单")) {
						if (isSuser && iok) {
							if (indentProject.getSkipPay() != null ? indentProject.getSkipPay() : false) {
							} else {
								stringBuilder.append("<a onclick='nextFlow2()'><div>延迟付款</div></a>");
								iok = false;
							}
						} else {
							stringBuilder.append("X");
							iok = false;
						}
					} else {
						stringBuilder.append("X");
						iok = false;
					}
				}
				stringBuilder.append("</div>");
				stringBuilder.append("</li>");
				index++;
			}
			br.setIsok(iok);
			break;
		case 2:
			for (String key : res.keySet()) {
				if (!res.get(key)) {
					stringBuilder.append("<li class ='before'>");
					stringBuilder.append("</div>");
					stringBuilder.append("<div class = '_info'>");
					stringBuilder.append(key);
					stringBuilder.append("</div>");
					stringBuilder.append("<div class = '_infoimg'>");
					stringBuilder.append("X");
					stringBuilder.append("</div>");
					stringBuilder.append("</li>");
				}
			}
			break;
		}
		br.setHtml(stringBuilder.toString());
		return br;
	}

	private Map<String, Boolean> execute(List<IndentResource> fileList, IndentProject ip, List<DealLog> deals,
			String task) {
		Map<String, Boolean> res = new LinkedHashMap<>();
		// 构造查询条件集合
		List<fileType> file = new ArrayList<>();
		List<InfoType> info = new ArrayList<>();
		List<PayType> pay = new ArrayList<>();
		// 根据步骤，拼装不同验证组件（调度中心）
		switch (task) {
		case "沟通":
			info.add(InfoType.base);
			info.add(InfoType.time);
			file.add(fileType.XuQiuWenDang);
			file.add(fileType.QA);
			break;
		case "方案":
			// 方案
			file.add(fileType.CeHuaFangAn);
			file.add(fileType.BaoJiaDan);
			file.add(fileType.PaiQiBiao);
			break;
		case "商务":
			// String shangwu = stepText.get(2);
			// 查询用户级别
			if (isSLevel(ip)) {
				// S级别用户特殊对待
				if (ip.getTask().getName().equals(SHANGWU)) {
					// 身为s用户有权利跳过（只在商务阶段检测 延迟付款 ）
					if (ip.getSkipPay() != null ? ip.getSkipPay() : false) {
						info.add(InfoType.provider);
						info.add(InfoType.customerPayment);
						info.add(InfoType.priceFinish);
					} else {
						info.add(InfoType.provider);
						info.add(InfoType.customerPayment);
						info.add(InfoType.priceFinish);
						pay.add(PayType.payFinish);
					}
				} else {
					// 不是商务步骤，不检测S级别客户的支付情况
					info.add(InfoType.provider);
					info.add(InfoType.customerPayment);
					info.add(InfoType.priceFinish);
				}
				break;
			}
			// 苦逼的小白
			info.add(InfoType.provider);
			info.add(InfoType.customerPayment);
			info.add(InfoType.priceFinish);
			pay.add(PayType.payFinish);
			break;
		case "制作":
			// 制作
			file.add(fileType.FenJingTouJiaoBen);
			break;
		case "交付":
			// 交付
			info.add(InfoType.providerPayment);
			break;
		default:
			break;
		}

		// 进入业务操作
		if (ValidateUtil.isValid(file)) {
			Map<String, Boolean> r = verifyResFile(file, fileList);
			if (ValidateUtil.isValid(r)) {
				res.putAll(r);
			}
		}
		if (ValidateUtil.isValid(info)) {
			Map<String, Boolean> r = verifyInfo(info, ip);
			if (ValidateUtil.isValid(r)) {
				res.putAll(r);
			}
		}
		if (ValidateUtil.isValid(pay)) {
			Map<String, Boolean> r = verifyPay(pay, deals);
			if (ValidateUtil.isValid(r)) {
				res.putAll(r);
			}
		}
		return res;
	}

	private boolean isSLevel(IndentProject indentProject) {
		Long userId = indentProject.getCustomerId();
		// User user = userService.findUserById(userId);
		PmsUser user = pmsUserFacade.findUserById(userId);
		if (user.getClientLevel() != null) {
			int userClientLevel = user.getClientLevel();
			return userClientLevel == User.S;
		}
		return false;
	}

	/**
	 * 验证资源文件
	 * 
	 * @param resType
	 * @param projectId
	 * @return
	 */
	private Map<String, Boolean> verifyResFile(List<fileType> resTypes, List<IndentResource> resList) {
		/**
		 * 结果储存 ？？
		 */
		Map<String, Boolean> res = new HashMap<>();
		for (int i = 0; i < resTypes.size(); i++) {
			String type = resTypes.get(i).getText();
			boolean isExist = false;
			for (int j = 0; j < resList.size(); j++) {
				IndentResource indentResource = resList.get(j);
				String resType = indentResource.getIrtype();
				resType = resType.replaceAll("&amp;", "&");
				if (resType.equals(type)) {
					isExist = true;
				}
			}
			if (isExist) {
				res.put(type, true);
			} else {
				res.put(type, false);
			}
		}
		return res;
	}

	/**
	 * 验证项目信息
	 * 
	 * @param scope
	 * @param projectId
	 * @return
	 */
	private Map<String, Boolean> verifyInfo(List<InfoType> scope, IndentProject ip) {
		Map<String, Boolean> res = new HashMap<>();
		InfoType infoType;
		for (int i = 0; i < scope.size(); i++) {
			infoType = scope.get(i);
			switch (infoType) {
			case base:
				if (!ValidateUtil.isValid(ip.getProjectName())) {
					res.put(infoType.getText(), false);
				} else {
					res.put(infoType.getText(), true);
				}

				if (!ValidateUtil.isValid(ip.getSource())) {
					res.put(infoType.getText(), false);
				} else if (ip.getSource().equals("个人信息下单") && !(ip.getReferrerId() != null && ip.getReferrerId() > 0)) {
					res.put(infoType.getText() + ",个人信息下单", false);
				} else {
					res.put(infoType.getText(), true);
				}

				if (ip.getCustomerId() != null && ip.getCustomerId() > 0) {
					if (ValidateUtil.isValid(ip.getUserName())) {
						if (ValidateUtil.isValid(ip.getUserContact())) {
							if (ValidateUtil.isValid(ip.getUserPhone())) {
								res.put(infoType.getText(), true);
							} else {
								res.put(infoType.getText(), false);
							}
						} else {
							res.put(infoType.getText(), false);
						}
					} else {
						res.put(infoType.getText(), false);
					}
				} else {
					res.put(infoType.getText(), false);
				}

				if (ip.getPriceFirst() <= 0) {
					res.put(infoType.getText(), false);
				} else {
					res.put(infoType.getText(), true);
				}
				if (ip.getPriceLast() <= 0) {
					res.put(infoType.getText(), false);
				} else {
					res.put(infoType.getText(), true);
				}

				break;
			case time:
				Map<String, String> ipTime = ip.getTime();
				boolean flag = false;
				for (String val : ipTime.values()) {
					if (val.equals("")) {
						flag = true;
						break;
					} else {
						flag = false;
					}
				}
				if (flag) {
					res.put(infoType.getText(), false);
				} else {
					res.put(infoType.getText(), true);
				}
				break;
			case provider:
				if (ip.getTeamId() != null && ip.getTeamId() > 0) {
					if (ValidateUtil.isValid(ip.getTeamName())) {
						if (ValidateUtil.isValid(ip.getTeamContact())) {
							if (ValidateUtil.isValid(ip.getTeamContact())) {
								res.put(infoType.getText(), true);
							} else {
								res.put(infoType.getText(), false);
							}
						} else {
							res.put(infoType.getText(), false);
						}
					} else {
						res.put(infoType.getText(), false);
					}
				} else {
					res.put(infoType.getText(), false);
				}
				break;
			case synergy:
				// List<Synergy> synergys = ip.getSynergys();
				// if (!ValidateUtil.isValid(synergys)) {
				// return "协同人信息不完整，请补充！";
				// }
				break;
			case providerPayment:
				if (ip.getProviderPayment() == null) {
					res.put(infoType.getText(), false);
				} else {
					res.put(infoType.getText(), true);
				}
				break;
			case customerPayment:
				if (ip.getCustomerPayment() == null) {
					res.put(infoType.getText(), false);
				} else {
					res.put(infoType.getText(), true);
				}
				break;
			case priceFinish:
				if (ip.getPriceFinish() == null) {
					res.put(infoType.getText(), false);
				} else {
					res.put(infoType.getText(), true);
				}
				break;
			}
		}
		return res;
	}

	/**
	 * 验证支付信息
	 * 
	 * @param scope
	 * @param projectId
	 * @return
	 */
	private Map<String, Boolean> verifyPay(List<PayType> scope, List<DealLog> deals) {
		Map<String, Boolean> res = new HashMap<>();
		if (deals.size() > 0) {
			for (PayType payType : scope) {
				switch (payType) {
				case payFinish:
					boolean hasPayComplete = false;
					for (DealLog dealLog : deals) {
						if (dealLog.getDealStatus() == 1) {
							hasPayComplete = true;
						}
					}
					if (!hasPayComplete)
						res.put(payType.getText(), false);
					else
						res.put(payType.getText(), true);
					break;
				}
			}
		} else {
			res.put(scope.get(0).getText(), false);
		}
		return res;
	}

	enum InfoType {
		base("基础信息"), time("预计时间"), provider("供应商信息"), synergy("协同人"), providerPayment("供应商实际支付金额"), customerPayment(
				"客户实际支付金额"), priceFinish("最终价格");

		private String text;

		InfoType(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	enum PayType {
		payFinish("有支付完成订单");

		private String text;

		PayType(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	/*
	 * tags.add("需求文档"); tags.add("Q&A文档"); tags.add("排期表"); tags.add("策划方案");
	 * tags.add("报价单"); tags.add("制作导演信息"); tags.add("分镜头脚本"); tags.add("花絮");
	 * tags.add("成片");
	 */
	enum fileType {

		XuQiuWenDang("需求文档"), QA("Q&A文档"), PaiQiBiao("排期表"), CeHuaFangAn("策划方案"), BaoJiaDan("报价单"), ZhiZuoDaoYan(
				"制作导演信息"), FenJingTouJiaoBen("分镜头脚本"), HuaXun("花絮");

		fileType(String text) {
			this.text = text;
		}

		private String text;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	@Override
	public List<IndentProject> findProjectListByPhone(IndentProject indentProject) {
		List<IndentProject> list = findProjectList(indentProject);
		if (ValidateUtil.isValid(list)) {
			list = indentActivitiService.fullCurrentTask(list);
			for (int i = 0; i < list.size(); i++) {
				List<ActivitiTask> node = new ArrayList<>();
				List<ActivitiTask> nodes = indentActivitiService.getNodes(list.get(i));
				for (ActivitiTask activitiTask : nodes) {
					activitiTask.getScheduledTime().setFdStartTime("");
					activitiTask.setCreateTime("");
				}
				for (int j = 0; j < nodes.size(); j++) {
					try {
						node.add(nodes.get(j).clone());
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
				IndentProject ii2 = list.get(i);
				ii2.setNodes(node);
				if (ii2.getMasterFlowId() != null) {
					indentActivitiService.updateNodes(ii2);
				}
			}
		}
		return list;
	}

	public void deleteProject(IndentProject indentProject) {
		// IndentProject ip =
		// indentProjectMapper.findProjectInfo(indentProject);
		// 删除支付记录
		dealLogMapper.deleteDealByProjectId(indentProject.getId());
		// 删除文件
		indentResourceService.removeIndentResource(indentProject);
		// 删除评论
		indentCommentService.removeIndentCommentList(indentProject);
		// 删除流程
		indentActivitiService.removeProcess(indentProject);
		// 删除本身
		indentProjectMapper.deleteById(indentProject.getId());

	}

	class BuildRes {
		private boolean isok;
		private String html;

		public boolean isIsok() {
			return isok;
		}

		public void setIsok(boolean isok) {
			this.isok = isok;
		}

		public String getHtml() {
			return html;
		}

		public void setHtml(String html) {
			this.html = html;
		}
	}

	/**
	 * 导出excel表格
	 * 
	 * @param list
	 * @param outputStream
	 */
	public void export2Excel(final List<IndentProject> list, final OutputStream outputStream) {

	}

}
