package com.panfeng.util;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Right;

public class ValidateUtil {
	
	public static boolean isValid(final String str){
		
		if(str !=null && !"".equals(str)){
			
			return true;
		}
		return false;
	}
	
	public static boolean isValid(final String[] str){
		
		if(str != null && str.length > 0){
			
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isValid(final Collection col){
		
		if(col == null || col.isEmpty()){
			return false;
		}
		
		return true;
	}

	/**
	 * 判断是否有权限
	 */
	public static boolean hasRight(final String uri, final HttpServletRequest req,
			final ServletContext sc,final Right right,final HttpServletResponse resp,final SessionInfo info) {
		
		if(isValid(uri)){
			// 从redis中获取right
			
			if(right != null){
				// 判断权限是否是公用的
				if(right.getIsCommon()){
					// 公共资源放行
					return true;
				}else {
					if(info != null){
						// 判断用户是否是超级管理员
						if(info.isSuperAdmin()){
							// 超级管理员放行
							return true;
						} else {
							// 非超级管理员 -- 判断是否具有该权限
							return info.hasRight(right);
		 				}
					}else {
						// 未登录
						return false;
					}
				}
			}
		}
		
		return false;
	}

	public static boolean isValid(final long[] ids) {
		 
		if(ids != null && ids.length > 0){
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isValid(Map map) {
		
		if(map != null && !map.isEmpty()){
			return true;
		}
		return false;
	}
	
}
