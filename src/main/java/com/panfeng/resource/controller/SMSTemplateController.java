package com.panfeng.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.Sms;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.Pagination;
import com.panfeng.service.SMSTemplateService;
/**
 *短信模板
 */
@RestController
@RequestMapping("/portal")
public class SMSTemplateController extends BaseController{

	@Autowired
	private SMSTemplateService smsService;
	
	@RequestMapping(value = "/sms-list")
	public ModelAndView view(final ModelMap model) {
		return new ModelAndView("sms-list", model);
	}
	
	@RequestMapping(value = "/sms/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<Sms> list(final Pagination pagination, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		pagination.setBegin((page - 1) * rows);
		pagination.setLimit(rows);

		DataGrid<Sms> dataGrid = new DataGrid<Sms>();
		final List<Sms> list = smsService.listWithPagination(pagination);
		dataGrid.setRows(list);
		final long total = smsService.maxSize(pagination);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping(value = "/sms/save", method = RequestMethod.POST)
	public void save(final Sms sms) {
		smsService.save(sms);
	}
	
	@RequestMapping(value = "/sms/update", method = RequestMethod.POST)
	public void update(final Sms sms) {
		smsService.update(sms);
	}
	@RequestMapping(value = "/sms/delete", method = RequestMethod.POST)
	public void delete(final int[] ids) {
		smsService.delete(ids);
	}
}
