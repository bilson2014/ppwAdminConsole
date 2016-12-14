package com.panfeng.persist;

import java.util.List;

import com.panfeng.resource.model.ProductTmp;
import com.panfeng.resource.view.Pagination;

public interface ProductTmpMapper {

	List<ProductTmp> listWithPagination(Pagination view);

}
