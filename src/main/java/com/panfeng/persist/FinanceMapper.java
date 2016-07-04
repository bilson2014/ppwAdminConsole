package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.DealLog;
import com.panfeng.resource.view.FinanceView;

public interface FinanceMapper {

	public List<DealLog> listWithPagination(final FinanceView view);

	public long maxSize(final FinanceView view);

	public long update(final DealLog dealLog);
	
	public long save(final DealLog dealLog);
	
	public long deleteByArray(@Param("ids") final long[] ids);

}
