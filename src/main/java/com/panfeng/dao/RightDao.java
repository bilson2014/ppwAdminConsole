package com.panfeng.dao;

import java.util.Map;

import com.panfeng.resource.model.Right;

public interface RightDao {

	public Right getRightFromRedis(final String uri);
	
	public Map<String,Right> getRightsFromRedis();
	
	public void addRightByRedis(final Right right);
	
	public void resetRightFromRedis(final Map<String,Right> map);
}
