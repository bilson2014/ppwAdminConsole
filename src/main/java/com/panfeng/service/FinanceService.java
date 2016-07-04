package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.DealLog;
import com.panfeng.resource.model.Job;
import com.panfeng.resource.view.FinanceView;

public interface FinanceService {

	public List<DealLog> listWithPagination(final FinanceView view);

	public long maxSize(final FinanceView view);

	public long update(final DealLog job);
	
	public long save(final DealLog job);
	
	public long deleteByArray(final long[] ids);

	public List<DealLog> getAll();

	public Job findDealLogById(final long id);
}
