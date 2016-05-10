package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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

import com.panfeng.resource.model.BizBean;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.IndentProjectView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.IndentProjectService;
import com.panfeng.util.ValidateUtil;

@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {
	@Autowired
	private IndentProjectService indentProjectService = null;
	
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
	public List<IndentProject> getUserAllProject(
			@RequestBody final IndentProject indentProject) {
		return indentProjectService.findProjectList(indentProject);
	}

	@RequestMapping("/get-projectInfo")
	public IndentProject getProjectInfo(
			@RequestBody final IndentProject indentProject) {
		return indentProjectService.getProjectInfo(indentProject);
	}

	@RequestMapping("/get-redundantProject")
	public IndentProject getRedundantProject(
			@RequestBody final IndentProject indentProject) {
		return indentProjectService.getRedundantProject(indentProject);
	}

	@RequestMapping("/update-indentProject")
	public boolean updateIndentProject(
			@RequestBody final IndentProject indentProject) {
		return indentProjectService.updateIndentProject(indentProject);
	}

	@RequestMapping("/getProjectTags")
	public List<BizBean> getProjectTags() {
		
		return indentProjectService.getTags();
	}

	@RequestMapping("/cancelProject")
	public boolean cancelProject(@RequestBody final IndentProject indentProject) {
		return indentProjectService.cancelProject(indentProject);
	}

	@RequestMapping("/get/report")
	public void getReport(@RequestBody final IndentProject indentProject,
			final HttpServletResponse response) {
		try {
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			String dateString=	DateUtils.formatDate(new Date(), "yyyy-MM-dd");
			String filename=URLEncoder.encode("管家报表"+dateString+".xlsx", "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+  filename+ "\"\r\n");
			OutputStream outputStream = response.getOutputStream();
			indentProjectService.getReport(indentProject, outputStream);
			if(outputStream!=null){
				outputStream.flush();
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 跳转项目列表
	@RequestMapping("/project-list")
	public ModelAndView view(final HttpServletRequest request){
		
		return new ModelAndView("project-list");
	}
	
	@RequestMapping("/list")
	public DataGrid<IndentProject> list(final IndentProjectView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		DataGrid<IndentProject> dataGrid = new DataGrid<IndentProject>();
		final List<IndentProject> list = indentProjectService.listWithPagination(view);
		dataGrid.setRows(list);
		
		final long total = indentProjectService.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping("/update")
	public long update(@RequestBody final IndentProject project){
		
		final long ret = indentProjectService.update(project);
		return ret;
	}
	
	@RequestMapping(value = "/updateInfo",method = RequestMethod.POST )
	public long updateInfo(final IndentProject project){
		
		final long ret = indentProjectService.update(project);
		return ret;
	}
	
	@RequestMapping("/saveInfo")
	public Boolean saveInfo(final IndentProject indentProject) {
		
		return indentProjectService.save(indentProject);
	}
	
	@RequestMapping("/delete")
	public long delete(final long[] ids){
		
		if(ValidateUtil.isValid(ids)){
			for (final long id : ids) {
				final IndentProject project = new IndentProject();
				project.setId(id);
				project.setState(IndentProject.PROJECT_CANCEL);
				indentProjectService.cancelProject(project);
			}
		}
		return 1l;
	}
	
	// 获取所有的供应商
	@RequestMapping("/getAllTeam")
	public List<IndentProject> allTeam(){
		
		final List<IndentProject> list = indentProjectService.getAllTeam();
		return list;
	}
	
	@RequestMapping("/getAllUser")
	public List<IndentProject> allUser(){
		
		final List<IndentProject> list = indentProjectService.getAllUser();
		return list;
	}
	
	@RequestMapping("/getAllVersionManager")
	public List<IndentProject> allManager(){
		
		final List<IndentProject> list = indentProjectService.getAllVersionManager();
		return list;
	}
	
	@RequestMapping("/getAllProject")
	public List<IndentProject> allProject(){
		
		final List<IndentProject> list = indentProjectService.getAllProject();
		return list;
	}
	
	@RequestMapping("/export")
	public void export(final IndentProjectView view ,final HttpServletResponse response){
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			String dateString=	DateUtils.formatDate(new Date(), "yyyy-MM-dd");
			String filename=URLEncoder.encode("项目列表"+dateString+".xlsx", "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+  filename+ "\"\r\n");
			OutputStream outputStream = response.getOutputStream();
			// 获取所有的项目 
			view.setBegin(0);
			view.setLimit(999999999l);
			List<IndentProject> list = indentProjectService.listWithPagination(view);
			indentProjectService.getReport(list, outputStream);
			if(outputStream!=null){
				outputStream.flush();
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("/get/SerialID")
	public IndentProject getProjectSerialID(){
		
		final IndentProject project = new IndentProject();
		project.setSerial(indentProjectService.getProjectSerialID());
		return project;
	}
	
}
