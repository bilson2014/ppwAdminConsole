package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.SMSTemplateMapper;
import com.panfeng.resource.model.Sms;
import com.panfeng.resource.view.Pagination;
import com.panfeng.service.SMSTemplateService;

@Service
public class SMSTemplateServiceImpl implements SMSTemplateService{

	@Autowired
	private SMSTemplateMapper mapper;
	
	@Override
	public List<Sms> listWithPagination(Pagination pagination) {
		return mapper.listWithPagination(pagination);
	}

	@Override
	public long maxSize(Pagination pagination) {
		return mapper.maxSize(pagination);
	}

	@Override
	public void save(Sms sms) {
		mapper.save(sms);
	}

	@Override
	public void update(Sms sms) {
		mapper.update(sms);
	}

	@Override
	public long delete(int[] ids) {
		if(ids.length>0){
			for(int id : ids){
				mapper.delete(id);
			}
			return 1l;
		}
		return 0l;
	}

}
