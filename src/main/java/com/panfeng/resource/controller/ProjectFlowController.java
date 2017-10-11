package com.panfeng.resource.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.enums.FileType;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.finance.entity.PmsDealLog;
import com.paipianwang.pat.facade.finance.service.PmsFinanceFacade;
import com.paipianwang.pat.facade.indent.entity.IndentSource;
import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.entity.PmsTree;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.entity.PmsProjectResource;
import com.paipianwang.pat.workflow.entity.ProjectColumn;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectGroupColumnShipFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectGroupColumnUpdateShipFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectGroupResourceUpdateFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectResourceFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectResourceRightFacade;
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
//	@Autowired
//	private ActivitiUserMapper activitiUserMapper;
	@Autowired
	private ActivitiMemberShipMapper activitiMemberShipMapper;
	@Autowired
	private PmsProjectGroupColumnShipFacade pmsProjectGroupColumnShipFacade;
	@Autowired
	private PmsProjectGroupColumnUpdateShipFacade pmsProjectGroupColumnUpdateShipFacade;
	@Autowired
	private PmsProjectGroupResourceUpdateFacade pmsProjectGroupResourceUpdateFacade;
	@Autowired
	private PmsProjectResourceRightFacade pmsProjectResourceRightFacade;
	

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
		SessionInfo sessionInfo=getCurrentInfo(request);
		projectFlowService.updateProjectSynergy(request,result,sessionInfo);
		return result;
	}
	
	/**
	 * 获取项目角色权限树
	 * @return
	 */
	@RequestMapping("/project-right/tree")
	public List<PmsTree> getProjectRightTree(){
		//-----------构造权限树
		List<PmsTree> result=new ArrayList<>();
		//项目信息查看
		PmsTree columnRight=new PmsTree();
		columnRight.setId(ProjectColumn.COLUMN_VIEW_RIGHT);
		columnRight.setText("项目信息查看");
		columnRight.setChildren(getColumnChildren(columnRight.getId()));
		result.add(columnRight);
		//项目信息修改
		PmsTree columnEditRight=new PmsTree();
		columnEditRight.setId(ProjectColumn.COLUMN_EDIT_RIGHT);
		columnEditRight.setText("项目信息修改");
		columnEditRight.setChildren(getColumnChildren(columnEditRight.getId()));
		result.add(columnEditRight);
		//项目文件查看
		PmsTree resourceRight=new PmsTree();
		resourceRight.setId(ProjectColumn.FILE_VIEW_RIGHT);
		resourceRight.setText("项目文件查看");
		resourceRight.setChildren(getFileChildren(resourceRight.getId()));
		result.add(resourceRight);
		//项目文件更新
		PmsTree resourceEditRight=new PmsTree();
		resourceEditRight.setId(ProjectColumn.FILE_EDIT_RIGHT);
		resourceEditRight.setText("项目文件更新");
		resourceEditRight.setChildren(getFileChildren(resourceEditRight.getId()));
		
		result.add(resourceEditRight);
		return result;
	}
	private static String split="-";//tree父子id分隔符
	
	/**
	 * 获取项目角色权限：
	 * 		项目信息查看
	 * 		项目信息修改
	 * 		项目文件查看
	 * 		项目文件修改
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/project-right")
	public List<String> getProjectRoleRight(@RequestBody final PmsRole role,final HttpServletRequest request){
		List<Group> groups=new ArrayList<>();
		Group group = new GroupEntity();
		group.setId(role.getRoleValue());
		groups.add(group);
		
		//获取权限数据
		Map<String,List<String>> columnRights=pmsProjectGroupColumnShipFacade.getColumns(groups);
		Map<String,List<String>> columnUpdateRights=pmsProjectGroupColumnUpdateShipFacade.getColumns(groups);
		List<String> fileRights=pmsProjectResourceRightFacade.getResources(groups);
		List<String> fileUpdateRights=pmsProjectGroupResourceUpdateFacade.getResources(groups);
		
		//数据格式化：类别id+（表id）+权限节点id
		List<String> result=new ArrayList<>();
		
		Iterator<String> iterator=columnRights.keySet().iterator();
		while(iterator.hasNext()){
			String key=(String)iterator.next();
			List<String> values=columnRights.get(key);
			for(String value:values){
				result.add(ProjectColumn.COLUMN_VIEW_RIGHT+split+key+split+value);
			}
		}
		iterator=columnUpdateRights.keySet().iterator();
		while(iterator.hasNext()){
			String key=(String)iterator.next();
			List<String> values=columnUpdateRights.get(key);
			for(String value:values){
				result.add(ProjectColumn.COLUMN_EDIT_RIGHT+split+key+split+value);
			}
		}
		for(String fileRight:fileRights){
			result.add(ProjectColumn.FILE_VIEW_RIGHT+split+fileRight);
		}
		for(String fileRight:fileUpdateRights){
			result.add(ProjectColumn.FILE_EDIT_RIGHT+split+fileRight);
		}
		return result;
	}
	
	
	@RequestMapping("/project-right/grant")
	public String grantProjectRoleRight(final String roleId,final String[] resourceIds){
		Group group=new GroupEntity(roleId);
		Map<String,List<String>> columns=new HashMap<>();
		Map<String,List<String>> editColumns=new HashMap<>();
		List<String> resources=new ArrayList<>();
		List<String> editResources=new ArrayList<>();
		
		
		for(String resourceId:resourceIds){
			String[] ids=resourceId.split(split);
			switch (ids[0]) {
			case ProjectColumn.COLUMN_VIEW_RIGHT:{
				//信息查看权限
				if(!columns.containsKey(ids[1])){
					columns.put(ids[1],new ArrayList<String>());
				}
				columns.get(ids[1]).add(ids[2]);
				break;	
			}
			case ProjectColumn.COLUMN_EDIT_RIGHT:{
				//项目信息修改
				if(!editColumns.containsKey(ids[1])){
					editColumns.put(ids[1],new ArrayList<String>());
					//添加id
					if("PROJECT_USER".equals(ids[1])){
						editColumns.get(ids[1]).add("projectUserId");
					}else if("PROJECT_TEAM".equals(ids[1])){
						editColumns.get(ids[1]).add("projectTeamId");
					}
				}
				editColumns.get(ids[1]).add(ids[2]);
				break;
			}
			case ProjectColumn.FILE_VIEW_RIGHT:{
				//项目文件查看
				resources.add(ids[1]);
				break;
			}
			case ProjectColumn.FILE_EDIT_RIGHT:{
				//项目文件修改
				editResources.add(ids[1]);
				break;
			}

			default:
				break;
			}
		}
		
		pmsProjectGroupColumnShipFacade.updateColumnShip(group, columns);
		pmsProjectGroupColumnUpdateShipFacade.updateColumnShip(group, editColumns);
		pmsProjectResourceRightFacade.updateResourceShip(group, resources);
		pmsProjectGroupResourceUpdateFacade.updateResourceShip(group, editResources);
		
		return "";
	}
	
	private static List<PmsTree> getFileChildren(String rootId) {
		// 文件信息
		List<PmsTree> fileChildren = new ArrayList<>();
		for (FileType file : FileType.values()) {
			if (file.getType() == 1) {
				PmsTree fileItem = new PmsTree();
				fileItem.setId(rootId+split+file.getId());
				fileItem.setText(file.getText());
				fileChildren.add(fileItem);
			}
		}
		return fileChildren;
	}
	
	/*
	 * id规则：父id+当前节点id
	 * 		表级层id：表名
	 * 		字段级层id:字段名
	 */
	private static List<PmsTree> getColumnChildren(String rootId) {
		List<PmsTree> columnChildren = new ArrayList<>();
		ProjectColumn[] columnArray=ProjectColumn.getArrayInstance();
		if(columnArray!=null){
			for(ProjectColumn column:columnArray){
				PmsTree tableNode = new PmsTree();
				tableNode.setId(rootId+split+column.getTableKey());
				tableNode.setText(column.getTableName());
				//字段项叶子节点
				List<PmsTree> children = new ArrayList<>();
				tableNode.setChildren(children);
				for (ProjectColumn.ColumnItem item : column.getItems()) {
					PmsTree syItem = new PmsTree();
					syItem.setId(tableNode.getId()+split+item.getValue());
					syItem.setText(item.getText());
					children.add(syItem);
				}
				columnChildren.add(tableNode);
			}
		}
		return columnChildren;
	}
}
