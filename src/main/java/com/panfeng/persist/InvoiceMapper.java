package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Invoice;
import com.panfeng.resource.view.InvoiceView;

public interface InvoiceMapper {

	public List<Invoice> listWithPagination(final InvoiceView view);
	
	public long maxSize(final InvoiceView view);
	
	public long save(final Invoice invoice);
	
	public long update(final Invoice invoice);
	
	public long deleteByIds(@Param("ids") final long[] ids);
}
