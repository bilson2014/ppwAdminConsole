package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.dao.FileStatusDao;
import com.panfeng.service.FileStatusService;

@Service
public class FileStatusServiceImpl implements FileStatusService {

	@Autowired
	final FileStatusDao dao = null;
	
	public void save(final String key, final String field,final String value) {
		
		dao.save(key, field, value);
	}

	public void update( final String key,final String field,
			final String content) {
		
		dao.update(key, field, content);
	}


	public void delete(final String key,final String field) {
		
		dao.delete(key, field);
	}

	public List<String> find(final String key,
			final String... fields) {
		
		final List<String> list = dao.find(key, fields);
		return list;
	}

	public String find( final String key,final String field) {
		
		final String str = dao.find(key, field);
		return str;
	}

	

}
