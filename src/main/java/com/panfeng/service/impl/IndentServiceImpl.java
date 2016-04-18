package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.IndentMapper;
import com.panfeng.resource.model.Indent;
import com.panfeng.resource.view.IndentView;
import com.panfeng.service.IndentService;

@Service
public class IndentServiceImpl implements IndentService {

	@Autowired
	private final IndentMapper mapper = null;
	
	@Override
	public List<Indent> listWithPagination(final IndentView view) {
		
		final List<Indent> list = mapper.listWithPagination(view);
		return list;
	}

	@Override
	public long maxSize(final IndentView view) {
		
		final long total = mapper.maxSize(view);
		return total;
	}

	@Override
	public Indent findIndentById(final long indentId) {
		
		final Indent indent = mapper.findIndentById(indentId);
		return indent;
	}

	@Override
	public long save(final Indent indent) {
		
		final long ret = mapper.save(indent);
		return ret;
	}

	@Override
	public long update(final Indent indent) {
		
		final long ret = mapper.update(indent);
		return ret;
	}

	@Override
	public long delete(final long[] ids) {
		
		if(ids.length > 0){
			for(final long id : ids){
				mapper.delete(id);
			}
		} else {
			throw new RuntimeException("Indent Delete error ...");
		}
		return 0l;
	}

	@Override
	public long deleteByUserId(final long userId) {
		
		final long ret = mapper.deleteByUserId(userId);
		return ret;
	}

	@Override
	public long deleteByServiceId(final long serviceId) {
		
		final long ret = mapper.deleteByServiceId(serviceId);
		return ret;
	}

	@Override
	public long order(final Indent indent) {
		
		final long ret = mapper.order(indent);
		return ret;
	}

	@Override
	public long checkStatus(final int status) {
		
		final long count = mapper.checkStatus(status);
		return count;
	}

}
