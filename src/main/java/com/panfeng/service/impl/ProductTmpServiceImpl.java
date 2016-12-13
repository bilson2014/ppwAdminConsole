package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.persist.ProductTmpMapper;
import com.panfeng.resource.model.ProductTmp;
import com.panfeng.resource.view.Pagination;
import com.panfeng.service.ProductTmpService;
@Service
public class ProductTmpServiceImpl implements ProductTmpService{

	@Autowired
	private ProductTmpMapper mapper;
	@Override
	public List<ProductTmp> listWithPagination(Pagination view) {
		
		return mapper.listWithPagination(view);
	}

	@Override
	public long maxSize(Pagination view) {
		// TODO Auto-generated method stub
		return 0;
	}

}
