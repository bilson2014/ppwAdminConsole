package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Service;
import com.panfeng.resource.view.ServiceView;

public interface ServiceService {
	
	public List<Service> listWithPagination(final ServiceView view);
	
	public long maxSize(final ServiceView view);
	/**
	 * 删除 所属于 product编号的 service
	 * @param productId product 编号
	 */
	public long deleteByProduct(final long productId);
	
	/**
	 * 根据 服务编号 删除 service 
	 * @param serviceId service 编号
	 */
	public long delete(final long[] ids);
	
	public long save(final Service service);
	
	public long update(final Service service);
	
	public Service findServiceById(final long serviceId);

	public List<Service> loadService(final Integer productId);

	// 按照升序只取一条service数据
	public Service loadSingleService(final long productId);

	/**
	 * 更新 服务价格和秒数
	 * @param service 包含 价格和秒数
	 */
	public long updatePriceAndMcoms(final Service service);
	
	/**
	 * 根据 服务ID 获取 服务 
	 * @param serviceId 服务ID
	 */
	public Service loadServiceById(final long serviceId);
}
