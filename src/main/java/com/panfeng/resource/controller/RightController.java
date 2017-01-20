package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Right;
import com.panfeng.resource.model.Tree;
import com.panfeng.resource.view.RightView;
import com.panfeng.service.RightService;

/**
 * 权限相关
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class RightController extends BaseController {

	@Autowired
	private final RightService service = null;
	
	@RequestMapping("/right-list")
	public ModelAndView view(){
		
		return new ModelAndView("right-list");
	}
	
	@RequestMapping("/right/tree")
	public List<Tree> rightTree(){
		
		final List<Tree> list = service.resourceTree();
		return list;
	}
	
	@RequestMapping("/right/list")
	public List<Right> list(final RightView view){
		
		List<Right> list = service.all();
		return list;
	}
	
	@RequestMapping(value = "/right/save",method= RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public long save(@RequestBody final Right right){
		
		final long ret = service.save(right);
		return ret;
	}
	
	@RequestMapping(value = "/right/update",method= RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public long update(@RequestBody final Right right){
		
		final long ret = service.update(right);
		return ret;
	}
	
	@RequestMapping("/right/delete")
	public long delete(final long[] ids){
		
		final long ret = service.delete(ids);
		return ret;
	}
	
	@RequestMapping("/right/menu")
	public List<Tree> menu(final HttpServletRequest request){
		
		//final SessionInfo info = (SessionInfo) sessionService.getSessionWithField(request, GlobalConstant.SESSION_INFO);
		final SessionInfo info = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		
		if(info != null){
			if(GlobalConstant.ROLE_EMPLOYEE.equals(info.getSessionType())){
				final List<Tree> list = service.menu(request);
				return list;
			}
		}
		
		return null;
	}
}
