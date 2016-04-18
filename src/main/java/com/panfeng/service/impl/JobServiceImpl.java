package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.JobMapper;
import com.panfeng.resource.model.Job;
import com.panfeng.resource.view.JobView;
import com.panfeng.service.JobService;

@Service
public class JobServiceImpl implements JobService {

	@Autowired
	private final JobMapper mapper = null;
	
	public List<Job> listWithPagination(final JobView view) {
		
		final List<Job> list = mapper.listWithPagination(view);
		return list;
	}

	public long maxSize(final JobView view) {
		
		final long total = mapper.maxSize(view);
		return total;
	}

	public long update(final Job job) {
		
		final long ret = mapper.update(job);
		return ret;
	}

	public long save(final Job job) {
		
		final long ret = mapper.save(job);
		return ret;
	}

	@Transactional
	public long delete(final long[] ids) {
		
		if(ids != null && ids.length > 0){
			for (final long id : ids) {
				mapper.delete(id);
			}
		}
		return 0l;
	}

	public List<Job> getAll() {
		
		final List<Job> list = mapper.getAll();
		return list;
	}

	public Job findJobById(final long id) {
		
		final Job job = mapper.findJobById(id);
		return job;
	}

}
