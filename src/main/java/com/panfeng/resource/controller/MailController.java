package com.panfeng.resource.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.panfeng.resource.model.Mail;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.MailView;
import com.panfeng.service.MailService;

/**
 * 邮件
 */
@RestController
@RequestMapping("/portal")
public class MailController extends BaseController {
	
	@Autowired
	private final MailService mailService = null;
	//@Autowired
	//private final UserService userService = null;
	
	@RequestMapping(value = "/mail-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("mail-list", model);
	}
	
	@RequestMapping(value = "/mail/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<Mail> list(final MailView view, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);

		DataGrid<Mail> dataGrid = new DataGrid<Mail>();
		final List<Mail> list = mailService.listWithPagination(view);
		dataGrid.setRows(list);
		final long total = mailService.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	@RequestMapping(value = "/mail/save", method = RequestMethod.POST)
	public void save(final Mail mail) {
		mailService.save(mail);
	}
	
	@RequestMapping(value = "/mail/update", method = RequestMethod.POST)
	public void update(final Mail mail) {
		mailService.update(mail);
	}
	@RequestMapping(value = "/mail/delete", method = RequestMethod.POST)
	public void delete(final int[] ids) {
		mailService.delete(ids);
	}
}
