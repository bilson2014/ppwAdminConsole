package com.panfeng.resource.controller;

import java.util.List;

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
	public boolean cancelProject(@RequestBody final IndentProject indentProject){
		return indentProjectService.cancelProject(indentProject);
	}
}
