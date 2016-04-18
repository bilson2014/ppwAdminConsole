package com.panfeng.interceptor;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.dao.RightDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Right;
import com.panfeng.service.SessionInfoService;
import com.panfeng.util.UrlResourceUtils;
import com.panfeng.util.ValidateUtil;

/**
 * 登陆拦截器
 * @author Jack
 *
 */
public class SecurityInterceptor implements HandlerInterceptor {

	@Autowired
	private final RightDao dao = null;
	
	@Autowired
	private final SessionInfoService sessionService = null;
	
	private List<String> excludeUrls;
	
	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	/**
	 * 完成页面rengder后调用
	 */
	public void afterCompletion(HttpServletRequest req,
			HttpServletResponse resp, Object arg2, Exception arg3)
			throws Exception {

	}

	/**
	 * 调用 controller 具体方法执行
	 */
	public void postHandle(HttpServletRequest req, HttpServletResponse resp,
			Object arg2, ModelAndView arg3) throws Exception {

	}

	/**
	 * 在调用 controller 具体方法前调用
	 */
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object arg2) throws Exception {
		
		final String url = req.getRequestURI();
		final ServletContext sc = req.getServletContext();
		final String uri = UrlResourceUtils.URLResolver(url, sc.getContextPath());
		
		if(excludeUrls.contains(uri)){
			return true;
		}
		
		final Right right = dao.getRightFromRedis(uri);
		final SessionInfo info = (SessionInfo) sessionService.getSessionWithField(req, GlobalConstant.SESSION_INFO); // 获取session
		
		// 首先验证是否是公共资源
		if(ValidateUtil.hasRight(uri, req, sc,right,resp,info)){
			// 公共资源
			return true;
		} else {
			// 非公共资源
			// 判断是否登陆
			// 从公共缓存中取出session
			if(info == null){
				// 未登录
				req.setAttribute("message", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
				req.getRequestDispatcher("/login").forward(req, resp);
				return false;
			}else {
				
				if(("/index").equals(uri)){
					return true;
				}
				
				if(ValidateUtil.hasRight(url, req, sc,right,resp,info)){
					return true;
				} else {
					// 没有权限
					req.setAttribute("message", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
					req.getRequestDispatcher("/error").forward(req, resp);
					return false;
				}
				
			}
		}
		
	}
	
}
