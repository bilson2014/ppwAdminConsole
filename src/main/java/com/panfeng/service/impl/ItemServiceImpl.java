package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.ItemMapper;
import com.panfeng.resource.model.Item;
import com.panfeng.resource.view.ItemView;
import com.panfeng.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private final ItemMapper mapper = null;
	
	@Override
	public List<Item> listWithPagination(final ItemView view) {
		
		final List<Item> list = mapper.listWithPagination(view);
		return list;
	}

	@Override
	public List<Item> list() {
		
		final List<Item> list = mapper.list();
		return list;
	}

	@Override
	public long maxSize(final ItemView view) {
		
		final long total = mapper.maxSize(view);
		return total;
	}

	@Override
	public long save(final Item item) {
		
		final long ret = mapper.save(item);
		return ret;
	}

	@Override
	public long update(final Item item) {
		
		final long ret = mapper.update(item);
		return ret;
	}

	@Override
	public long delete(final long[] ids) {
		
		if(ids.length > 0){
			for(final long id : ids){
				mapper.delete(id);
			}
		}else{
			throw new RuntimeException("Item Delete Error ...");
		}
		return 0l;
	}

	public List<Item> listWithoutActive() {
		
		final List<Item> list = mapper.listWithoutActive();
		return list;
	}

}
