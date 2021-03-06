/*package com.panfeng.service.impl;

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

	@Override
	public Double sumPriceBySalesmanUniqueId(final String salesmanUniqueId) {
		
		final Double price = mapper.sumPriceBySalesmanUniqueId(salesmanUniqueId);
		return price;
	}

	@Override
	public long countBySalesmanUniqueId(final String salesmanUniqueId) {
		
		final long total = mapper.countBySalesmanUniqueId(salesmanUniqueId);
		return total;
	}

	*//**
	 * 成本计算器更新订单信息
	 *//*
	@Override
	public long updateForCalculate(Indent indent) {
		 return mapper.updateForCalculate(indent);
	}

	*//**
	 * 批量修改订单状态
	 *//*
	@Override
	public boolean changeIndentsType(Indent indent) {
			
		long l = mapper.changeIndentsType(indent);
		return l>=0l;
	}

}
*/