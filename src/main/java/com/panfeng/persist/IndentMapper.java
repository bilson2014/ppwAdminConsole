package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Indent;
import com.panfeng.resource.view.IndentView;

public interface IndentMapper {

	public List<Indent> listWithPagination(final IndentView view);
	
	public long maxSize(final IndentView view);
	
	public long save(final Indent indent);
	
	public long update(final Indent indent);
	
	public long delete(@Param("indentId") final long indentId);
	
	public long deleteByUserId(@Param("userId") final long userId);
	
	public long deleteByServiceId(@Param("serviceId") final long serviceId);

	public long order(final Indent indent);

	public long checkStatus(@Param("indentType") final int indentType);
	
	public Double sumPriceBySalesmanUniqueId(@Param("salesmanUniqueId") final String salesmanUniqueId);
	
	public long countBySalesmanUniqueId(@Param("salesmanUniqueId") final String salesmanUniqueId);

	/**
	 * 成本计算器更新订单信息
	 */
	public long updateForCalculate(Indent indent);

	public long changeIndentsType(Indent indent);
}
