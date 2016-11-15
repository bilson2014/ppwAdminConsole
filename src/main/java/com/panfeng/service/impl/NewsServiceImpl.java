package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.NewsMapper;
import com.panfeng.resource.model.News;
import com.panfeng.resource.view.Pagination;
import com.panfeng.service.NewsService;

@Service
public class NewsServiceImpl implements NewsService{

	@Autowired
	private NewsMapper mapper;
	@Override
	public List<News> listWithPagination(Pagination pagination) {
		return mapper.listWithPagination(pagination);
	}

	@Override
	public long maxSize(Pagination pagination) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void save(News news) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(News news) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int[] ids) {
		// TODO Auto-generated method stub
		
	}

}
