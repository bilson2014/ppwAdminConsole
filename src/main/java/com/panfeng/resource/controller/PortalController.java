package com.panfeng.resource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理入口
 * @author GY
 */
@RestController
public class PortalController extends BaseController {

	@RequestMapping("/login")
	public ModelAndView loginView(){
		
		return new ModelAndView("login");
	}
	
	@RequestMapping("/error")
	public ModelAndView errorView(){
		
		return new ModelAndView("error");
	}
}
