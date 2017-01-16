package com.panfeng.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.dao.RightDao;
import com.panfeng.dao.StorageLocateDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Right;
import com.panfeng.service.FDFSService;
import com.panfeng.service.SessionInfoService;
import com.panfeng.util.Constants;
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
	private final StorageLocateDao storageDao = null;
	
	@Autowired
	private final SessionInfoService sessionService = null;
	
	private List<String> excludeUrls;
	
	@Autowired
	public final FDFSService fdfsService = null;
	
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
			Object obj, ModelAndView mv) throws Exception {
		if(mv != null) {
			// 如果不为空，则说明进入视图解析器
			final Map<String, String> nodeMap = storageDao.getStorageFromRedis(GlobalConstant.STORAGE_NODE_RELATIONSHIP);
			// 获取最优Storage节点
			final String serviceIP = fdfsService.locateFileStoragePath();
			String ip = "";
			final StringBuffer sbf = new StringBuffer();
			sbf.append("http://");
			
			if(ValidateUtil.isValid(serviceIP)) {
				ip = nodeMap.get(serviceIP);
				if(ValidateUtil.isValid(ip)) {
					sbf.append(ip);
					sbf.append(":8888/");
				} else {
					sbf.append(GlobalConstant.FDFS_BACKUP_SERVER_PATH);
				}
			} else {
				sbf.append(GlobalConstant.FDFS_BACKUP_SERVER_PATH);
			}
			mv.addObject(GlobalConstant.FILE_LOCATE_STORAGE_PATH, sbf.toString());
		}
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
		SessionInfo info = (SessionInfo) sessionService.getSessionWithField(req, GlobalConstant.SESSION_INFO); // 获取session
		/*if(null == info){
			info = checkAutoLogin(req,resp);
		}*/
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

	private SessionInfo checkAutoLogin(HttpServletRequest request
			,HttpServletResponse response) {
		SessionInfo info = null;
		String token = null;
		Cookie[] cookie = request.getCookies();
		if(cookie!=null){
			if(cookie.length>0){
				for (Cookie c : cookie) {
					if ("token".equals(c.getName())) {
						token = c.getValue();
					}
				}
				if(null!=token){
					info = (SessionInfo) sessionService.getSessionInfoWithToken(request,token);
					if(info != null){
						//将info重新放入session redis键改为当前sessionId
						info.setToken(com.panfeng.util.DataUtil.md5(request.getSession().getId()));
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(GlobalConstant.SESSION_INFO, info);
						sessionService.addSessionSeveralTime(request, map,60*60*24*7);//登陆用户存放七天
						
						Cookie cookieUsername = new Cookie("token", request.getSession().getId());
						cookieUsername.setPath("/");
						cookieUsername.setDomain(Constants.COOKIES_SCOPE);
						cookieUsername.setMaxAge(60 * 60 * 24 * 7); /* 设置cookie的有效期为 7 天 */
						response.addCookie(cookieUsername);
						sessionService.removeSessionByToken(request,token);
					}
				}
			}
		}
		return info;
	}
	
}
