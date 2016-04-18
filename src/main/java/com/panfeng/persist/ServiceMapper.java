package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Service;
import com.panfeng.resource.view.ServiceView;

public interface ServiceMapper {

	public List<Service> listWithPagination(final ServiceView view);
	
	public long maxSize(final ServiceView view);
	/**
	 * 删除 所属于 product编号的 service
	 * @param productId product 编号
	 */
	public long deleteByProduct(@Param("productId") final long productId);
	
	/**
	 * 根据 服务编号 删除 service 
	 * @param serviceId service 编号
	 */
	public long delete(@Param("serviceId") final long serviceId);
	
	public long save(final Service service);
	
	public long update(final Service service);
	
	public Service findServiceById(@Param("serviceId") final long serviceId);

	public List<Service> loadService(@Param("productId") final Integer productId);

	public Service loadSingleService(@Param("productId") final long productId);

	/**
	 * 更新 服务价格和秒数
	 * @param service 包含 价格和秒数
	 */
	public long updatePriceAndMcoms(final Service service);

	/**
	 * 根据服务ID获取服务
	 * @param serviceId 服务ID
	 * @return
	 */
	public Service loadServiceById(@Param("serviceId") final long serviceId);
}
