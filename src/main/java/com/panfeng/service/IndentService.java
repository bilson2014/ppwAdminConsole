package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Indent;
import com.panfeng.resource.view.IndentView;

public interface IndentService {

	public List<Indent> listWithPagination(final IndentView view);
	
	public long maxSize(final IndentView view);
	
	public Indent findIndentById(final long indentId);
	
	public long save(final Indent indent);
	
	public long update(final Indent indent);
	
	public long delete(final long[] ids);
	
	public long deleteByUserId(final long userId);
	
	public long deleteByServiceId(final long serviceId);

	// 下单
	public long order(final Indent indent);

	public long checkStatus(final int status);
}
