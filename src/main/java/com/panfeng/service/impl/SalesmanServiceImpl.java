package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.SalesmanMapper;
import com.panfeng.resource.model.Salesman;
import com.panfeng.resource.view.SalesmanView;
import com.panfeng.service.SalesmanService;
import com.panfeng.util.ValidateUtil;

@Service
public class SalesmanServiceImpl implements SalesmanService {
	
	@Autowired
	private final SalesmanMapper mapper = null;

	public List<Salesman> listWithPagination(SalesmanView view) {
		
		final List<Salesman> list = mapper.listWithPagination(view);
		return list;
	}

	public long maxSize(final SalesmanView view) {
		
		final long ret = mapper.maxSize(view);
		return ret;
	}

	public long update(final Salesman salesman) {
		
		final long ret = mapper.update(salesman);
		return ret;
	}

	public long save(final Salesman salesman) {
		
		final long ret = mapper.save(salesman);
		return ret;
	}

	@Transactional
	public long delete(final long[] ids) {
		
		if(ValidateUtil.isValid(ids)){
			for (final long id : ids) {
				final long ret = mapper.delete(id);
				if(ret == 0){
					return ret;
				}
			}
		}
		return ids.length;
	}

	public List<Salesman> list() {
		
		List<Salesman> list = mapper.list();
		return list;
	}

	public Salesman findSalesmanById(final long id) {
		
		final Salesman salesman = mapper.findSalesmanById(id);
		return salesman;
	}

}
