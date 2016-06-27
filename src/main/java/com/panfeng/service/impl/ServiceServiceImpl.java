package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.ServiceMapper;
import com.panfeng.resource.model.Service;
import com.panfeng.resource.view.ServiceView;
import com.panfeng.service.ServiceService;

@org.springframework.stereotype.Service

public class ServiceServiceImpl implements ServiceService{

	@Autowired
	private ServiceMapper mapper = null;
	
	@Override
	public List<Service> listWithPagination(final ServiceView view) {
		
		final List<Service> list = mapper.listWithPagination(view);
		return list;
	}
	
	@Override
	public long maxSize(final ServiceView view) {
		
		final long total = mapper.maxSize(view);
		return total;
	}

	@Override
	public long deleteByProduct(final long productId) {
		
		final long ret = mapper.deleteByProduct(productId);
		return ret;
	}

	@Transactional
	public long delete(final long[] ids) {
		
		if(ids.length > 0){
			for (final long id : ids) {
				mapper.delete(id);
			}
		}else{
			throw new RuntimeException("Service delete error ...");
		}
		return 0l;
	}

	@Override
	public long save(final Service service) {
		
		final long ret = mapper.save(service);
		return ret;
	}

	@Override
	public long update(final Service service) {
		
		final long ret = mapper.update(service);
		return ret;
	}

	@Override
	public Service findServiceById(final long serviceId) {
		
		final Service service = mapper.findServiceById(serviceId);
		return service;
	}

	@Override
	public List<Service> loadService(final Integer productId) {
		
		final List<Service> list = mapper.loadService(productId);
		return list;
	}

	@Override
	public Service loadSingleService(final long productId) {
		
		final Service service = mapper.loadSingleService(productId);
		return service;
	}

	@Override
	public long updatePriceAndMcoms(final Service service) {
	
		final long ret = mapper.updatePriceAndMcoms(service);
		return ret;
	}
	
	public Service loadServiceById(final long serviceId){
		
		final Service service = mapper.loadServiceById(serviceId);
		return service;
	}

	public Service getServiceById(final long serviceId) {
		
		final Service service = mapper.getServiceById(serviceId);
		return service;
	}

}
