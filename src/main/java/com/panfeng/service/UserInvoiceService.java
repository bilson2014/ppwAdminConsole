package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.UserInvoice;
import com.panfeng.resource.view.InvoiceView;

public interface UserInvoiceService {

	/**
	 * 分页查询书
	 * @param view 条件
	 * @return
	 */
	public List<UserInvoice> listWithPagination(final InvoiceView view);
	
	/**
	 * 总条目数
	 * @param view 条件
	 * @return
	 */
	public long maxSize(final InvoiceView view);
	
	/**
	 * 保存
	 * @param invoice 发票实体
	 * @return
	 */
	public long save(final UserInvoice invoice);
	
	/**
	 * 更新
	 * @param invoice 发票实体
	 * @return
	 */
	public long update(final UserInvoice invoice);
	
	/**
	 * 批量删除发票
	 * @param ids id 数组
	 * @return
	 */
	public long deleteByIds(final long[] ids);

	public long auditing(UserInvoice invoice);
}
