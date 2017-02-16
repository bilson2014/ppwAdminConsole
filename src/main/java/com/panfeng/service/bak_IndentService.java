package com.panfeng.service;
/*package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Indent;
import com.panfeng.resource.view.IndentView;

public interface IndentService {

	public List<Indent> listWithPagination(final IndentView view);
	
	public long maxSize(final IndentView view);
	
	public long save(final Indent indent);
	
	public long update(final Indent indent);
	
	public long delete(final long[] ids);
	
	public long deleteByUserId(final long userId);
	
	public long deleteByServiceId(final long serviceId);

	// 下单
	public long order(final Indent indent);

	public long checkStatus(final int status);
	
	*//**
	 * 根据分销人唯一编号计算订单总额
	 * @param salesmanUniqueId 分销人唯一编号
	 * @return 订单总额
	 *//*
	public Double sumPriceBySalesmanUniqueId(final String salesmanUniqueId);
	
	*//**
	 * 根据分销人唯一编号计算订单总数
	 * @param salesmanUniqueId 分销人唯一编号
	 * @return 订单总数
	 *//*
	public long countBySalesmanUniqueId(final String salesmanUniqueId);

	*//**
	 * 成本计算器更新订单信息
	 *//*
	public long updateForCalculate(Indent indent);

	*//**
	 * 批量修改订单状态
	 *//*
	public boolean changeIndentsType(Indent indent);
}
*/