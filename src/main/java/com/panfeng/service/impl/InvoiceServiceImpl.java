package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.InvoiceMapper;
import com.panfeng.resource.model.Invoice;
import com.panfeng.resource.view.InvoiceView;
import com.panfeng.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService{

	@Autowired
	private final InvoiceMapper mapper = null;
	
	public List<Invoice> listWithPagination(final InvoiceView view) {
		
		final List<Invoice> list = mapper.listWithPagination(view); 
		return list;
	}

	public long maxSize(final InvoiceView view) {
		
		final long size = mapper.maxSize(view);
		return size;
	}

	public long save(final Invoice invoice) {
		final long ret = mapper.save(invoice);
		return ret;
	}

	public long update(final Invoice invoice) {
		
		final long ret = mapper.update(invoice);
		return ret;
	}

	public long deleteByIds(final long[] ids) {
		
		final long ret = mapper.deleteByIds(ids);
		return ret;
	}

}
