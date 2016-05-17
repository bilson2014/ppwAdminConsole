package com.panfeng.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
	
	/**
	 * 转换为json格式
	 * @param obj 对象
	 * @return json字符串
	 */
	public static String toJson(final Object obj){
		
		final Gson gson = new Gson();
		final String str = gson.toJson(obj);
		return str;
	}
	
	/**
	 * 转换json为对象格式
	 * @param <T>
	 * @param json json字符串
	 * @return 对象
	 */
	public static <T> T toBean(final String json,final Class<T> clazz){
		T t = null;
		final Gson gson = new Gson();
		t = gson.fromJson(json, clazz);
		return t;
	}
	
	/**
	 * 转换json为List数组
	 * @param json
	 * @param clazz
	 * @return List数组
	 */
	public static <T> List<T> toList(final String json){
		
		List<T> list = new ArrayList<T>();
		
		final Gson gson = new Gson();
		list = gson.fromJson(json, new TypeToken<List<T>>() {}.getType());
		return list;
	}

	// 前台处理
	public static<T> List<T> fromJsonArray(final String json,Class<T> clazz) throws Exception{
		
		List<T> list = new ArrayList<T>();
		
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for (final JsonElement jsonElement : array) {
			list.add(new Gson().fromJson(jsonElement, clazz));
		}
		
		return list;
	}
}
