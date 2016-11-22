package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.News;
import com.panfeng.resource.view.Pagination;

public interface NewsService {

	List<News> listWithPagination(Pagination pagination);

	long maxSize(Pagination pagination);

	void save(News news);

	void update(News news);

	long delete(int[] ids);

	boolean moveUp(int newid);

	boolean moveDown(int newid);

	List<News> RecommendNews();

	News info(Integer newId);

}
