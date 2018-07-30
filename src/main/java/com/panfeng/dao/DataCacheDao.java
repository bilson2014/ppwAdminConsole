package com.panfeng.dao;

public interface DataCacheDao {

	public void setCacheData(String key, String type,String data);
	public String getCacheData(String key,String type);
	
	public void setExpire(String key,int expireSeconds);
}
