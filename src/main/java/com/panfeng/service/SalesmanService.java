package com.panfeng.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Salesman;
import com.panfeng.resource.view.SalesmanView;

public interface SalesmanService {

	public List<Salesman> listWithPagination(final SalesmanView view);

	public long maxSize(final SalesmanView view);

	public long update(final Salesman salesman);
	
	public long save(final Salesman salesman);
	
	public long delete(final long[] ids);

	public List<Salesman> list();

	public Salesman findSalesmanById(final long id);
	
	/**
	 * 根据唯一身份表示获取分销人
	 * @param uniqueId 分销人唯一身份标示
	 */
	public Salesman findSalesmanByUniqueId(@Param("uniqueId") final String uniqueId);
}
