package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Salesman;
import com.panfeng.resource.view.SalesmanView;

public interface SalesmanService {

	public List<Salesman> listWithPagination(final SalesmanView view);

	public long maxSize(final SalesmanView view);

	public long update(final Salesman salesman);
	
	public long save(final Salesman salesman);
	
	public long delete(final long[] ids);

	public List<Salesman> list();

	public Salesman findSalesmanById(final long id);
}
