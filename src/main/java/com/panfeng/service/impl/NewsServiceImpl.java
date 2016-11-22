package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.panfeng.persist.NewsMapper;
import com.panfeng.resource.model.News;
import com.panfeng.resource.view.Pagination;
import com.panfeng.service.NewsService;


@Service
@Transactional
public class NewsServiceImpl implements NewsService{

	@Autowired
	private NewsMapper mapper;
	@Override
	public List<News> listWithPagination(Pagination pagination) {
		return mapper.listWithPagination(pagination);
	}

	@Override
	public long maxSize(Pagination pagination) {
		return mapper.maxSize(pagination);
	}

	@Override
	public void save(News news) {
		int maxIndex = mapper.getMaxIndex();
		news.setSortIndex(maxIndex+1);
		mapper.save(news);
	}

	@Override
	public void update(News news) {
		mapper.update(news);
	}
	@Override
	public long delete(int[] ids) {
		//1.删除新闻
		//2.提升排序
		if(ids.length>0){
			for(int id : ids){
				News n = mapper.getNewsById(id);
				mapper.delete(id);
				//提高大于n的所有数据的排序
				mapper.promoteAllAboveByIndex(n.getSortIndex());
			}
			return 1l;
		}
		return 0l;
		
	}

	@Override
	public boolean moveUp(int newid) {
		News news = mapper.getNewsById(newid);
		int index = news.getSortIndex();
		//1.降低上一个的排序
		int flag1 = mapper.downSortBySort(index);
		//2.提升当前id的排序
		int flag2 =mapper.upSortByNewsId(newid);
		return flag1>0 && flag2>0;
	}

	@Override
	public boolean moveDown(int newid) {
		News news = mapper.getNewsById(newid);
		int index = news.getSortIndex();
		//1.提升上一个的排序
		int flag1 = mapper.upSortBySort(index);
		//2.降低当前id的排序
		int flag2 =mapper.downSortByNewsId(newid);
		return flag1>0 && flag2>0;
	}

	@Override
	public List<News> RecommendNews() {
		return mapper.RecommendNews();
	}

	@Override
	public News info(Integer newId) {
		return mapper.info(newId);
	}

}
