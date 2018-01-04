package com.panfeng.resource.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public ModelAndView loginView(final HttpServletRequest request,
			final HttpServletResponse response){
		//转http
		Cookie cookie = new Cookie("JSESSIONID", request.getSession().getId());   
		response.addCookie(cookie); 
		
		Cookie[] ck = request.getCookies();  
	    System.out.println("Cookie[] ck = request.getCookies() cookie is null"+(null==ck));
	    if(null!=ck && ck.length>0){
	    	System.out.println(ck[0].getName()+":"+ck[0].getValue());
	    }
	    
		
		return new ModelAndView("login");
	}
	
	@RequestMapping("/error")
	public ModelAndView errorView(){
		
		return new ModelAndView("error");
	}
}
