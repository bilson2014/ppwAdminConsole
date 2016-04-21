package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.IndentProject;
import com.panfeng.service.IndentProjectService;

@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {
	@Autowired
	private IndentProjectService indentProjectService;

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
	public String[] getProjectTags() {
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
}
