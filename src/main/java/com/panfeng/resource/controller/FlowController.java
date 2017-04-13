package com.panfeng.resource.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsTree;
import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.FlowTemplate;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.NodesEvent;
import com.panfeng.service.ActivitiEngineService;
import com.panfeng.service.IndentActivitiService;
import com.panfeng.service.TaskChainService;
import com.panfeng.service.TemplateDataManage;
import com.panfeng.util.HttpUtil;

@RestController
public class FlowController extends BaseController {

	@Autowired
	private IndentActivitiService indentActivitiService;
	@Autowired
	private ActivitiEngineService activitiEngineService;
	@Autowired
	private TaskChainService taskChainService;
	@Autowired
	private TemplateDataManage templateDataManage;

	@RequestMapping("/add-view")
	public ModelAndView view(final ModelMap model) {
		return new ModelAndView("add-flow", model);
	}

	@RequestMapping("/getnodes")
	public List<ActivitiTask> getFlowNodes(@RequestBody final IndentProject indentProject) {
		return indentActivitiService.getNodes(indentProject);
	}

	@RequestMapping("/startProcess")
	public Boolean startProcess(@RequestBody final IndentProject indentProject, HttpServletRequest request) {
		SessionInfo currentInfo = getCurrentInfo(request);
		return indentActivitiService.startProcess(indentProject, currentInfo);
	}

