/*package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.UserInvoiceMapper;
import com.panfeng.resource.model.UserInvoice;
import com.panfeng.resource.view.InvoiceView;
import com.panfeng.service.UserInvoiceService;
import com.paipianwang.pat.common.util.Constants;
import com.panfeng.util.ValidateUtil;

@Service
public class UserInvoiceServiceImpl implements UserInvoiceService{

	@Autowired
	private final UserInvoiceMapper mapper = null;
	
	public List<UserInvoice> listWithPagination(final InvoiceView view) {
		
		final List<UserInvoice> list = mapper.listWithPagination(view);
		return list;
	}

	public long maxSize(final InvoiceView view) {
		
		final long size = mapper.maxSize(view);
		return size;
	}

	public long save(final UserInvoice invoice) {
		final long ret = mapper.save(invoice);
		return ret;
	}

	public long update(final UserInvoice invoice) {
		
		final long ret = mapper.update(invoice);
		return ret;
	}

	public long deleteByIds(final long[] ids) {
		
		final long ret = mapper.deleteByIds(ids);
		return ret;
	}
	

	@Override
	public long auditing(UserInvoice invoice) {
		int status = invoice.getInvoiceStatus();//发票状态
		if(status == Constants.INVOICE_STATUS_OK){//通过审核
			if(ValidateUtil.isValid(invoice.getIds())){
				return mapper.agreeInvoiceByIds(invoice.getIds());
			}
		}else if(status == Constants.INVOICE_STATUS_NO){//未通过
			if(invoice.getInvoiceId()!=0){
				return mapper.disagreeInvoice(invoice);
			}
		}
		return 0;
	}

}
*/