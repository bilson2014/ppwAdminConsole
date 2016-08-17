package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.TeamInvoice;
import com.panfeng.resource.view.InvoiceView;

public interface TeamInvoiceMapper {

	public List<TeamInvoice> listWithPagination(final InvoiceView view);
	
	public long maxSize(final InvoiceView view);
	
	public long save(final TeamInvoice invoice);
	
	public long update(final TeamInvoice invoice);
	
	public long deleteByIds(@Param("ids") final long[] ids);

	public long agreeInvoiceByIds(@Param("ids") long[] ids);

	public long disagreeInvoice(TeamInvoice invoice);
}
