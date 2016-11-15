package com.panfeng.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.News;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.Pagination;
import com.panfeng.service.NewsService;

/**
 * 新闻编辑
 */
@RestController
@RequestMapping("/portal")
public class NewsController extends BaseController {

	@Autowired
	private NewsService newsService;
	
	@RequestMapping(value = "/news-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("news-list", model);
	}
	
	@RequestMapping(value = "/news/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<News> list(final Pagination pagination, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		pagination.setBegin((page - 1) * rows);
		pagination.setLimit(rows);

		DataGrid<News> dataGrid = new DataGrid<News>();
		final List<News> list = newsService.listWithPagination(pagination);
		dataGrid.setRows(list);
		final long total = newsService.maxSize(pagination);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	@RequestMapping(value = "/news/save", method = RequestMethod.POST)
	public void save(final News news) {
		newsService.save(news);
	}
	
	@RequestMapping(value = "/news/update", method = RequestMethod.POST)
	public void update(final News news) {
		newsService.update(news);
	}
	@RequestMapping(value = "/news/delete", method = RequestMethod.POST)
	public void delete(final int[] ids) {
		newsService.delete(ids);
	}
}
