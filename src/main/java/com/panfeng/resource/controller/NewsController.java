package com.panfeng.resource.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.News;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.NewsView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.FDFSService;
import com.panfeng.service.NewsService;

/**
 * 新闻编辑
 */
@RestController
@RequestMapping("/portal")
public class NewsController extends BaseController {

	@Autowired
	private NewsService newsService;

	@Autowired
	private final FDFSService fdfsService = null;

	@RequestMapping(value = "/news-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("news-list", model);
	}

	@RequestMapping(value = "/news/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<News> list(final NewsView newsView, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		newsView.setBegin((page - 1) * rows);
		newsView.setLimit(rows);

		DataGrid<News> dataGrid = new DataGrid<News>();
		final List<News> list = newsService.listWithPagination(newsView);
		dataGrid.setRows(list);
		final long total = newsService.maxSize(newsView);
		dataGrid.setTotal(total);
		return dataGrid;
	}

	@RequestMapping(value = "/news/save", method = RequestMethod.POST)
	public void save(final News news, MultipartFile picLDUrlFile) {
		if (!picLDUrlFile.isEmpty()) {
			String path = fdfsService.upload(picLDUrlFile);
			news.setPicLDUrl(path);
		}
		newsService.save(news);
	}

	@RequestMapping(value = "/news/update", method = RequestMethod.POST)
	public void update(final News news, MultipartFile picLDUrlFile) {
		if (!picLDUrlFile.isEmpty()) {

			String picLDUrl = news.getPicLDUrl();
			if (StringUtils.isNotEmpty(picLDUrl)) {
				fdfsService.delete(picLDUrl);
			}
			String path = fdfsService.upload(picLDUrlFile);
			news.setPicLDUrl(path);
		}
		newsService.update(news);
	}

	@RequestMapping(value = "/news/delete", method = RequestMethod.POST)
	public void delete(final int[] ids) {
		newsService.delete(ids);
	}

	/**
	 * action 排序动作 up down
	 */
	@RequestMapping("/news/sort")
	public boolean sortRecommendTeam(final String action, final String id) {
		boolean flag = false;
		int newid = Integer.valueOf(id);
		switch (action) {
		case "up":
			flag = newsService.moveUp(newid);
			break;
		case "down":
			flag = newsService.moveDown(newid);
			break;
		}
		return flag;
	}

	/**
	 * 获取首页推荐的新闻列表
	 */
	@RequestMapping("/news/recommend")
	public List<News> RecommendNews() {
		List<News> list = newsService.RecommendNews();
		return list;
	}

	/**
	 * 根据id获取新闻资讯
	 */
	@RequestMapping("/news/info/{newId}")
	public News info(@PathVariable("newId") final Integer newId) {
		News news = newsService.info(newId);
		return news;
	}

	/**
	 * 新闻详情页推荐列表
	 */
	@RequestMapping("/news/info/recommend")
	public List<News> newsInfoRecommend() {

		List<News> list = newsService.searchAllNews();
		return list;
	}
	// ------------------------------前台分页 -----------------------------

	@RequestMapping("/news/pagelist")
	public List<News> newsList(@RequestBody NewsView newsView) {
		if (newsView != null) {
			if (newsView.getCategory() == -1) {
				newsView.setCategory(null);
			}
		}
		List<News> list = newsService.listWithPagination(newsView);
		return list;
	}

	@RequestMapping("/news/pagesize")
	public long maxSize(@RequestBody NewsView newsView) {
		if (newsView != null) {
			if (newsView.getCategory() == -1) {
				newsView.setCategory(null);
			}
		}
		final long total = newsService.maxSize(newsView);
		return total;
	}

}
