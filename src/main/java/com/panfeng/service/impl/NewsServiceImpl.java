package com.panfeng.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.NewsMapper;
import com.panfeng.resource.model.News;
import com.panfeng.resource.view.NewsView;
import com.panfeng.service.FDFSService;
import com.panfeng.service.NewsService;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsMapper mapper;

	@Autowired
	private final FDFSService fdfsService = null;

	@Override
	public List<News> listWithPagination(NewsView newsView) {
		return mapper.listWithPagination(newsView);
	}

	@Override
	public long maxSize(NewsView newsView) {
		return mapper.maxSize(newsView);
	}

	@Override
	public void save(News news) {
		int maxIndex = mapper.getMaxIndex();
		news.setSortIndex(maxIndex + 1);
		if (news.getRecommend() == null) {
			news.setRecommend(0);
		}
		mapper.save(news);
	}

	@Override
	public void update(News news) {
		if (news.getRecommend() == null) {
			news.setRecommend(0);
		}
		mapper.update(news);
	}

	@Override
	public long delete(int[] ids) {
		// 1.删除新闻
		// 2.提升排序
		if (ids.length > 0) {
			for (int id : ids) {
				News n = mapper.getNewsById(id);
				String picLDUrl = n.getPicLDUrl();
				if (StringUtils.isNotEmpty(picLDUrl)) {
					fdfsService.delete(picLDUrl);
				}
				mapper.delete(id);
				// 提高大于n的所有数据的排序
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
		// 1.降低上一个的排序
		int flag1 = mapper.downSortBySort(index);
		// 2.提升当前id的排序
		int flag2 = mapper.upSortByNewsId(newid);
		return flag1 > 0 && flag2 > 0;
	}

	@Override
	public boolean moveDown(int newid) {
		News news = mapper.getNewsById(newid);
		int index = news.getSortIndex();
		// 1.提升上一个的排序
		int flag1 = mapper.upSortBySort(index);
		// 2.降低当前id的排序
		int flag2 = mapper.downSortByNewsId(newid);
		return flag1 > 0 && flag2 > 0;
	}

	@Override
	public List<News> RecommendNews() {
		return mapper.RecommendNews();
	}

	@Override
	public News info(Integer newId) {
		News info = mapper.info(newId);
		if (info != null) {
			info = adjustMorePage(info);
		}
		return info;
	}

	@Override
	public List<News> searchAllNews() {
		return mapper.searchAllNews();
	}

	@Override
	public News getNext(Integer newId) {

		News next = mapper.getNext(newId);
		if (next != null) {
			next = adjustMorePage(next);
		}
		return next;
	}

	@Override
	public News getPrev(Integer newId) {

		News prev = mapper.getPrev(newId);
		if (prev != null) {
			prev = adjustMorePage(prev);
		}
		return prev;
	}

	private News adjustMorePage(News news) {
		Integer id = news.getId();
		News next2 = mapper.getNext(id);
		// 用户前台下一页按钮控制
		if (next2 != null) {
			news.setNext(true);
		} else {
			news.setNext(false);
		}

		News next3 = mapper.getPrev(id);
		// 用户前台上一页按钮控制
		if (next3 != null) {
			news.setPrev(true);
		} else {
			news.setPrev(false);
		}
		return news;
	}
}
