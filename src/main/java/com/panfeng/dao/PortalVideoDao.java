package com.panfeng.dao;

import java.util.Map;

import com.panfeng.resource.model.Product;

public interface PortalVideoDao {
	
	/**
	 * 重置首页视频集合
	 * @param productMap
	 */
	public void resetPortalVideo(final Map<Long,Product> productMap);
	
	/**
	 * 获取首页视频集合
	 * @return
	 */
	public Map<Long,Product> getProductsFromRedis();
}
