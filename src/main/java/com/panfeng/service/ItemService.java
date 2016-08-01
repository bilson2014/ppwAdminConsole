package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Item;
import com.panfeng.resource.view.ItemView;

public interface ItemService {

	public List<Item> listWithPagination(final ItemView view);
	
	public List<Item> list();
	
	public long maxSize(final ItemView view);
	
	public long save(final Item item);
	
	public long update(final Item item);
	
	public long delete(final long[] ids);

	// 获取活动之外的分类
	public List<Item> listWithoutActive();

	public List<Item> getTagsById(List<Long> ids);
}
