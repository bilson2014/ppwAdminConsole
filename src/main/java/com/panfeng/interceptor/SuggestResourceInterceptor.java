package com.panfeng.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.ResourceToken;
import com.panfeng.domain.SessionInfo;
import com.panfeng.service.SessionInfoService;

/**
 * 搜索提示资源拦截器
 * @author Administrator
 *
 */
public class SuggestResourceInterceptor implements HandlerInterceptor {

	@Autowired
	private final SessionInfoService sessionService = null; // 获取身份验证
	
	/**
	 * 在调用 controller 具体方法前调用
	 */
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {
		
		// 判断身份
		final SessionInfo info = (SessionInfo) sessionService.getSessionWithField(request, GlobalConstant.SESSION_INFO); // 获取session
		
		boolean flag = false;
		String solrUrl = null;
		// 如果是供应商和没有分类的用户，则只有首页推荐视频
		if(info != null){
			final String type =  info.getSessionType();
			if(GlobalConstant.ROLE_EMPLOYEE.equals(type)){
				// 如果是内部员工，则可以查询资源库
				flag = true;
				solrUrl = GlobalConstant.SOLR_EMPLOYEE_URL;
			}else if(GlobalConstant.ROLE_CUSTOMER.equals(type)){
				// 如果是用户，则判断是否定义级别
				if(info.getClientLevel() != null){
					// 用户已经分出级别，可以查询资源库
					flag = true;
					solrUrl = GlobalConstant.SOLR_URL;
				} else {
					// 用户未分级，不能访问资源库，只能访问首页推荐视频
					flag = false;
					solrUrl = GlobalConstant.SOLR_PORTAL_URL;
				}
			}else if(GlobalConstant.ROLE_PROVIDER.equals(type)){
				// 通过审核的供应商可以访问资源库
				if(info.isIdentification()){
					flag = true;
					solrUrl = GlobalConstant.SOLR_URL;
				}else {
					// 未通过审核的供应商不能访问资源库
					flag = false;
					solrUrl = GlobalConstant.SOLR_PORTAL_URL;
				}
			}
		} else {
			// 未登录不能访问资源库，只能访问首页推荐视频
			flag = false;
			solrUrl = GlobalConstant.SOLR_PORTAL_URL;
		}
		
		final ResourceToken token = new ResourceToken();
		token.setFlag(flag);
		token.setSolrUrl(solrUrl);
		request.setAttribute("searchResourceToken", token);
		return true;
	}
	
	/**
	 * 调用 controller 具体方法执行
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView mv) throws Exception {
		
	}

	/**
	 * 完成页面rengder后调用
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
