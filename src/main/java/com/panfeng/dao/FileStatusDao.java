package com.panfeng.dao;

import java.util.List;

	public interface FileStatusDao {
	
	public void save(final String key,final String field,final String value);
	
	public void update(final String key,final String field,final String content);
	
	public void delete(final String key,final String field);

	public List<String> find(final String key,final String... fields);
	
	public String find(final String key,final String field);
}