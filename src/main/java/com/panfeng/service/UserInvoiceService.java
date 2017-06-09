package com.panfeng.service;

import java.io.OutputStream;
import java.util.List;

import com.paipianwang.pat.facade.finance.entity.PmsUserInvoice;

public interface UserInvoiceService {

	/**
	 * 导出报表
	 * @param list
	 * @param os
	 */
	public void generateReport(List<PmsUserInvoice> list, OutputStream os);
}