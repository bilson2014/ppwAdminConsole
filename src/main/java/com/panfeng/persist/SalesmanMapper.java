package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Salesman;
import com.panfeng.resource.view.SalesmanView;

public interface SalesmanMapper {

	public List<Salesman> listWithPagination(final SalesmanView view);

	public long maxSize(final SalesmanView view);

	public long update(final Salesman salesman);

	public long save(final Salesman salesman);

	public long delete(@Param("salesmanId") final long salesmanId);

	public List<Salesman> list();

	public Salesman findSalesmanById(@Param("salesmanId") final long salesmanId);

	/**
	 * 根据唯一身份表示获取分销人
	 * @param uniqueId 分销人唯一身份标示
	 */
	public Salesman findSalesmanByUniqueId(@Param("uniqueId") final String uniqueId);
}
