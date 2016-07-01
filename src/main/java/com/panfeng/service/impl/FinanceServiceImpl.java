package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.FinanceMapper;
import com.panfeng.resource.model.DealLog;
import com.panfeng.resource.model.Job;
import com.panfeng.resource.view.FinanceView;
import com.panfeng.service.FinanceService;
import com.panfeng.util.ValidateUtil;

@Service
public class FinanceServiceImpl implements FinanceService {

	@Autowired
	private FinanceMapper mapper = null;
	
	@Override
	public List<DealLog> listWithPagination(final FinanceView view) {
		
		final List<DealLog> list = mapper.listWithPagination(view);
		return list;
	}

	@Override
	public long maxSize(final FinanceView view) {
		
		final long size = mapper.maxSize(view);
		return size;
	}

	@Override
	public long update(final DealLog dealLog) {
		
		final long ret = mapper.update(dealLog);
		return ret;
	}

	@Override
	public long save(final DealLog dealLog) {
		
		final long ret = mapper.save(dealLog);
		return ret;
	}

	@Transactional
	public long deleteByArray(final long[] ids) {
		
		if(ValidateUtil.isValid(ids)){
			return mapper.deleteByArray(ids);
		}
		return 0l;
	}

	@Override
	public List<DealLog> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Job findDealLogById(final long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
