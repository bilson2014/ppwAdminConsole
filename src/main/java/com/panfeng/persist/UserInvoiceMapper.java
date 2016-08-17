package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.UserInvoice;
import com.panfeng.resource.view.InvoiceView;

public interface UserInvoiceMapper {

	public List<UserInvoice> listWithPagination(final InvoiceView view);
	
	public long maxSize(final InvoiceView view);
	
	public long save(final UserInvoice invoice);
	
	public long update(final UserInvoice invoice);
	
	public long deleteByIds(@Param("ids") final long[] ids);
	
	public long agreeInvoiceByIds(@Param("ids") long[] ids);

	public long disagreeInvoice(UserInvoice invoice);
}
