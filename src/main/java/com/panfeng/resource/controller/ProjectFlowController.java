package com.panfeng.resource.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.finance.entity.PmsDealLog;
import com.paipianwang.pat.facade.finance.service.PmsFinanceFacade;
import com.paipianwang.pat.facade.indent.entity.IndentSource;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.entity.PmsProjectResource;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectResourceFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.persist.ActivitiMemberShipMapper;
import com.panfeng.resource.model.ActivitiMember;
import com.panfeng.resource.view.ProjectFlowView;
import com.panfeng.service.ProjectFlowService;

@RestController
@RequestMapping("/portal")
public class ProjectFlowController extends BaseController {

	@Autowired
	private PmsProjectFlowFacade pmsProjectFlowFacade;
	@Autowired
	private PmsProjectResourceFacade pmsProjectResourceFacade;
	@Autowired
	private PmsProjectMessageFacade pmsProjectMessageFacade;
	@Autowired
	private PmsFinanceFacade pmsFinanceFacade;
	@Autowired
	private ProjectFlowService projectFlowService;
	@Autowired
	private ActivitiMemberShipMapper activitiMemberShipMapper;

	/**
	 * 项目管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/project-flow")
	public ModelAndView view(final ModelMap model) {
		// 处理数据字典
		List<Map<String, Object>> emlist = new ArrayList<Map<String, Object>>();
		for (IndentSource source : IndentSource.values()) {
			Map<String, Object> map = new HashMap<>();
			map.put("value", source.getValue());
			map.put("text", source.getName());
			emlist.add(map);
		}
		Object json = JSONArray.toJSON(emlist);
		model.put("sourceCombobox", json);
		return new ModelAndView("projectflow-list");
	}

	/**
	 * 分页查看项目信息
	 * @param view
	 * @param pageParam
	 * @return
	 */
	@RequestMapping(value = "/projectflow/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsProjectFlow> listWithPagination(final ProjectFlowView view, PageParam pageParam) {
		// 封装分页参数
		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);
		// 封装查询参数
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectName", view.getProjectName());
		paramMap.put("projectStatus", view.getProjectStatus());
		paramMap.put("principalName", view.getPrincipalName());
		paramMap.put("schemeName", view.getSchemeName());
		paramMap.put("superviseName", view.getSuperviseName());
		paramMap.put("userName", view.getUserName());
		paramMap.put("teamName", view.getTeamName());
		paramMap.put("projectSource", view.getProjectSource());
		paramMap.put("projectStage", view.getProjectStage());
		paramMap.put("productId", view.getProductId());
		paramMap.put("productConfigLevelId", view.getProductConfigLevelId());
		paramMap.put("beginTime", view.getBeginTime());
		paramMap.put("endTime", view.getEndTime());

		final DataGrid<PmsProjectFlow> dataGrid = pmsProjectFlowFacade.listWithPagination(pageParam, paramMap);

