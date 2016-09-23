package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.BizBean;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.IndentResource;
import com.panfeng.resource.model.Synergy;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.IndentProjectView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.IndentProjectService;
import com.panfeng.service.IndentResourceService;
import com.panfeng.util.Log;
import com.panfeng.util.ValidateUtil;

@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {
	@Autowired
	private IndentProjectService indentProjectService = null;

//	@Autowired
//	private IndentActivitiService activitiService = null;

	@Autowired
	private IndentResourceService resourceService = null;

	@RequestMapping("/save")
	public Boolean save(@RequestBody final IndentProject indentProject) {

		return indentProjectService.save(indentProject);
	}

	@RequestMapping("/flow-index")
	public ModelAndView view(final ModelMap model) {
		return new ModelAndView("flow");
	}

	@RequestMapping("/upadte-view")
	public ModelAndView updateview(final ModelMap model) {
		model.put("state", "update");
		return new ModelAndView("add-flow", model);
	}

	@RequestMapping("/all-project")
	public List<IndentProject> getUserAllProject(@RequestBody final IndentProject indentProject) {
		final List<IndentProject> list = indentProjectService.findProjectList(indentProject);
		return list;
	}
	@RequestMapping("/phone-project")
	public List<IndentProject> getPhoneProject(@RequestBody final IndentProject indentProject) {
		final List<IndentProject> list = indentProjectService.findProjectListByPhone(indentProject);
		return list;
	}

	@RequestMapping("/get-projectInfo")
	public IndentProject getProjectInfo(@RequestBody final IndentProject indentProject) {
		return indentProjectService.getProjectInfo(indentProject);
	}

	@RequestMapping("/get-redundantProject")
	public IndentProject getRedundantProject(@RequestBody final IndentProject indentProject) {
		final IndentProject project = indentProjectService.getRedundantProject(indentProject);
		return project;
	}

	@RequestMapping("/update-indentProject")
	public boolean updateIndentProject(@RequestBody final IndentProject indentProject,HttpServletRequest request) {
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update indentProject ...",sessionInfo);
		return indentProjectService.updateIndentProject(indentProject,false);
	}
	
	@RequestMapping("/update-synergyProject")
	public boolean updateSynergyProject(@RequestBody final IndentProject indentProject,HttpServletRequest request) {
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update indentProject ...",sessionInfo);
		return indentProjectService.updateIndentProject(indentProject,true);
	}

	@RequestMapping("/getProjectTags")
	public List<BizBean> getProjectTags() {
		return indentProjectService.getTags();
	}

	/**
	 * @param indentProject
	 * @return
	 */
	@RequestMapping("/cancelProject")
	public boolean cancelProject(@RequestBody final IndentProject indentProject,HttpServletRequest request) {
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("cancelProject ...",sessionInfo);
		return indentProjectService.cancelProject(indentProject);
	}

	@RequestMapping("/get/report")
	public void getReport(@RequestBody final IndentProject indentProject, final HttpServletResponse response) {
		try {
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			String dateString = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
			String filename = URLEncoder.encode("管家报表" + dateString + ".xlsx", "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"\r\n");
			OutputStream outputStream = response.getOutputStream();
			indentProjectService.getReport(indentProject, outputStream);
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 跳转项目列表
	@RequestMapping("/project-list")
	public ModelAndView view(final HttpServletRequest request) {

		return new ModelAndView("project-list");
	}

	// 跳转项目会议报表 -- 会议报表与项目列表功能一样，只是少了客户信息以及供应商信息
	@RequestMapping("/project-meeting")
	public ModelAndView meetingView(final HttpServletRequest request) {

		return new ModelAndView("project-meeting");
	}

	@RequestMapping("/list")
	public DataGrid<IndentProject> list(final IndentProjectView view, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);

		DataGrid<IndentProject> dataGrid = new DataGrid<IndentProject>();
		// add by wanglc 2016-7-11 13:46:29 接口限制视频管家与协同人的选择关系begin
		if (whetherJustChooseSynergy(view)) {// 搜索 只选择了是否协同,没有选择视频管家
			return dataGrid;
		}
		// add by wanglc 2016-7-11 13:46:29 接口限制视频管家与协同人的选择关系end
		final List<IndentProject> list = indentProjectService.listWithPagination(view);
		dataGrid.setRows(list);
		final long total = indentProjectService.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}

	private boolean whetherJustChooseSynergy(final IndentProjectView view) {
		boolean b = view.getUserId() == null && (view.getIsSynergy() != null && view.getIsSynergy() == 1);
		return b;
	}

	@RequestMapping("/update")
	public long update(@RequestBody final IndentProject project,HttpServletRequest request) {
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update IndentProject ...",sessionInfo);
		final long ret = indentProjectService.update(project);
		return ret;
	}

	@RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
	public long updateInfo(final IndentProject project, String user_name, String ratio, String synergyid,HttpServletRequest request) {
		// add by wanglc,2016-06-23 10:00 begin
		// -> 增加了3个参数,user_name,synergyid和ratio,用于后台修改
		List<Synergy> list = new ArrayList<Synergy>();
		String[] users = null;
		String[] ratios = null;
		String[] synergyids = null;
		if (ValidateUtil.isValid(user_name)) {
			users = user_name.split(",");
		}
		if (ValidateUtil.isValid(ratio)) {
			ratios = ratio.split(",");
		}

		if (ValidateUtil.isValid(synergyid)) {
			synergyids = synergyid.split(",");
		}

		if (ValidateUtil.isValid(users)) {
			for (int i = 0; i < users.length; i++) {
				Synergy s = new Synergy();
				if (ValidateUtil.isValid(ratios)) {
					s.setRatio(Double.parseDouble(ratios[i]));
				}
				if (ValidateUtil.isValid(synergyids)) {
					s.setSynergyId(Long.parseLong(synergyids[i]));
				}
				s.setUserId(Long.parseLong(users[i]));
				list.add(s);
			}
		}

		project.setSynergys(list);
		// add by wanglc,2016-06-23 10:30 end
//		if (project.getState() == 3) { // 暂停动作同时调用工作流引擎暂停
//			activitiService.suspendProcess(project,true);
//		}
//
//		// 如果之前项目状态为暂停，那么应该启动引擎
//		if (project.getState() == 0) {
//			final IndentProject originalProject = indentProjectService.getProjectInfo(project);
//			if (originalProject != null) {
//				if (originalProject.getState() == 3) { // 之前项目状态为3，那么恢复流程
//					activitiService.resumeProcess(project,true);
//				}
//			}
//		}

		final long ret = indentProjectService.update(project);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update IndentProject ...",sessionInfo);
		return ret;
	}

	/**
	 * 获取资源文件列表
	 * 
	 * @param indentProject
	 *            项目实体（包含ID）
	 * @return 资源列表文件
	 */
	@RequestMapping("/getIndentResourceList")
	public List<IndentResource> resouceList(final IndentProject indentProject) {

		final List<IndentResource> list = resourceService.findIndentList(indentProject);
		return list;
	}

	@RequestMapping("/saveInfo")
	public Boolean saveInfo(final IndentProject indentProject, String user_name, String ratio,HttpServletRequest request) {
		if(null!=user_name && null != ratio){
			List<Synergy> list = new ArrayList<Synergy>();
			String[] users = user_name.split(",");
			String[] ratios = ratio.split(",");
			for (int i = 0; i < users.length; i++) {
				Synergy s = new Synergy();
				s.setRatio(Double.parseDouble(ratios[i]));
				s.setUserId(Long.parseLong(users[i]));
				list.add(s);
			}
			indentProject.setSynergys(list);
		}
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save IndentProject ...",sessionInfo);
		return indentProjectService.save(indentProject);
	}

	@RequestMapping("/delete")
	public long delete(final long[] ids,HttpServletRequest request) {

		if (ValidateUtil.isValid(ids)) {
			for (final long id : ids) {
				final IndentProject project = new IndentProject();
				project.setId(id);
				project.setState(IndentProject.PROJECT_CANCEL);
				indentProjectService.cancelProject(project);
			}
		}
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete IndentProjects ...  ids:"+ids.toString(),sessionInfo);
		return 1l;
	}

	// 获取所有的供应商
	@RequestMapping("/getAllTeam")
	public List<IndentProject> allTeam() {

		final List<IndentProject> list = indentProjectService.getAllTeam();
		return list;
	}

	@RequestMapping("/getAllUser")
	public List<IndentProject> allUser() {

		final List<IndentProject> list = indentProjectService.getAllUser();
		return list;
	}

	@RequestMapping("/getAllVersionManager")
	public List<IndentProject> allManager() {

		final List<IndentProject> list = indentProjectService.getAllVersionManager();
		return list;
	}

	@RequestMapping("/getAllProject")
	public List<IndentProject> allProject() {

		final List<IndentProject> list = indentProjectService.getAllProject();
		return list;
	}

	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(final IndentProjectView view, final HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			String dateString = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
			String filename = URLEncoder.encode("项目列表" + dateString + ".xlsx", "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"\r\n");
			OutputStream outputStream = response.getOutputStream();
			// 获取所有的项目
			view.setBegin(0);
			view.setLimit(999999999l);
			List<IndentProject> list = indentProjectService.listWithPagination(view);
			indentProjectService.getReport(list, outputStream);
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/get/SerialID")
	public IndentProject getProjectSerialID() {

		final IndentProject project = new IndentProject();
		project.setSerial(indentProjectService.getProjectSerialID(null));
		return project;
	}

	/**
	 * 获取所有项目
	 * 
	 * @return list
	 */
	@RequestMapping("/all")
	public List<IndentProject> all() {
		final List<IndentProject> list = indentProjectService.all();
		return list;
	}

	@RequestMapping("/verifyProjectInfo")
	public BaseMsg verifyProjectInfo(@RequestBody IndentProject indentProject) {
		if (indentProject == null)
			return new BaseMsg(BaseMsg.ERROR, "错误", "项目信息不能为空");
		return indentProjectService.verifyProjectInfo(indentProject.getId());
	}
	// ------------------------------------------协同人处理部分------------------------------------------

	@RequestMapping("/remove/synergy")
	public boolean removeSynergy(@RequestBody final BizBean bizBean) {
		long ret = 0l;
		if (bizBean != null && bizBean.getName() != null) {
			ret = indentProjectService.removeSynergy(Long.parseLong(bizBean.getName()));
		}
		return ret > 0 ? true : false;
	}

	@RequestMapping("/get/synergys")
	public List<IndentProject> getSynergys(@RequestBody final IndentProject indentProject) {
		return indentProjectService.getSynergys(indentProject);
	}
	// -------------------------------- 验证流程资源完整度------------------------------
	
//	public boolean verifyIntegrity(){
//		
//		return true;
//	}
}
