package com.panfeng.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.panfeng.resource.model.Right;

/**
 * Redis 数据转换
 * @author Jack
 *
 */
public class RedisUtils {

	public static Map<String, String> toJson(final Map<String, Right> map) {
		
		final Map<String,String> objMap = new HashMap<String, String>();
		if(ValidateUtil.isValid(map)){
			for (Map.Entry<String, Right> entry : map.entrySet()) {
				final Right right = entry.getValue();
				// 转换为json
				final Gson gson = new Gson();
				final String str = gson.toJson(right);
				objMap.put(entry.getKey(), str);
			}
		}
		return objMap;
	}
	
	public static Map<String, String> mapToJson(final Map<String, Object> map) {
		
		final Map<String,String> objMap = new HashMap<String, String>();
		if(ValidateUtil.isValid(map)){
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				final Object t = entry.getValue();
				// 转换为json
				final Gson gson = new Gson();
				final String str = gson.toJson(t);
				objMap.put(entry.getKey(), str);
			}
		}
		return objMap;
	}

	public static <T> T fromJson(final String json ,final Class<T> clazz) {
		
		if(ValidateUtil.isValid(json)){
			T t = null;
			final Gson gson = new Gson();
			t = gson.fromJson(json, clazz);
			return t;
		}
		return null;
	}

	public static String toJson(final Object obj) {
		
		if(obj != null){
			final Gson gson = new Gson();
			final String str = gson.toJson(obj);
			return str;
		}
		return null;
	}

	public static Map<String, Right> fromJson(final Map<String, String> map) {
		
		if(ValidateUtil.isValid(map)){
			final Map<String,Right> rightMap = new HashMap<String, Right>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				final String key = entry.getKey();
				if(ValidateUtil.isValid(key)){
					final String str = entry.getValue();
					if(ValidateUtil.isValid(str)){
						final Gson gson = new Gson();
						final Right right = gson.fromJson(str, Right.class);
						rightMap.put(key, right);
					}
				}
			}
			return rightMap;
		}
		return null;
	}


}
