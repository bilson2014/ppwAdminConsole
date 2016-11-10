package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Sms;
import com.panfeng.resource.view.Pagination;

public interface SMSTemplateMapper {

	List<Sms> listWithPagination(Pagination pagination);

	long maxSize(Pagination pagination);

	void save(Sms sms);

	void update(Sms sms);

	void delete(@Param("id") final int id);

}
