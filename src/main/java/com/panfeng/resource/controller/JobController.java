package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Job;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.JobView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.JobService;
import com.panfeng.util.Log;

@RestController
@RequestMapping("/portal")
public class JobController extends BaseController {

	@Autowired
	private final JobService service = null;
	
	@RequestMapping("/job-list")
	public ModelAndView view(){
		
		return new ModelAndView("job-list");
	}
	
	@RequestMapping(value = "/job/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public DataGrid<Job> list(final JobView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		DataGrid<Job> dataGrid = new DataGrid<Job>();
		
		final List<Job> list = service.listWithPagination(view);
		
		dataGrid.setRows(list);
		final long total = service.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping("/job/update")
	public long update(final Job job,HttpServletRequest request){
		
		final long ret = service.update(job);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update job ...", sessionInfo);
		return ret;
	}
	
	@RequestMapping("/job/save")
	public long save(final Job job,HttpServletRequest request){
		
		final long ret = service.save(job);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("add job ...", sessionInfo);
		return ret;
	}
	
	@RequestMapping("/job/delete")
	public long delete(final long[] ids,HttpServletRequest request){
		
		final long ret = service.delete(ids);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete items ...  ids:"+ids.toString(), sessionInfo);
		return ret;
	}
	
	// -------------------------- 前端请求 -------------------------------
	@RequestMapping("/job/static/list")
	public List<Job> list(final HttpServletRequest request){
		
		final List<Job> list = service.getAll();
		return list;
	}
	
	@RequestMapping("/job/static/{id}")
	public Job job(final HttpServletRequest request,@PathVariable("id") final Long id){
		
		if(id != null){
			final Job job = service.findJobById(id);
			return job;
		}
		return null;
	}
}
