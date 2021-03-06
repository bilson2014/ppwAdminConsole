package com.panfeng.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.Constants;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.right.entity.PmsRight;
import com.panfeng.dao.DataCacheDao;
import com.panfeng.dao.RightDao;
import com.panfeng.dao.StorageLocateDao;
import com.panfeng.util.UrlResourceUtils;

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
	private DataCacheDao dataCacheDao;
	
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
			Object obj, ModelAndView mv) throws Exception {
		if(mv != null) {
			// 如果不为空，则说明进入视图解析器
			final Map<String, String> nodeMap = storageDao.getStorageFromRedis(PmsConstant.STORAGE_NODE_RELATIONSHIP);
			// 获取最优Storage节点
			final String serviceIP = FastDFSClient.locateSource();
			String ip = "";
			final StringBuffer sbf = new StringBuffer();
			sbf.append("https://");
			
			if(ValidateUtil.isValid(serviceIP)) {
				ip = nodeMap.get(serviceIP);
				if(ValidateUtil.isValid(ip)) {
					sbf.append(ip);
					sbf.append("/");
				} else {
					sbf.append(PublicConfig.FDFS_BACKUP_SERVER_PATH);
				}
			} else {
				sbf.append(PublicConfig.FDFS_BACKUP_SERVER_PATH);
			}
			mv.addObject(PmsConstant.FILE_LOCATE_STORAGE_PATH, sbf.toString());
		}
		
		HttpSession session=req.getSession();
		final SessionInfo info = (SessionInfo) session.getAttribute(PmsConstant.SESSION_INFO);
		
		if(info!=null && info.getCacheTab()!=null && info.getCacheTab()>0) {
			try {
				dataCacheDao.setExpire(session.getId()+PmsConstant.CACHE_KEYNAME, session.getMaxInactiveInterval());
			} catch (Exception e) {
			}
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
		
		final PmsRight right = dao.getRightFromRedis(uri);
		final SessionInfo info = (SessionInfo) req.getSession().getAttribute(PmsConstant.SESSION_INFO);
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
				if("apaipian.com".equals(req.getServerName()) || "www.apaipian.com".equals(req.getServerName())){//生产管理端或者上一访问为https?此时服务的接收不到cookie
					resp.sendRedirect("https://"+req.getServerName()+":7071"+"/login");
				}else{
					req.getRequestDispatcher("/login").forward(req, resp);
				}
				
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
					HttpSession session = request.getSession();
					info = (SessionInfo) session.getAttribute(PmsConstant.SESSION_INFO);
					if(info != null){
						//将info重新放入session redis键改为当前sessionId
						info.setToken(com.panfeng.util.DataUtil.md5(request.getSession().getId()));
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(PmsConstant.SESSION_INFO, info);
						
						Cookie cookieUsername = new Cookie("token", request.getSession().getId());
						cookieUsername.setPath("/");
						cookieUsername.setDomain(Constants.COOKIES_SCOPE);
						cookieUsername.setMaxAge(60 * 60 * 24 * 7); /* 设置cookie的有效期为 7 天 */
						response.addCookie(cookieUsername);
						session.removeAttribute("PmsConstant.SESSION_INFO");
					}
				}
			}
		}
		return info;
	}
	
}
