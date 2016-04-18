package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Job;
import com.panfeng.resource.view.JobView;

public interface JobMapper {

	public List<Job> listWithPagination(final JobView view);

	public long maxSize(final JobView view);

	public long update(final Job job);

	public long save(final Job job);

	public void delete(@Param("jobId") final long jobId);
	
	public Job findJobById(@Param("jobId") final long jobId);

	public List<Job> getAll();
}
