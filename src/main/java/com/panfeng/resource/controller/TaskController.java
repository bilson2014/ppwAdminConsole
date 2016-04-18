package com.panfeng.resource.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TaskController extends BaseController {

	@RequestMapping(value = "/getTaskView/{taskName}/{phases}")
	public ModelAndView getTaskView(@PathVariable String taskName,
			@PathVariable String phases, final ModelMap model) {
		String taskKey = taskName;
		
		
		return new ModelAndView(taskKey, model);
		/**
		 * --> 提交taskkey 和用户身份  task 自动识别分析处理相关业务
		 */
	}
}
