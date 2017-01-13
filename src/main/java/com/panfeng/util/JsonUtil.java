/*package com.panfeng.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
	
	*//**
	 * 转换为json格式
	 * @param obj 对象
	 * @return json字符串
	 *//*
	public static String toJson(final Object obj){
		
		final Gson gson = new Gson();
		final String str = gson.toJson(obj);
		return str;
	}
	
	*//**
	 * 转换json为对象格式
	 * @param <T>
	 * @param json json字符串
	 * @return 对象
	 *//*
	public static <T> T toBean(final String json,final Class<T> clazz){
		T t = null;
		final Gson gson = new Gson();
		t = gson.fromJson(json, clazz);
		return t;
	}
	
	*//**
	 * 转换json为List数组
	 * @param json
	 * @param clazz
	 * @return List数组
	 *//*
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
	*//**
	 * list 对象转 list map
	 * @param <T>
	 *//*
	public static<T> List<Map<String, Object>> getValueListMap(List<T> list) {  
        List<Map<String, Object>> returnList = new ArrayList<>();
		for(T t : list){
        	 Map<String, Object> map = new HashMap<String, Object>(); 
        	// 获取f对象对应类中的所有属性域  
            Field[] fields = t.getClass().getDeclaredFields();  
            for (int i = 0, len = fields.length; i < len; i++) {  
                String varName = fields[i].getName();  
                try {  
                    // 获取原来的访问控制权限  
                    boolean accessFlag = fields[i].isAccessible();  
                    // 修改访问控制权限  
                    fields[i].setAccessible(true);  
                    // 获取在对象f中属性fields[i]对应的对象中的变量  
                    Object o = fields[i].get(t);  
                    if (o != null)  
                        map.put(varName, o.toString());  
                    // System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);  
                    // 恢复访问控制权限  
                    fields[i].setAccessible(accessFlag);  
                } catch (IllegalArgumentException ex) {  
                    ex.printStackTrace();  
                } catch (IllegalAccessException ex) {  
                    ex.printStackTrace();  
                }  
            }  
            returnList.add(map);
        }
		return returnList;
    }  
	
	*//**
	 * 对象转  map
	 *//*
	public static<T> Map<String, Object> getValueListMap(T t) {  
    	 Map<String, Object> map = new HashMap<String, Object>(); 
    	// 获取f对象对应类中的所有属性域  
        Field[] fields = t.getClass().getDeclaredFields();  
        for (int i = 0, len = fields.length; i < len; i++) {  
            String varName = fields[i].getName();  
            try {  
                // 获取原来的访问控制权限  
                boolean accessFlag = fields[i].isAccessible();  
                // 修改访问控制权限  
                fields[i].setAccessible(true);  
                // 获取在对象f中属性fields[i]对应的对象中的变量  
                Object o = fields[i].get(t);  
                if (o != null)  
                    map.put(varName, o.toString());  
                // 恢复访问控制权限  
                fields[i].setAccessible(accessFlag);  
            } catch (IllegalArgumentException ex) {  
                ex.printStackTrace();  
            } catch (IllegalAccessException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return map;
    }  
}
*/