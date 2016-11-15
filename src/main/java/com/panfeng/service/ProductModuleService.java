package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.ProductModule;

public interface ProductModuleService {

	List<ProductModule> list();

	boolean save(ProductModule productModule);

	boolean update(ProductModule productModule);

	long delete(long[] ids);

}
