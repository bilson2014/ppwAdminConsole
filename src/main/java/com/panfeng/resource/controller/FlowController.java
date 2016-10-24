package com.panfeng.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.service.IndentActivitiService;

@RestController
public class FlowController extends BaseController {

	@Autowired
	private IndentActivitiService indentActivitiService;

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

	@RequestMapping(value = "/getCurrectTask", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public ActivitiTask getCurrectTask(
			@RequestBody final IndentProject indentProject) {
		return indentActivitiService.getCurrentTask(indentProject);
	}

	@RequestMapping(value = "/completeTask" ,method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseMsg completeTask(@RequestBody final IndentProject indentProject) {
		String res =  indentActivitiService.completeTask(indentProject);
		return new BaseMsg(BaseMsg.ERROR,"",res);
	}

	@RequestMapping("/getIndentFlows")
	public List<IndentFlow> getIndentFlows(
			@RequestBody final IndentProject indentProject) {

		return indentActivitiService.getIndentFlows(indentProject);
	}
	
	@RequestMapping("/suspendProcess")
	public boolean suspendProcess(
			@RequestBody final IndentProject indentProject) {
		return indentActivitiService.suspendProcess(indentProject,false);
	}
	
	@RequestMapping("/jumpPrevTask")
	public boolean jumpPrevTask(
			@RequestBody final IndentProject indentProject) {
		return indentActivitiService.jumpPrevTask(indentProject);
	}
	@RequestMapping("/resumeProcess")
	public boolean resumeProcess(
			@RequestBody final IndentProject indentProject) {
		return indentActivitiService.resumeProcess(indentProject,false);
	}
	
//	@RequestMapping("/removeProcess")
//	public boolean removeProcess(
//			@RequestBody final IndentProject indentProject) {
//		return indentActivitiService.removeProcess(indentProject);
//	}

}
