package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Sms;
import com.panfeng.resource.view.Pagination;

public interface SMSTemplateService {

	List<Sms> listWithPagination(Pagination pagination);

	long maxSize(Pagination pagination);

	void save(Sms sms);

	void update(Sms sms);

	long delete(int[] ids);

}
