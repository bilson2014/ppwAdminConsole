package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Job;
import com.panfeng.resource.view.JobView;

public interface JobService {

	public List<Job> listWithPagination(final JobView view);

	public long maxSize(final JobView view);

	public long update(final Job job);
	
	public long save(final Job job);
	
	public long delete(final long[] ids);

	public List<Job> getAll();

	public Job findJobById(final long id);
}