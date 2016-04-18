package com.panfeng.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.service.IndentActivitiService;
import com.panfeng.service.IndentCommentService;
import com.panfeng.service.IndentResourceService;

@RestController
public class FlowController extends BaseController {

	@Autowired
	private IndentActivitiService indentActivitiService;
	@Autowired
	private IndentResourceService indentResourceService;
	@Autowired
	private IndentCommentService indentCommentService;

	@RequestMapping("/add-view")
	public ModelAndView view(final ModelMap model) {
		return new ModelAndView("add-flow", model);
	}

	@RequestMapping("/getnodes")
	public List<ActivitiTask> getFlowNodes(
			@RequestBody final IndentProject indentProject) {
		return indentActivitiService.getNodes(indentProject);
	}

	@RequestMapping("/startProcess")
	public Boolean startProcess(@RequestBody final IndentProject indentProject) {
		return indentActivitiService.startProcess(indentProject);
	}

	@RequestMapping(value = "getCurrectTask", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public ActivitiTask getCurrectTask(
			@RequestBody final IndentProject indentProject) {
		return indentActivitiService.getCurrentTask(indentProject);
	}

	@RequestMapping("/completeTask")
	public boolean completeTask(@RequestBody final IndentProject indentProject) {
		return indentActivitiService.completeTask(indentProject);
	}

	@RequestMapping("/getIndentFlows")
	public List<IndentFlow> getIndentFlows(
			@RequestBody final IndentProject indentProject) {

		return indentActivitiService.getIndentFlows(indentProject);
	}
	
	@RequestMapping("/suspendProcess")
	public boolean suspendProcess(
			@RequestBody final IndentProject indentProject) {
		return indentActivitiService.suspendProcess(indentProject);
	}
	
	@RequestMapping("/resumeProcess")
	public boolean resumeProcess(
			@RequestBody final IndentProject indentProject) {
		return indentActivitiService.resumeProcess(indentProject);
	}
	
//	@RequestMapping("/removeProcess")
//	public boolean removeProcess(
//			@RequestBody final IndentProject indentProject) {
//		return indentActivitiService.removeProcess(indentProject);
//	}

}
