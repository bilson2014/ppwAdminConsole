package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.News;
import com.panfeng.resource.view.NewsView;

public interface NewsService {

	List<News> listWithPagination(NewsView newsView);

	long maxSize(NewsView newsView);

	void save(News news);

	void update(News news);

	long delete(int[] ids);

	boolean moveUp(int newid);

	boolean moveDown(int newid);

	List<News> RecommendNews();

	News info(Integer newId);

	List<News> searchAllNews();
	
	News getNext( Integer newId);

	News getPrev( Integer newId);

}
