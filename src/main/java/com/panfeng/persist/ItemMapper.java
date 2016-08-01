package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Item;
import com.panfeng.resource.view.ItemView;

public interface ItemMapper {

	public List<Item> listWithPagination(final ItemView view);
	
	public List<Item> list();
	
	public long maxSize(final ItemView view);
	
	public long save(final Item item);
	
	public long update(final Item item);
	
	public long delete(@Param("itemId") final long itemId);

	// 获取活动之外的分类
	public List<Item> listWithoutActive();
	// 获取标签集合
	public List<Item> getTagsById(@Param("ids")List<Long> ids);
}
