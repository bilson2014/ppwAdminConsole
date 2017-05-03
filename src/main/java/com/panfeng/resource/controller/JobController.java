package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.facade.employee.entity.PmsJob;
import com.paipianwang.pat.facade.employee.service.PmsJobFacade;
import com.panfeng.resource.view.JobView;
import com.panfeng.util.Log;

@RestController
@RequestMapping("/portal")
public class JobController extends BaseController {

	/*@Autowired
	private final JobService service = null;*/
	
	@Autowired
	private final PmsJobFacade pmsJobFacade = null;
	
	
	@RequestMapping("/job-list")
	public ModelAndView view(){
		
		return new ModelAndView("job-list");
	}
	
	@RequestMapping(value = "/job/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public DataGrid<PmsJob> list(final JobView view,final PageParam param){
		
		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);
		
		final DataGrid<PmsJob> dataGrid = pmsJobFacade.listWithPagination(param, JsonUtil.objectToMap(view));
		
		return dataGrid;
	}
	
	@RequestMapping("/job/update")
	public long update(final PmsJob job,HttpServletRequest request){
		
		final long ret = pmsJobFacade.update(job);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update job ...", sessionInfo);
		return ret;
	}
	
	@RequestMapping("/job/save")
	public long save(final PmsJob job,HttpServletRequest request){
		
		final long ret = pmsJobFacade.save(job);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("add job ...", sessionInfo);
		return ret;
	}
	
	@RequestMapping("/job/delete")
	public long delete(final long[] ids,HttpServletRequest request){
		
		final long ret = pmsJobFacade.delete(ids);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete items ...  ids:"+ids.toString(), sessionInfo);
		return ret;
	}
	
	// -------------------------- 前端请求 -------------------------------
	@RequestMapping("/job/static/list")
	public List<PmsJob> list(final HttpServletRequest request){
		
		final List<PmsJob> list = pmsJobFacade.getAll();
		return list;
	}
	
	@RequestMapping("/job/static/{id}")
	public PmsJob job(final HttpServletRequest request,@PathVariable("id") final Long id){
		
		if(id != null){
			final PmsJob job = pmsJobFacade.findJobById(id);
			return job;
		}
		return null;
	}
}
