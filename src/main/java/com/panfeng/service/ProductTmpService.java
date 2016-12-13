package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.ProductTmp;
import com.panfeng.resource.view.Pagination;

public interface ProductTmpService {

	List<ProductTmp> listWithPagination(Pagination view);

	long maxSize(Pagination view);

}