		return dataGrid;
	}
	/**
	 * 查看项目文件信息
	 * @param view
	 * @return
	 */
	@RequestMapping(value = "project-resource/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsProjectResource> listResourceByProject(final ProjectFlowView view) {
		final List<PmsProjectResource> dataGrid = pmsProjectResourceFacade.getResourceByProjectId(view.getProjectId());
		return new DataGrid<PmsProjectResource>(dataGrid.size(), dataGrid);
	}

	/**
	 * 分页查看项目日志信息
	 * @param view
	 * @param pageParam
	 * @return
	 */
	@RequestMapping(value = "project-message/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsProjectMessage> listMessageByProject(final ProjectFlowView view, PageParam pageParam) {
		// 封装分页参数
		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);
		// 封装查询参数
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", view.getProjectId());

		final DataGrid<PmsProjectMessage> dataGrid = pmsProjectMessageFacade.listWithPagination(pageParam, paramMap);

		return dataGrid;
	}

	/**
	 * 查看项目财务信息
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/project-finance/{projectId}")
	public Map<String, Object> listMessageByProject(@PathVariable("projectId") String projectId) {
		List<PmsDealLog> finances = pmsFinanceFacade.listByProjectId(projectId);
		Map<String, Object> result = new HashMap<>();
		if (ValidateUtil.isValid(finances)) {
			for (PmsDealLog finance : finances) {
				if ("role_customer".equals(finance.getUserType())) {
					// 客户实付金额
					result.put("customerAmount", finance.getPayPrice());
				} else if ("role_provider".equals(finance.getUserType())) {
					// 供应商实发金额
					result.put("providerAmount", finance.getPayPrice());
				}
			}
		}
		return result;
	}
	
	/**
	 * 文件下载
	 * @param id
	 * @param response
	 * @param request
	 */
	@RequestMapping("/project/getDFSFile/{id}")
	public void getDFSFile(@PathVariable final long id, final HttpServletResponse response,
			final HttpServletRequest request) {
		PmsProjectResource pmsProjectResource = pmsProjectResourceFacade.getProjectResourceById(id);

		InputStream in = FastDFSClient.downloadFile(pmsProjectResource.getResourcePath());

		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			String filename = URLEncoder.encode(pmsProjectResource.getResourceName(), "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"\r\n");
			// send file
			saveTo(in, ouputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void saveTo(InputStream in, OutputStream out) throws Exception {
		byte[] data = new byte[1024];
		int index = 0;
		while ((index = in.read(data)) != -1) {
			out.write(data, 0, index);
		}
		out.flush();
		in.close();
		out.close();
	}

	/**
	 * 数据导出
	 * @param view
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/project/export", method = RequestMethod.POST)
	public void export(final ProjectFlowView view, final HttpServletResponse response,
			final HttpServletRequest request) {

		SessionInfo sessionInfo = getCurrentInfo(request);
		final List<PmsProjectFlow> list = pmsProjectFlowFacade.getProjectFlowDetailByParam(JsonUtil.objectToMap(view));

		projectFlowService.exportProjectFlow(list,response,sessionInfo);	
	}
	
	/**
	 * 获取团队下拉值
	 * @return
	 */
	@RequestMapping(value="/project/member-pull")
	public Map<String,List<ActivitiMember>> getActivitiMemberData(){
		Map<String,List<ActivitiMember>> result=new HashMap<>();
		for(ProjectRoleType role:ProjectRoleType.values()){
			List<ActivitiMember> members=activitiMemberShipMapper.findByRole(role.getId());
			result.put(role.getId(), members);
		}
		return result;
	}
	
	/**
	 * 获取项目角色列表
	 * @return
	 */
	@RequestMapping("/project/groups")
	public List<Map<String, Object>> findProjectRoles() {
		// 处理数据字典
		List<Map<String, Object>> emlist = new ArrayList<Map<String, Object>>();
		List<String> exclude=new ArrayList<>();
		exclude.add("assignee");
		exclude.add("customer");
		exclude.add("teamPlan");
		exclude.add("teamProduct");
		for (ProjectRoleType role : ProjectRoleType.values()) {
			if(!exclude.contains(role.getId())){
				Map<String, Object> map = new HashMap<>();
				map.put("value",role.getId());
				map.put("text", role.getText());
				emlist.add(map);
			}
		}
		return emlist;
	}
	
	/**
	 * 获取用户项目角色
	 * @param userId
	 * @return
	 */
	@RequestMapping("/project/userGroup/{userId}")
	public ActivitiMember getProjectUserRole(@PathVariable("userId") String userId){
		List<ActivitiMember> roles=activitiMemberShipMapper.findByUserId("employee_"+userId);
		if(ValidateUtil.isValid(roles)){
			return roles.get(0);
		}
		return null;
	}
	
	/**
	 * 修改项目协同人
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/project-synergy/update")
	public BaseMsg updateProjectSynergy(final HttpServletRequest request, final HttpServletResponse response,ModelMap model){
		BaseMsg result=new BaseMsg(BaseMsg.NORMAL, "修改成功", null);
		projectFlowService.updateProjectSynergy(request,result);
		return result;
	}
	
}
