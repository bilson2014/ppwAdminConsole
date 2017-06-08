package com.panfeng.resource.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.information.entity.PmsNews;
import com.paipianwang.pat.facade.information.service.PmsNewsFacade;
import com.panfeng.resource.view.NewsView;

/**
 * 新闻编辑
 */
@RestController
@RequestMapping("/portal")
public class NewsController extends BaseController {

	@Autowired
	private PmsNewsFacade facade = null;

	@RequestMapping(value = "/news-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("news-list", model);
	}

	@RequestMapping(value = "/news/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsNews> list(final NewsView view, final PageParam pageParam) {

		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tags", view.getTags());
		param.put("recommend", view.getRecommend());
		param.put("status", view.getStatus());
		param.put("visible", view.getVisible());
		
		DataGrid<PmsNews> dataGrid = facade.listWithPagination(pageParam, param);
		return dataGrid;
	}

	@RequestMapping(value = "/news/save", method = RequestMethod.POST)
	public void save(final PmsNews news, MultipartFile picLDUrlFile) {
		if (!picLDUrlFile.isEmpty()) {
			String path = FastDFSClient.uploadFile(picLDUrlFile);
			news.setPicLDUrl(path);
		}
		facade.insert(news);
	}

	@RequestMapping(value = "/news/update", method = RequestMethod.POST)
	public void update(final PmsNews news, MultipartFile picLDUrlFile) {
		if (!picLDUrlFile.isEmpty()) {

			String picLDUrl = news.getPicLDUrl();
			if (StringUtils.isNotEmpty(picLDUrl)) {
				FastDFSClient.deleteFile(picLDUrl);
			}
			String path = FastDFSClient.uploadFile(picLDUrlFile);
			news.setPicLDUrl(path);
		}
		facade.update(news);
	}

	@RequestMapping(value = "/news/delete", method = RequestMethod.POST)
	public void delete(final long[] ids) {
		
		facade.deleteByIds(ids);
	}

}
