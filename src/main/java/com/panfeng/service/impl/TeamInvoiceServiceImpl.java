package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.TeamInvoiceMapper;
import com.panfeng.resource.model.TeamInvoice;
import com.panfeng.resource.view.InvoiceView;
import com.panfeng.service.TeamInvoiceService;
import com.panfeng.util.Constants;
import com.panfeng.util.ValidateUtil;

@Service
public class TeamInvoiceServiceImpl implements TeamInvoiceService{

	@Autowired
	private final TeamInvoiceMapper mapper = null;
	
	public List<TeamInvoice> listWithPagination(final InvoiceView view) {
		
		final List<TeamInvoice> list = mapper.listWithPagination(view);
		return list;
	}

	public long maxSize(final InvoiceView view) {
		
		final long size = mapper.maxSize(view);
		return size;
	}

	public long save(final TeamInvoice invoice) {
		final long ret = mapper.save(invoice);
		return ret;
	}

	public long update(final TeamInvoice invoice) {
		
		final long ret = mapper.update(invoice);
		return ret;
	}

	public long deleteByIds(final long[] ids) {
		
		final long ret = mapper.deleteByIds(ids);
		return ret;
	}

	@Override
	public long auditing(TeamInvoice invoice) {
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
