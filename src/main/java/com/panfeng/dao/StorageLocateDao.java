package com.panfeng.dao;

import java.util.Map;

public interface StorageLocateDao {

	// 从redis中获取storage节点IP地址
	public Map<String, String> getStorageFromRedis(final String id);
	
	// 重置Storage Node
	public void resetStorageFromRedis(final Map<String, String> nodes);
}
