package com.panfeng.service;

import java.io.OutputStream;
import java.util.List;

import com.paipianwang.pat.facade.finance.entity.PmsTeamInvoice;

public interface TeamInvoiceService {

	/**
	 * 导出报表
	 * @param list
	 * @param os
	 */
	public void generateReport(List<PmsTeamInvoice> list, OutputStream os);
}