	@RequestMapping(value = "/getCurrectTask", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public ActivitiTask getCurrectTask(@RequestBody final IndentProject indentProject) {
		return indentActivitiService.getCurrentTask(indentProject);
	}

	@RequestMapping(value = "/completeTask", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseMsg completeTask(@RequestBody final IndentProject indentProject, HttpServletRequest request) {
		String processId = indentActivitiService.getIndentCurrentFlowId(indentProject);
		Task task = activitiEngineService.getCurrentTask(processId);
		// ---------兼容性----------
		String key = task.getProcessDefinitionId();
		SessionInfo sessionInfo = getCurrentInfo(request);
		if (key != null && key.contains("IndentFlow")) {
			String res = indentActivitiService.completeTask(indentProject, processId, sessionInfo);
			return new BaseMsg(BaseMsg.NORMAL, "", res);
		}
		// -------------------------
		else {
			BaseMsg res = indentActivitiService.completeTask_2(indentProject, processId, sessionInfo);
			return res;
		}
	}

	@RequestMapping(value = "/verifyIntegrity", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseMsg verifyIntegrity(@RequestBody final IndentProject indentProject) {

		return indentActivitiService.verifyIntegrity(indentProject);
	}

	@RequestMapping("/getIndentFlows")
	public List<IndentFlow> getIndentFlows(@RequestBody final IndentProject indentProject) {

		return indentActivitiService.getIndentFlows(indentProject);
	}

	@RequestMapping("/suspendProcess")
	public boolean suspendProcess(@RequestBody final IndentProject indentProject) {

		return indentActivitiService.suspendProcess(indentProject, false);
	}

	@RequestMapping("/jumpPrevTask")
	public boolean jumpPrevTask(@RequestBody final IndentProject indentProject) {
		return indentActivitiService.jumpPrevTask(indentProject);
	}

	@RequestMapping("/resumeProcess")
	public boolean resumeProcess(@RequestBody final IndentProject indentProject) {
		return indentActivitiService.resumeProcess(indentProject, false);
	}

	// -------------------------------------new----------------------------------------
	@RequestMapping("/flowTemplateView")
	public ModelAndView flowView() {

		return new ModelAndView("flowTemplate");
	}

	@RequestMapping("/flow/test")
	public ModelAndView flowTestView() {

		return new ModelAndView("test");
	}

	@RequestMapping("/flow/startProcess")
	public BaseMsg startProcess_2(@RequestBody FlowTemplate flowTemplate, HttpServletRequest request) {
		//SessionInfo currentInfo = getCurrentInfo(request);
		String processId = activitiEngineService.startProcess(flowTemplate.getId(), null);
		return processId == null ? new BaseMsg(BaseMsg.ERROR, "启动失败", null)
				: new BaseMsg(BaseMsg.NORMAL, "", processId);
	}

	@RequestMapping(value = "/flow/prevTask", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseMsg prevTask(HttpServletRequest request, String processId) {
		SessionInfo sessionInfo = getCurrentInfo(request);
		activitiEngineService.prevTask(sessionInfo, processId);
		return new BaseMsg(BaseMsg.NORMAL, "", null);
	}

	@RequestMapping(value = "/delete/flow", method = RequestMethod.POST)
	public BaseMsg deleteFlow(HttpServletRequest request, String processId) {

		activitiEngineService.removeProcess(processId);
		return new BaseMsg(BaseMsg.NORMAL, "", null);
	}

	@RequestMapping("/all/template")
	public List<FlowTemplate> getAllTemplate() {
		List<FlowTemplate> templates = activitiEngineService.getAllTemplate();
		return templates;
	}

	@RequestMapping("/get/image/{deployment_id}")
	public void getImage(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("deployment_id") String deployment_id) {
		try {
			InputStream inputStream = activitiEngineService.getImage(deployment_id);
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("image/png");
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + ((int) (Math.random() * 100000)) + ".png\"\r\n");
			response.setContentLength(inputStream.available());
			ServletOutputStream ouputStream = response.getOutputStream();
			// send file
			HttpUtil.saveTo(inputStream, ouputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/get/processimage")
	public void getProcessImage(HttpServletRequest request, HttpServletResponse response, String processId) {
		try {
			InputStream inputStream = activitiEngineService.getProcessImage(processId);
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("image/png");
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + ((int) (Math.random() * 100000)) + ".png\"\r\n");
			response.setContentLength(inputStream.available());
			ServletOutputStream ouputStream = response.getOutputStream();
			// send file
			HttpUtil.saveTo(inputStream, ouputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/put/template")
	public BaseMsg updateTeamplate(@RequestBody FlowTemplate flowTemplate) {
		if (ValidateUtil.isValid(flowTemplate.getId()) && ValidateUtil.isValid(flowTemplate.getName())
				&& flowTemplate.getFlowNodes().size() > 0) {
			return activitiEngineService.createFlowTemplate(flowTemplate);
		} else {
			return new BaseMsg(BaseMsg.ERROR, "模板创建失败！", false);
		}
	}

	@RequestMapping("/get/template")
	public FlowTemplate getTeamplate(@RequestBody FlowTemplate flowTemplate) {
		FlowTemplate flowTemplate2 = activitiEngineService.getTemplate(flowTemplate.getD_id());
		return flowTemplate2;
	}

	/////////////////////////////////////////////////////// 流程节点处理相关、、、、、、、、、、、、、、、、、、、、、、、、、、

	@RequestMapping("/nodesEvent-view")
	public ModelAndView nodeEventView() {
		return new ModelAndView("events-view");
	}

	@RequestMapping("/get/events")
	public List<NodesEvent> allEvents() {
		return taskChainService.getAllEvents();
	};

	@RequestMapping("/get/event/tree")
	public List<PmsTree> getEventTree() {
		return taskChainService.getEventTree();
	}

	@RequestMapping("/put/events")
	public BaseMsg updateEvent(@RequestBody NodesEvent nodesEvent) {
		long res = taskChainService.updateEvent(nodesEvent);
		if (res > 0)
			return new BaseMsg(BaseMsg.NORMAL, "", true);
		else
			return new BaseMsg(BaseMsg.ERROR, "", false);
	}

	@RequestMapping("/post/events")
	public BaseMsg addEvent(@RequestBody NodesEvent nodesEvent) {
		long res = taskChainService.addEvent(nodesEvent);
		if (res > 0)
			return new BaseMsg(BaseMsg.NORMAL, "", true);
		else
			return new BaseMsg(BaseMsg.ERROR, "", false);
	}

	@RequestMapping("/delete/events")
	public BaseMsg addEvent(String eventIds) {
		String[] ids = eventIds.split("\\,");
		for (String id : ids) {
			taskChainService.deleteEvent(Long.parseLong(id));
		}
		return new BaseMsg(BaseMsg.NORMAL, "", true);
	}
	// ------------------------------------模板数据填充处理部分----------------------------------

	@RequestMapping("/get/datatemplates")
	public List<PmsTree> getDataList() {
		List<PmsTree> dataList = templateDataManage.getDataList();
		return dataList;
	}

	@RequestMapping("/get/eventHandles")
	public List<PmsTree> getEventHandle() {
		List<PmsTree> dataList = templateDataManage.getEventHandle();
		return dataList;
	}

	@RequestMapping("/get/fields")
	public List<PmsTree> getfield(String templateDataKey) {
		List<PmsTree> fields = templateDataManage.optionalFields(templateDataKey);
		return fields;
	}

	@RequestMapping("/get/personnel")
	public List<PmsTree> personnel(String templateDataKey) {
		List<PmsTree> personnel = templateDataManage.personnel(templateDataKey);
		return personnel;
	}

	@RequestMapping("/get/template/tree")
	public List<PmsTree> contentTemplateTree() {
		return taskChainService.contentTemplateTree();
	}

	// -------------------------------------------test------------------------------------------

}
