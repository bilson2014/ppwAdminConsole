package com.panfeng.service;

import java.util.List;

public interface FileStatusService {
	
	/**
	 * 保存Map格式
	 * @param key 唯一标识
	 * @param field 字段
	 * @param value 内容
	 */
	public void save(final String key,final String field,final String value);
	
	/**
	 * 更新
	 * @param key 唯一标识
	 * @param field 字段
	 * @param value 内容
	 */
	public void update(final String key,final String field,final String content);
	
	/**
	 * 删除
	 * @param key 唯一标识
	 * @param field 字段
	 */
	public void delete(final String key,final String field);
	
	/**
	 * 根据多个字段名查找
	 * @param key 唯一标识
	 * @param fields 字段名数组
	 * @return String
	 */
	public List<String> find(final String key,final String... fields);
	
	/**
	 * 根据单个字段名查找
	 * @param key 唯一标识
	 * @param field 字段名
	 * @return String
	 */
	
	public String find(final String key,final String field);
}
